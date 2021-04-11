package com.group4.AI;

import com.group4.controller.GameController.GameType;
import com.group4.model.GameOptions;
import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.Tile;

public class AIPlayer extends Player {

	private AI ai = null;
	private GameOptions gameOptions = null;
	
	/**
     * Instantiate AI property for given GameType
     *
     * @param className
     * @param type
     * @return GameProperty subclass
     * @author mobieljoy12
     */
    @SuppressWarnings("deprecation")
    private void instantiate(final String className, @SuppressWarnings("rawtypes") final Class type, GameType gameType) {
    	try {
    		this.ai = (AI) type.cast(Class.forName(className).newInstance());
    		this.ai.setAIType(this.gameOptions, gameType);
        } catch (InstantiationException
                | IllegalAccessException
                | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
	
    /***
     * AIPlayer instantiates a specific AI class when a GameType is set
     * 
     * @param id - Player id
     */
	public AIPlayer(String id) {
		super(id);
	}
	
	public void setGameOptions(GameOptions gameOptions) {
		this.gameOptions = gameOptions;
	}
	
	@Override
	public void makeMove(Tile tile) {
		// Don't allow moves if player does not have the turn, only happens when gameproperty is already set
		if(this.playerState != PlayerState.PLAYING_HAS_TURN) return;
		if(this.gameProperty.gameHasEnded()) return;
		try {
			System.out.println("sleep" + this.getId());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.gameProperty.makeMove(this.ai.makeMove(getAvailableOptions()), this);
		System.out.println("n" + tile.getWeight()); // If move is legal, notify observers to toggle turn
		this.notifyObservers();
	}
	
	@Override
	public void setGameProperty(GameProperty game) {
		this.gameProperty = game;
		this.instantiate("com.group4.AI." + this.gameProperty.getGameType().toString().toUpperCase() + "AI", AI.class, game.getGameType());
	}

}
