package com.group4.util;

import java.util.HashMap;

public class PlayerList {

	// Save the players into a map with Id => Player object
	public static HashMap<String, Player> players = new HashMap<String, Player>();
	
	/***
	 * Get Player
	 * If multiplayer, p1 can be cast to network player
	 * E.g. NetworkPlayer networkPlayer = (NetworkPlayer) GameOptions.getPlayer("p1");
	 * 
	 * @param id - String playerId
	 * @return Player - The player found, or NULL
	 * @author mobieljoy12
	 */
	public static void addPlayer(Player player) {
		players.put(player.getId(), player);
	}
	
	/***
	 * Get Player
	 * If multiplayer, p1 can be cast to network player
	 * E.g. NetworkPlayer networkPlayer = (NetworkPlayer) GameOptions.getPlayer("p1");
	 * 
	 * @param id - String playerId
	 * @return Player - The player found, or NULL
	 * @author mobieljoy12
	 */
	public static Player getPlayer(String id) {
		if(players.containsKey(id)) {
			return players.get(id);
		}
		return null;
	}
	
	/***
	 * Get the size of the PlayerList
	 * 
	 * @return int - Size
	 * @author mobieljoy12
	 */
	public static int size() {
		return players.size();
	}
	
	/***
	 * Get all players
	 * 
	 * @return HashMap<String, Player> - HashMap with all players with id
	 * @author mobieljoy12
	 */
	public static HashMap<String, Player> getPlayers(){
		return players;
	}
	
	/***
	 * Get the other player id than the player id passed in
	 * 
	 * @param id - String id that you have
	 * @return String - Player id that is not with the id passed in
	 * @author mobieljoy12
	 */
	public static String getOtherPlayerId(String id) {
		return (id.equals("p1")) ? "p2" : "p1";
	}
	
	/***
	 * Get the other player than the player passed in
	 * 
	 * @param id - String id of the player you have
	 * @return Player - Player that is not with the id passed in
	 * @author mobieljoy12
	 */
	public static Player getOtherPlayer(String id) {
		return (id.equals("p1")) ? PlayerList.getPlayer("p2") : PlayerList.getPlayer("p1");
	}
	
	/***
	 * Make sure all players are no longer in game
	 * 
	 * @author mobieljoy12
	 */
	public static void cleanUp() {
		players.values().forEach((p) -> p.setGameProperty(null));
	}
	
}
