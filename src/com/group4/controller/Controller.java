package com.group4.controller;

import com.group4.util.Tile;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/***
 * Default Controller
 * 
 * @author Anne Pier Merkus & Gemar Koning
 */
public class Controller {

	protected static Stage stage;

	private Controller CurrentController;

	static GridPane gameInfoPlayerOne;
	static GridPane gameInfoPlayerTwo;

	private Button quitButton;

	/**
	 * Swap the current scene and set the current controller
	 *
	 * @param stage
	 * @param resource
	 * @author Gemar Koning
	 */
	public void swap(Stage stage, String resource){
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
			Parent root = loader.load();

			this.CurrentController = loader.getController();

			Scene scene = new Scene(root, 500, 500);
			scene.getStylesheets().add(getClass().getResource("../test.css").toExternalForm());

			System.out.println(stage);
			stage.setScene(scene);
		}catch (Exception e){
			System.out.println("New scene could not be loaded: " + e);
			e.printStackTrace();
		}
	}

	/***
	 * Creates the gameview
	 * 
	 * @param gameType - The type of game to create the gameview for
	 * @param gameController - The controller currently in charge of the game
	 * @param multiplayer - Whether it is multiplayer or not
	 * @return StackPane - The Stackpane to add to the window
	 * @author Anne Pier Merkus
	 */
	public StackPane fillInBoard(GameController.GameType gameType, GameController gameController, boolean multiplayer) {
		StackPane root = new StackPane();
		GridPane parent = new GridPane();
		GridPane gameView = new GridPane();
		GridPane giveUp = new GridPane();

		try {
			Controller.gameInfoPlayerOne = FXMLLoader.load(getClass().getResource("GameScore.fxml"));
			Controller.gameInfoPlayerTwo = FXMLLoader.load(getClass().getResource("GameScore.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageView imageView = new ImageView();
		imageView.fitWidthProperty().bind(root.widthProperty());
		imageView.fitHeightProperty().bind(root.heightProperty());;
		Image image = new Image(getClass().getResource("green.jpg").toExternalForm());
		imageView.setImage(image);
		giveUp.setMaxHeight(50);
		giveUp.setMinHeight(50);
		giveUp.setMinWidth(487);
		giveUp.setMaxWidth(487);
		parent.setVgap(20);

		giveUp.setAlignment(Pos.CENTER);
		root.setAlignment(Pos.CENTER);
		parent.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(30, 30, 30, 30));
		gameInfoPlayerOne.setPrefSize(250, 400);
		gameInfoPlayerTwo.setPrefSize(250, 400);

		root.getChildren().add(imageView);
		root.getChildren().add(parent);

		this.quitButton = new Button("Geef op");
		quitButton.setMinWidth(80);
		quitButton.setMinHeight(40);
		quitButton.setMaxWidth(80);
		quitButton.setMaxHeight(40);
		this.quitButton.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: #000000; -fx-text-fill: #ffffff");
		this.quitButton.setOnAction((event) -> {
			if (!multiplayer)
			{
				swap(stage, "../MainMenu.fxml");
			}
		});
		giveUp.add(quitButton, 0, 0);
		parent.add(giveUp, 1, 1);

		parent.add(gameInfoPlayerOne, 0, 0);
		parent.add(gameView, 1, 0);
		parent.add(gameInfoPlayerTwo, 2, 0);

		if (!multiplayer)
			gameController.createGame(gameType);

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
	 * @author Gemar Koning
	 */
	public Controller getCurrentController(){
		return this.CurrentController;
	}

	/***
	 * Toggles the image that shows who has the turn to the right player
	 * 
	 * @author Gemar Koning
	 */
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
	 * @author Gemar Koning
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
	 * @author Gemar Koning
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
	 * @author Gemar Koning
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
	 * @author Gemar Koning
	 */
	public void setAvailableMovesPlayer1(String availableMoves){
		Platform.runLater(() -> {
			if ((int)availableMoves.charAt(availableMoves.length() - 1) == 0)
			{
				gameInfoPlayerOne.getChildren().get(4).setVisible(true);
			}else{
				gameInfoPlayerOne.getChildren().get(4).setVisible(false);
			}
			((Text)((HBox)gameInfoPlayerOne.getChildren().get(2)).getChildren().get(0)).setText(availableMoves);
		});
	}

	/**
	 * set The available moves for player 2 on screen
	 *
	 * @param availableMoves amount of available moves for player
	 * @author Gemar Koning
	 */
	public void setAvailableMovesPlayer2(String availableMoves){
		Platform.runLater(() -> {
			if ((int)availableMoves.charAt(availableMoves.length() - 1) == 0)
			{
				gameInfoPlayerTwo.getChildren().get(4).setVisible(true);
			}else{
				gameInfoPlayerTwo.getChildren().get(4).setVisible(false);
			}
			((Text)((HBox)gameInfoPlayerTwo.getChildren().get(2)).getChildren().get(0)).setText(availableMoves);
		});

	}

	/**
	 * Returns the game quit button
	 *
	 * @return the game quit button
	 * @author Gemar Koning
	 */
	public Button getQuitButton(){
		return this.quitButton;
	}
}
