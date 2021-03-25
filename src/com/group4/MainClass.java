package com.group4;

import com.group4.model.Board;
import com.group4.util.Tile;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainClass extends Application {
	static Board gameBoard;
	
	public static void main(String[] args) {
		 gameBoard = new Board();
		 gameBoard.create(3,3);
		 launch(args);
	}
	
	public Pane fillInBoard() {
		Pane root = new Pane();
		root.setPrefSize(600, 600);
		for(Tile tile : gameBoard.getGameBoard()) {
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
