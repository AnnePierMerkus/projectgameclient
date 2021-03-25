package com.group4;

import com.group4.controller.GameController;
import com.group4.controller.GameController.Difficulty;
import com.group4.controller.GameController.GameType;
import com.group4.model.Board;
import com.group4.util.Tile;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainClass extends Application {
	
	public static void main(String[] args) {
		 
		 launch(args);
	}
	
	public Pane fillInBoard() {
		Pane root = new Pane();
		new GameController(false).createGame(Difficulty.EASY, GameType.TICTACTOE);
		root.setPrefSize(600, 600);
		for(Tile tile : Board.getGameBoard()) {
			root.getChildren().add(tile);
		}
		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(fillInBoard()));
		primaryStage.show();
	}
}
