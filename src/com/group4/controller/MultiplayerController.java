package com.group4.controller;

import com.group4.model.GameOptions;
import com.group4.util.network.Client;

/**
 *
 *
 * @author Gemar Koning
 */
public class MultiplayerController extends GameController {
    protected Client client;

    protected Thread client_thread;

    @Override
    void createGame(GameType gameType) {
        this.client = new Client("localhost", 7789);
        this.client_thread = new Thread(client);
        this.client_thread.start();

        this.game = new GameOptions(Difficulty.MEDIUM, gameType);
        this.game.setGameState(GameState.PLAYING);
    }

    @Override
    void createGame(Difficulty difficulty, GameType gameType) {
        this.client = new Client("localhost", 7789);
        this.client_thread = new Thread(client);
        this.client_thread.start();

        this.game = new GameOptions(difficulty, gameType);
        this.game.setGameState(GameState.PLAYING);
    }

    @Override
    void endGame() {
        this.client.close(); //close connection to server. client thread should stop automatically
    }
}
