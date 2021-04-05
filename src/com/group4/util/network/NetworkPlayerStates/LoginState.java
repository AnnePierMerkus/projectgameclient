package com.group4.util.network.NetworkPlayerStates;

import com.group4.util.Player;
import com.group4.util.network.NetworkPlayer;

public class LoginState implements NetworkPlayerState {

    @Override
    public void login(NetworkPlayer player) {
        System.out.println("Cannot login player. Player is already logged in.");
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
        System.out.println("getting gamelist from logged in player");
        player.getClient().sendMessage("GET gamelist");
    }

    @Override
    public void subscribePlayerToGame(NetworkPlayer player, String game) {
        System.out.println("Subscribing to " + game);
        player.getClient().sendMessage("SUBSCRIBE " + game);
    }

    @Override
    public void makeMove(NetworkPlayer player, int zet) {
        System.out.println("Player is currently not in a match. Cannot make a move.");
    }

    @Override
    public void challengePlayer(NetworkPlayer player, String online_player, String game) {
        System.out.println("Challenge " + online_player + " for game " + game);
        player.getClient().sendMessage("CHALLENGE " + online_player + " " + game);
    }

    @Override
    public void acceptChallenge(NetworkPlayer player, String code) {

    }

    @Override
    public void forfeit(NetworkPlayer player) {
        System.out.println("Player is currently not in a match. Cannot Forfeit");
    }
}
