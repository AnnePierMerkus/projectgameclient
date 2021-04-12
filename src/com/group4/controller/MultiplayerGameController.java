package com.group4.controller;

import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.network.NetworkPlayer;
import com.group4.util.network.NetworkPlayerStates.InMatchNoTurnState;

/**
 * Multiplayer game controller for creating multiplayer game
 *
 * @author Gemar Koning
 */
public class MultiplayerGameController extends GameController{

    private Player startingPlayer;

    @Override
    void createGame(GameType gameType) {
        this.game = new GameOptions(Difficulty.MEDIUM, gameType, (this.startingPlayer != null) ? this.startingPlayer.getId() : PlayerList.getPlayer("p1").getId());

        // Set PlayerState to has turn, turns will be monitored by server instead
        for(Player p : PlayerList.players.values()) {
            p.setPlayerState(Player.PlayerState.PLAYING_HAS_TURN);
            p.setGameProperty(this.game.getGameProperty());
        }

        //set player state
        ((NetworkPlayer) PlayerList.getPlayer("p1")).setState(new InMatchNoTurnState());
    }

    @Override
    void createGame(Difficulty difficulty, GameType gameType) {
        this.createGame(gameType);
    }

    @Override
    void endGame() {
        PlayerList.cleanUp();
    }

    public void setStartingPlayer(Player player){
        this.startingPlayer = player;
    }

}
