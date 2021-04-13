package com.group4.util.network.NetworkPlayerStates;
import com.group4.util.network.NetworkPlayer;

/**
 * State interface with available commands for player in different states
 *
 * @author Gemar Koning
 */
public interface NetworkPlayerState{
    /**
     * login player to server
     *
     * @param player Network player
     * @return if login was successful
     */
    boolean login(NetworkPlayer player);

    /**
     * logout player to server
     *
     * @param player Network player
     */
    void logout(NetworkPlayer player);

    /**
     * get available game list for player
     *
     * @param player Network player
     */
    void getGameList(NetworkPlayer player);

    /**
     * Get online players connected to server
     *
     * @param player Network player
     */
    void getPlayerList(NetworkPlayer player);

    /**
     * subscribe player to game
     *
     * @param player Network player
     * @param game Game to subscribe to
     */
    void subscribePlayerToGame(NetworkPlayer player, String game);

    /**
     * Make a move on the board during a match
     *
     * @param player Network player
     * @param zet Tile position on board
     *
     * @return if player was allowed to make a move
     */
    boolean makeMove(NetworkPlayer player, int zet);

    /**
     * challenge online player on certain game
     *
     * @param player Network player
     * @param online_player name of player to challenge
     * @param game name of game to play
     */
    void challengePlayer(NetworkPlayer player, String online_player, String game);

    /**
     * accept a challenge from other online player
     *
     * @param player Network player
     * @param code challenge code
     */
    void acceptChallenge(NetworkPlayer player, String code);

    /**
     * give up on connected match
     *
     * @param player Network player
     */
    void forfeit(NetworkPlayer player);
}
