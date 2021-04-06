package com.group4;

import com.group4.controller.GameController;
import com.group4.controller.ViewController;
import com.group4.model.Board;
import com.group4.util.Tile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainClass extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		primaryStage.setTitle("Broekspuzzels en spellen");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
		Parent root = loader.load();

		Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(getClass().getResource("test.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
