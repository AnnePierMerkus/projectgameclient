package com.group4.util.network;

import com.group4.util.Player;
import com.group4.util.Tile;
import com.group4.util.network.NetworkPlayerStates.InMatchPlayerTurnState;
import com.group4.util.network.NetworkPlayerStates.LogoutState;
import com.group4.util.network.NetworkPlayerStates.NetworkPlayerState;

/**
 * Network connected player
 *
 * this player class represents YOU as the player connected to the server
 *
 * @author Gemar Koning
 */
public class NetworkPlayer extends Player {
	
    private String name;

    private Client client;

    private NetworkPlayerState state;

    /**
     * Create a new network connected player
     *
     * @param id user id
     * @param client client connected to server
     *
     */
    public NetworkPlayer(String id, Client client) {
        super(id);
        this.client = client;

        //set default player name
        this.name = this.getId();

        //set default state
        this.state = new LogoutState();
    }

    /**
     *
     * @param id user id
     * @param name user display name for server
     * @param client
     */
    public NetworkPlayer(String id, String name, Client client) {
        super(id);
        this.client = client;

        //set name
        this.name = name;

        //set default state
        this.state = new LogoutState();
    }


    /**
     * Make a move and let the server know which move you have done
     *
     * @param tile - The Tile to make a move on
     * @return
     */
    @Override
    public void makeMove(Tile tile, int threadId) {
        System.out.println("");
        if (this.state instanceof InMatchPlayerTurnState){
            if (this.gameProperty.makeMove(tile, this, threadId)){
                this.state.makeMove(this, tile.getIndex());
            }else{
                System.out.println("Move is illegal");
            }
        }else{
            System.out.println("Network Player has not the turn");
        }
    }

    /**
     * Log player into multiplayer server
     */
    public boolean login(){
        return this.state.login(this);
    }

    /**
     * Logout from multiplayer server
     */
    public void logout(){
        this.state.logout(this);
    }

    /**
     * Subscribe player to available online game
     *
     * @param game string which represents online game to subscribe to
     */
    public void subscribe(String game){
        this.state.subscribePlayerToGame(this, game);
    }

    /**
     * forfeit on current playing game
     */
    public void forfeit(){
        this.state.forfeit(this);
    }

    /**
     * Challenge player on a certain game
     *
     * @param player player that we want to challenge
     * @param game game that we want to challenge player on
     */
    public void challenge(String player, String game){
        this.state.challengePlayer(this, player, game);
    }

    /**
     * accept challenge from other online player
     *
     * @param challenge_code code from challenge that we want to accept as player
     */
    public void acceptChallenge(String challenge_code){
        this.state.acceptChallenge(this, challenge_code);
    }

    /**
     * Get available game list from server
     */
    public void getAvailableGames(){
        this.state.getGameList(this);
    }

    /**
     * Get online players connected to server
     */
    public void getOnlinePlayers(){
        this.state.getPlayerList(this);
    }

    /**
     * Set current player state
     *
     * @param playerState State in which player currently is.
     */
    public void setState(NetworkPlayerState playerState){
        this.state = playerState;
    }

    /**
     * return client which is connected to server
     *
     * @return connected client
     */
    public Client getClient() {
        return client;
    }

    /**
     * get player name
     *
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Set player name
     *
     * @param name player name
     */
    public void setName(String name) {
        this.name = name;
    }
}
