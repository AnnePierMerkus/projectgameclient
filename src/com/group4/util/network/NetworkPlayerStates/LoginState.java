package com.group4.util.network.NetworkPlayerStates;

import com.group4.util.Player;
import com.group4.util.network.NetworkPlayer;

/**
 * Class that represents the network player logged in
 *
 * @author Gemar Koning
 */
public class LoginState implements NetworkPlayerState {

    @Override
    public boolean login(NetworkPlayer player) {
        System.out.println("Cannot login player. Player is already logged in.");
        return false;
    }

    @Override
    public void logout(NetworkPlayer player) {
        System.out.println("Logging out player...");

        //send message to connected server
        player.getClient().sendMessage("LOGOUT");

        //remove all messages from session
        player.getClient().clearMessages();

        //set player to logout state
        player.setState(new LogoutState());
    }

    @Override
    public void getGameList(NetworkPlayer player) {
        System.out.println("getting game list from logged in player");
        player.getClient().sendMessage("GET gamelist");
    }

    @Override
    public void getPlayerList(NetworkPlayer player) {
        System.out.println("getting player list from logged in player");
        player.getClient().sendMessage("GET playerlist");
    }

    @Override
    public void subscribePlayerToGame(NetworkPlayer player, String game) {
        System.out.println("Subscribing to " + game);
        player.getClient().sendMessage("SUBSCRIBE " + game);
    }

    @Override
    public boolean makeMove(NetworkPlayer player, int zet) {
        System.out.println("Player is currently not in a match. Cannot make a move.");

        return false;
    }

    @Override
    public void challengePlayer(NetworkPlayer player, String online_player, String game) {
        System.out.println("Challenge " + online_player + " for game " + game);
        player.getClient().sendMessage("CHALLENGE \"" + online_player + "\" \"" + game + "\"");
    }

    @Override
    public void acceptChallenge(NetworkPlayer player, String code) {
        System.out.println("Accepting challenge: " + code);
        player.getClient().sendMessage("challenge accept " + code);
    }

    @Override
    public void forfeit(NetworkPlayer player) {
        System.out.println("Player is currently not in a match. Cannot Forfeit");
    }
}
