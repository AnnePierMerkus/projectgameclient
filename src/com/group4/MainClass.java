package com.group4;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClass extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub


        //network demo code
        //this.networkTestCode();

		primaryStage.setTitle("Broekspuzzels en spellen");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
		Parent root = loader.load();

		Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(getClass().getResource("test.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
