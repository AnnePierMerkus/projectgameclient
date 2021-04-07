package com.group4.controller;

import com.group4.model.GameOptions;
import com.group4.util.network.Client;
import com.group4.util.network.NetworkPlayer;
import com.group4.util.network.NetworkPlayerStates.InMatchNoTurnState;
import com.group4.util.network.NetworkPlayerStates.InMatchPlayerTurnState;
import com.group4.util.network.NetworkPlayerStates.LoginState;
import javafx.event.ActionEvent;

/**
 *
 *
 * @author Gemar Koning
 */
public class MultiplayerController extends GameController {
	
    protected Client client;

    protected Thread client_thread;

    public NetworkPlayer networkPlayer;

    public MultiplayerController(){
        //create client for communication with server
        this.client = new Client("localhost", 7789);

        //start client on new thread for responsiveness
        this.client_thread = new Thread(client);
        this.client_thread.start();

        this.networkPlayer = (NetworkPlayer) this.game.getPlayer("p1");

        //temporary code
        String[] board = new String[]{
                "[]","[]","[]",
                "[]","[]","[]",
                "[]","[]","[]"};

        //register method for ending and starting game
        this.client.registerObserver((Object object) -> {
            Client client1 = (Client) object; //cast object to client instance
            System.out.println(client1.getMessage());

            //temporary code needs to be in game itself
            if (client1.getMessage().contains("WIN") || client1.getMessage().contains("LOSS") || client1.getMessage().contains("DRAW")){
                System.out.println("Match over. Result: " + client1.getMessage());
                this.networkPlayer.setState(new LoginState());
            }

            //temporary code needs to be in game itself
            if (client1.getMessage().contains("YOURTURN")){
                this.networkPlayer.setState(new InMatchPlayerTurnState());
            }

            //temporary code for demo
            if (client1.getMessage().contains("GAME MOVE")){
                if (client1.messageToMap().get("PLAYER").equals("idea")){
                    board[Integer.parseInt(client1.messageToMap().get("MOVE"))] = "[*]";
                }else{
                    board[Integer.parseInt(client1.messageToMap().get("MOVE"))] = "[0]";
                }

                for (int i = 0; i < board.length; i++){
                    if (((i + 1) % 3) == 0){
                        System.out.println(board[i]);
                    }else {
                        System.out.print(board[i]);
                    }
                }
            }

            if (client1.getMessage().contains("MATCH")){
                System.out.println(client1.getMessage());
                if (client1.messageToMap().get("GAMETYPE").equals("Tic-tac-toe")){
                    this.createGame(GameType.TICTACTOE);
                }else {
                    this.createGame(GameType.REVERSI);
                }
            }
        });
    }

    //log the player into the multiplayer server and set username
    public void login(ActionEvent event){
        this.networkPlayer.setName("idea"); //name from view form field here
        this.networkPlayer.login();
    }

    //logout from server
    public void logout(){
        this.networkPlayer.logout();
    }

    //subscribe to game type
    public void subscribe(String type){
        this.networkPlayer.subscribe(type);// game name from view form field here
    }

    //get available games from server
    public void getAvailableGames(){
        this.networkPlayer.getAvailableGames();
    }

    @Override
    public void createGame(GameType gameType) {
    	// TODO - Add players here
        this.game = new GameOptions(Difficulty.MEDIUM, gameType, this.client);
        this.game.setGameState(GameState.PLAYING);

        //set player state
        this.networkPlayer.setState(new InMatchNoTurnState());
    }

    @Override
    public void createGame(Difficulty difficulty, GameType gameType) {
    	// TODO - Add players here
        this.game = new GameOptions(difficulty, gameType, this.client);
        this.game.setGameState(GameState.PLAYING);

        //set player state
        this.networkPlayer.setState(new InMatchNoTurnState());
    }

    @Override
    public void endGame() {
        this.networkPlayer.forfeit(); //give up on current game this wil end the game
    }
}
