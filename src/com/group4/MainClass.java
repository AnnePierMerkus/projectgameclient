package com.group4;


import com.group4.controller.MultiplayerController;
import com.group4.util.Tile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Scanner;

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

	public void networkTestCode(){
        //network test code REMOVE IN FINAL VERSION
        MultiplayerController multiplayerController = new MultiplayerController();


        //the while loop represent the view that wil be implemented later
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        while((input = scanner.nextInt()) != 0){
            switch (input){
                case 1: multiplayerController.login();
                    break;

                case 2: multiplayerController.logout();
                    break;

                case 3: multiplayerController.getAvailableGames();
                    break;

                case 4: multiplayerController.subscribe("Tic-tac-toe");
                    break;

                case 5: multiplayerController.player.makeMove(new Tile(scanner.nextInt(), scanner.nextInt()));
                    break;

                case 6: multiplayerController.endGame();
                    break;
            }
        }
    }
}
