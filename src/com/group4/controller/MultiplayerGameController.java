package com.group4.controller;

import com.group4.model.GameOptions;
import com.group4.util.network.Client;

public class MultiplayerGameController extends GameController {
    protected Client client;

    protected Thread client_thread;

    @Override
    void createGame(GameType gameType) {
        //create new connection to server
        this.client = new Client("localhost", 7789);

        //start listening to server on a new thread
        this.client_thread = new Thread(this.client);
        this.client_thread.start();

        //create new game and set state
        this.game = new GameOptions(Difficulty.MEDIUM, gameType);
        this.game.setGameState(GameState.PLAYING);
    }

    @Override
    void createGame(Difficulty difficulty, GameType gameType) {
        //create a new connection to the server
        this.client = new Client("localhost", 7789);

        //start listening to server on a new thread
        this.client_thread = new Thread(this.client);
        this.client_thread.start();

        this.game = new GameOptions(difficulty, gameType);
        this.game.setGameState(GameState.PLAYING);
    }

    @Override
    void endGame() {
        //closing socket and input stream. should automatically finish the thread
        this.client.close();
    }
}
