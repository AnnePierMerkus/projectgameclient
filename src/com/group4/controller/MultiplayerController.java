package com.group4.controller;

import com.group4.games.TICTACTOE;
import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.network.Client;
import com.group4.util.network.NetworkPlayer;

/**
 *
 *
 * @author Gemar Koning
 */
public class MultiplayerController extends GameController {
    protected Client client;

    protected Thread client_thread;

    protected NetworkPlayer player;

    public MultiplayerController(){
        //create client for communication with server
        this.client = new Client("localhost", 7789);

        //start client on new thread for responsiveness
        this.client_thread = new Thread(client);
        this.client_thread.start();
    }

    //log the player into the multiplayer server and set username
    public void login(){
        this.player.setName("idea"); //name from view form field here
        this.player.login();
    }

    @Override
    void createGame(GameType gameType) {
        this.game = new GameOptions(Difficulty.MEDIUM, gameType);
        this.game.setGameState(GameState.PLAYING);
    }

    @Override
    void createGame(Difficulty difficulty, GameType gameType) {
        this.game = new GameOptions(difficulty, gameType);
        this.game.setGameState(GameState.PLAYING);
    }

    @Override
    void endGame() {
        this.client.close(); //close connection to server. client thread should stop automatically
    }
}
