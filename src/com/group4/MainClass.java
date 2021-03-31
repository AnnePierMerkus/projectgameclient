package com.group4;

import com.group4.games.TICTACTOE;
import com.group4.util.network.Client;
import com.group4.util.network.NetworkPlayer;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainClass extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
        /** network test code REMOVE IN FINAL VERSION
		Client client = new Client("localhost", 7789);
		Thread thread = new Thread(client);
		client.registerObserver((Object object) -> {
		    Client test = (Client) object;
            System.out.println(test.getMessage());
		});

		thread.start();
		NetworkPlayer networkPlayer = new NetworkPlayer("1010", "idea", new TICTACTOE(), client);
		networkPlayer.login();
		networkPlayer.getAvailableGames();
         **/
	}

}
