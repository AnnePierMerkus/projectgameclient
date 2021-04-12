package com.group4.controller;

import com.group4.util.Player;
import com.group4.util.Tile;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/***
 * Default Controller
 */
public class Controller {

	private Stage stage;

	private Controller CurrentController;

	GridPane gameInfoPlayerOne;
	GridPane gameInfoPlayerTwo;


	public Controller() {
		// TODO - Initialize
	}
	
	// TODO - View stuff ~ swapScene maybe?
	public void swap(Stage stage, String resource){
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
			Parent root = loader.load();

			this.CurrentController = loader.getController();

			Scene scene = new Scene(root, 500, 500);
			scene.getStylesheets().add(getClass().getResource("../test.css").toExternalForm());

			stage.setScene(scene);
		}catch (Exception e){
			System.out.println("New scene could not be loaded: " + e);
			e.printStackTrace();
		}
	}

	public StackPane fillInBoard(GameController.GameType gameType, GameController gameController, boolean multiplayer) {
		StackPane root = new StackPane();
		GridPane parent = new GridPane();
		GridPane gameView = new GridPane();

		try {
			gameInfoPlayerOne = FXMLLoader.load(getClass().getResource("GameScore.fxml"));
			gameInfoPlayerTwo = FXMLLoader.load(getClass().getResource("GameScore.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageView imageView = new ImageView();
		imageView.fitWidthProperty().bind(root.widthProperty());
		imageView.fitHeightProperty().bind(root.heightProperty());
		Image image = new Image(getClass().getResource("green.jpg").toExternalForm());
		imageView.setImage(image);

		root.setAlignment(Pos.CENTER);
		parent.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(30, 30, 30, 30));
		gameInfoPlayerOne.setPrefSize(250, 400);
		gameInfoPlayerTwo.setPrefSize(250, 400);

		root.getChildren().add(imageView);
		root.getChildren().add(parent);
		parent.add(gameInfoPlayerOne, 0, 0);
		parent.add(gameView, 1, 0);
		parent.add(gameInfoPlayerTwo, 2, 0);

		if (!multiplayer)
			gameController.createGame(GameController.Difficulty.EASY, gameType);

		//set first turn
		if (gameInfoPlayerOne != null && gameInfoPlayerTwo != null){
			if (gameController.game.getPlayerTurn().equals("p1")){
				Circle playerCircle = ((Circle)gameInfoPlayerOne.getChildren().get(0));
				playerCircle.setFill(Paint.valueOf("WHITE"));

				//get child 1 get childen(0) fill color

				gameInfoPlayerTwo.getChildren().get(3).setVisible(true);
			}else{
				((Circle)gameInfoPlayerOne.getChildren().get(0)).setFill(Paint.valueOf("WHITE"));
				gameInfoPlayerOne.getChildren().get(3).setVisible(true);
			}
		}

		Iterator<Map.Entry<Integer, Tile>> it = gameController.getOptions().getBoard().getGameBoard().entrySet().iterator();
		int row = 0;
		int column = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Tile> pair = it.next();
			gameView.add(pair.getValue(), column, row);
			column++;

			if ((pair.getKey() + 1) % gameController.getOptions().getBoard().getWidth() == 0)
			{
				column = 0;
				row++;
			}
		}
		this.toggleTurnImage();
		return root;
	}

	public Controller getCurrentController(){
		return this.CurrentController;
	}

	public void toggleTurnImage(){
		if (gameInfoPlayerOne != null && gameInfoPlayerTwo != null){
			ImageView turnImagePlayerOne = (ImageView) gameInfoPlayerOne.getChildren().get(3);
			ImageView turnImagePlayerTwo = (ImageView) gameInfoPlayerTwo.getChildren().get(3);

			//set the player turn image to the opposite of current image
			turnImagePlayerOne.setVisible(!turnImagePlayerOne.isVisible());
			turnImagePlayerTwo.setVisible(!turnImagePlayerTwo.isVisible());
		}
	}
}
