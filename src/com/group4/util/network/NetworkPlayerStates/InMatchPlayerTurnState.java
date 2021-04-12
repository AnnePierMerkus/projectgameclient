package com.group4.util.network.NetworkPlayerStates;

import com.group4.util.network.NetworkPlayer;

/**
 * State represents when player is in a match and player may make a move
 *
 * @author Gemar Koning
 */
public class InMatchPlayerTurnState implements NetworkPlayerState{
    @Override
    public boolean login(NetworkPlayer player) {
        System.out.println("Cannot login player. Player is already logged in.");
        return false;
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
    public void getPlayerList(NetworkPlayer player) {
        System.out.println("Player is in a match. Cannot retrieve player list");
    }

    @Override
    public void subscribePlayerToGame(NetworkPlayer player, String game) {
        System.out.println("Player is in a match. Cannot subscribe player to game.");
    }

    @Override
    public boolean makeMove(NetworkPlayer player, int zet) {
        System.out.println("Making a move...");

        //send move to server
        player.getClient().sendMessage("MOVE " + zet);
        System.out.println("sending move commando to server");

        //set player to no turn state
        player.setState(new InMatchNoTurnState());

        return true;
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
