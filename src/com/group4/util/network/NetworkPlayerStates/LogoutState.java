package com.group4.util.network.NetworkPlayerStates;

import com.group4.util.Player;
import com.group4.util.network.NetworkPlayer;

/**
 * Class that represents Networkplayer when loggedOut
 *
 * @author Gemar Koning
 */
public class LogoutState implements NetworkPlayerState{

    @Override
    public void login(NetworkPlayer player) {
        System.out.println("Logging in player...");

        //send login message to server
        player.getClient().sendMessage("LOGIN " + player.getName());

        //go to login state for NetworkPlayer
        player.setState(new LoginState());
    }

    @Override
    public void logout(NetworkPlayer player) {
        System.out.println("Player is not logged in. Cannot logout.");
    }

    @Override
    public void getGameList(NetworkPlayer player) {
        System.out.println("Player is not logged in. Cannot retrieve game list.");
    }

    @Override
    public void subscribePlayerToGame(NetworkPlayer player, String game) {
        System.out.println("Player is not logged in. Cannot subscribe to game.");
    }

    @Override
    public void makeMove(NetworkPlayer player, int zet) {
        System.out.println("Player is not logged in. Cannot make a move.");
    }

    @Override
    public void challengePlayer(NetworkPlayer player, String online_player, String game) {
        System.out.println("Player is not logged in. Cannot challenge player.");
    }

    @Override
    public void acceptChallenge(NetworkPlayer player, String code) {
        System.out.println("Player is not logged in. Cannot accept challenge.");
    }

    @Override
    public void forfeit(NetworkPlayer player) {
        System.out.println("Player is not logged in. Cannot forfeit game.");
    }
}
