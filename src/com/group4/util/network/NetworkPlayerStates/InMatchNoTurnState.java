package com.group4.util.network.NetworkPlayerStates;

import com.group4.util.network.NetworkPlayer;

/**
 * Player is in a match but has no turn
 *
 * @author Gemar Koning
 */
public class InMatchNoTurnState implements NetworkPlayerState {
    @Override
    public void login(NetworkPlayer player) {
        System.out.println("Cannot login player. Player is already logged in.");
    }

    @Override
    public void logout(NetworkPlayer player) {
        System.out.println("Player is in a match. Cannot logout player.");
    }

    @Override
    public void getGameList(NetworkPlayer player) {
        System.out.println("Player is in a match. Cannot retrieve game list.");
    }

    @Override
    public void subscribePlayerToGame(NetworkPlayer player, String game) {
        System.out.println("Player is in a match. Cannot subscribe player to game.");
    }

    @Override
    public void makeMove(NetworkPlayer player, int zet) {
        System.out.println("It is not the players turn. Cannot make a move");
    }

    @Override
    public void challengePlayer(NetworkPlayer player, String online_player, String game) {
        System.out.println("Player is in a match. Cannot challenge player.");
    }

    @Override
    public void acceptChallenge(NetworkPlayer player, String code) {
        System.out.println("Player is in a match. Cannot accept a challenge.");
    }

    @Override
    public void forfeit(NetworkPlayer player) {
        System.out.println("Giving up... \n The next time you feel like giving up; Remember, The enemy is watching. They wan't to see you fail, Never show your weakness.");

        //sending giving up command
        player.getClient().sendMessage("FORFEIT");

        //set back to login state
        player.setState(new LoginState());
    }
}
