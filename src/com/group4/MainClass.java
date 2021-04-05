package com.group4;

import com.group4.controller.MultiplayerController;
import com.group4.util.Tile;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

public class MainClass extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub

        this.networkTestCode();
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
