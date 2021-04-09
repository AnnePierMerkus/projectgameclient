package com.group4.util.network.NetworkPlayerStates;
import com.group4.util.Player;
import com.group4.util.network.NetworkPlayer;

/**
 * State interface with available commands for player in different states
 *
 * @author Gemar Koning
 */
public interface NetworkPlayerState{
    //login player to server
    void login(NetworkPlayer player);

    //logout player to server
    void logout(NetworkPlayer player);

    //get available game list for player
    void getGameList(NetworkPlayer player);

    //get online players connected to server
    void getPlayerList(NetworkPlayer player);

    //subscribe player to game
    void subscribePlayerToGame(NetworkPlayer player, String game);

    //make a move in connected match
    boolean makeMove(NetworkPlayer player, int zet);

    //challenge online player on certain game
    void challengePlayer(NetworkPlayer player, String online_player, String game);

    //accept a challenge from other online player
    void acceptChallenge(NetworkPlayer player, String code);

    //give up on connected match
    void forfeit(NetworkPlayer player);
}
