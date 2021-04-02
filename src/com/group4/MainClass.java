package com.group4;

import com.group4.games.TICTACTOE;
import com.group4.util.Tile;
import com.group4.util.network.Client;
import com.group4.util.network.NetworkPlayer;
import com.group4.util.network.NetworkPlayerStates.InMatchPlayerTurnState;
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
        Client client = new Client("localhost", 7789);
        Thread thread = new Thread(client);
        client.registerObserver((Object object) -> {
            Client test = (Client) object;
            System.out.println(test.getMessage());
        });

        thread.start();
        NetworkPlayer networkPlayer = new NetworkPlayer("1010", "idea", new TICTACTOE(), client);


        //this function would be in a game object and change the player state accordingly
        client.registerObserver((Object object) -> {
            Client client1 = (Client) object;

            if (client1.getMessage().contains("GAME YOURTURN")){
                networkPlayer.setState(new InMatchPlayerTurnState());
                System.out.println("player may make a move");
            }
        });

        Scanner scanner = new Scanner(System.in);
        int input = 0;
        while((input = scanner.nextInt()) != 0){
            switch (input){
                case 1: networkPlayer.login();
                    break;

                case 2: networkPlayer.getAvailableGames();
                    break;

                case 3: networkPlayer.logout();
                    break;

                case 4: networkPlayer.subscribe("Tic-tac-toe");
                    break;

                case 5: networkPlayer.makeMove(new Tile(scanner.nextInt(), scanner.nextInt()));
                    break;

                case 6: networkPlayer.forfeit();
                    break;
            }
        }
    }

}
