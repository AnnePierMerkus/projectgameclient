package com.group4.AI;

import com.group4.controller.GameController.GameType;
import com.group4.model.GameOptions;
import com.group4.util.Tile;

import java.util.List;


public abstract class AI {
	
	// The Game the AI can work with
	GameAI gameai;

	// The depth for the AI, how many moves ahead it will look
	int depth;
	
	/***
	 * Set the AI type to the GameType given + a difficulty given in depth
	 * 
	 * @param gameOptions - The game currently going on
	 * @param gameType - The gametype
	 * @param depth - The difficulty given in depth
	 * @author AnnePierMerkus & mobieljoy12
	 */
	public void setAIType(GameOptions gameOptions, GameType gameType, int depth) {
		this.depth = depth;
		this.gameai = new GameAI(gameType);
		this.gameai.setGameOptions(gameOptions);
		this.gameai.updateFromGame();
	}

    public abstract Tile makeMove(List<Tile> availableOptions);

}
