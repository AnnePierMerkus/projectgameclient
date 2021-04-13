package com.group4.AI;

import java.util.List;

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
    		switch (this.gameOptions.getDifficulty())
			{
				case EASY:
					this.ai.setAIType(this.gameOptions, gameType, 0);
					break;
				case MEDIUM:
				default:
					this.ai.setAIType(this.gameOptions, gameType, 2);
					break;
				case HARD:
					this.ai.setAIType(this.gameOptions, gameType, 5);
					break;
			}

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
     * @author mobieljoy12
     */
	public AIPlayer(String id) {
		super(id);
	}

	/***
	 * Set GameOptions pointer
	 *
	 * @param gameOptions
	 * @author mobieljoy12
	 */
	public void setGameOptions(GameOptions gameOptions) {
		this.gameOptions = gameOptions;
	}

	@Override
	public void makeMove(Tile tile) {
		// Empty
	}

	@Override
	public void setPlayerState(PlayerState state) {
		this.playerState = state;
		if(state.equals(PlayerState.PLAYING_HAS_TURN)) {
			if(this.ai != null) {
				Thread thread = new Thread(() -> {
					List<Tile> options = getAvailableOptions();
					if(!options.isEmpty()) {

							this.gameProperty.makeMove(this.ai.makeMove(getAvailableOptions()), this);

					}
					this.notifyObservers();
					});
				thread.start();
			}
		}
	}
	
	@Override
	public void setGameProperty(GameProperty game) {
		this.gameProperty = game;
		this.instantiate("com.group4.AI." + this.gameProperty.getGameType().toString().toUpperCase() + "AI", AI.class, game.getGameType());
	}

}
