package com.group4.controller;

import com.group4.util.Player;
import com.group4.util.Tile;
import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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

	static GridPane gameInfoPlayerOne;
	static GridPane gameInfoPlayerTwo;


	public Controller() {
		// TODO - Initialize
	}

	/**
	 * Swap the current scene and set the current controller
	 *
	 * @param stage
	 * @param resource
	 */
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
			this.gameInfoPlayerOne = FXMLLoader.load(getClass().getResource("GameScore.fxml"));
			this.gameInfoPlayerTwo = FXMLLoader.load(getClass().getResource("GameScore.fxml"));
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
				playerCircle.setFill(Color.WHITE);

				//get child 1 get childen(0) fill color
				((Text)((HBox)gameInfoPlayerOne.getChildren().get(1)).getChildren().get(0)).setFill(Color.BLACK);
				System.out.println(((Text)((HBox)gameInfoPlayerOne.getChildren().get(1)).getChildren().get(0)));

				gameInfoPlayerTwo.getChildren().get(3).setVisible(true);
			}else{
				((Circle)gameInfoPlayerOne.getChildren().get(0)).setFill(Color.WHITE);
				gameInfoPlayerOne.getChildren().get(3).setVisible(true);
				((Text)((HBox)gameInfoPlayerOne.getChildren().get(1)).getChildren().get(0)).setFill(Color.BLACK);
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

	/**
	 * Get the current controller that is controlling the view
	 *
	 * @return Controller
	 */
	public Controller getCurrentController(){
		return this.CurrentController;
	}

	public void toggleTurnImage(){
		System.out.println(gameInfoPlayerOne);
		if (gameInfoPlayerOne != null && gameInfoPlayerTwo != null){
			ImageView turnImagePlayerOne = (ImageView) gameInfoPlayerOne.getChildren().get(3);
			ImageView turnImagePlayerTwo = (ImageView) gameInfoPlayerTwo.getChildren().get(3);

			//set the player turn image to the opposite of current image
			turnImagePlayerOne.setVisible(!turnImagePlayerOne.isVisible());
			turnImagePlayerTwo.setVisible(!turnImagePlayerTwo.isVisible());
		}
	}

	/**
	 * Set the player turn image
	 *
	 * @param player the player who has the turn
	 */
	public void setTurnImage(String player){
		Platform.runLater(() -> {
			if (player.equals("p1")){
				gameInfoPlayerTwo.getChildren().get(3).setVisible(true);
				gameInfoPlayerOne.getChildren().get(3).setVisible(false);
			}else{
				gameInfoPlayerTwo.getChildren().get(3).setVisible(false);
				gameInfoPlayerOne.getChildren().get(3).setVisible(true);
			}
		});
	}

	/**
	 * Set the available score for player 1
	 *
	 * @param score current score of player
	 */
	public void setScorePlayer1(String score){
		Platform.runLater(() -> {
			((Text)((HBox)gameInfoPlayerOne.getChildren().get(1)).getChildren().get(0)).setText(score);
		});
	}

	/**
	 * Set the available score for player 2
	 *
	 * @param score current score of player
	 */
	public void setScorePlayer2(String score){
		Platform.runLater(() -> {
			((Text)((HBox)gameInfoPlayerTwo.getChildren().get(1)).getChildren().get(0)).setText(score);
		});
	}

	/**
	 * set The available moves for player 1 on screen
	 *
	 * @param availableMoves amount of available moves for player
	 */
	public void setAvailableMovesPlayer1(String availableMoves){
		Platform.runLater(() -> {
			((Text)((HBox)gameInfoPlayerOne.getChildren().get(2)).getChildren().get(0)).setText(availableMoves);
		});
	}

	/**
	 * set The available moves for player 2 on screen
	 *
	 * @param availableMoves amount of available moves for player
	 */
	public void setAvailableMovesPlayer2(String availableMoves){
		Platform.runLater(() -> {
			((Text)((HBox)gameInfoPlayerTwo.getChildren().get(2)).getChildren().get(0)).setText(availableMoves);
		});

	}
}
