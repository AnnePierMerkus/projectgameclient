package com.group4.model;

import com.group4.controller.GameController.Difficulty;
import com.group4.controller.GameController.GameType;
import com.group4.util.GameProperty;

public class GameOptions {

	// Difficulty holding enum value
	private Difficulty difficulty;
	
	// GameType holding enum value
	private GameType gameType;
	
	// GameProperty instance holding the game logic
	GameProperty game;
	
	/**
     * Instanciate GameProperty class for the gametype
     *
     * @param className
     * @param type
     * @return GameProperty subclass
     * @author Jasper van der Kooi
     */
    @SuppressWarnings("deprecation")
    private GameProperty instantiate(final String className, @SuppressWarnings("rawtypes") final Class type) {
    	try {
            return (GameProperty) type.cast(Class.forName(className).newInstance());
        } catch (InstantiationException
                | IllegalAccessException
                | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
	
    /***
     * Create a new game with given difficulty + gametype
     * 
     * @param difficulty
     * @param gameType
     */
	public GameOptions(Difficulty difficulty, GameType gameType) {
		this.difficulty = difficulty;
		this.gameType = gameType;
		
		this.game = this.instantiate("com.groep4.games." + gameType.toString().toUpperCase(), GameProperty.class);
		
	}
	
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	public GameType getGameType() {
		return this.gameType;
	}
	
}
