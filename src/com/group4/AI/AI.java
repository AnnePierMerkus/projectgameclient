package com.group4.AI;

import com.group4.controller.GameController.GameType;
import com.group4.model.GameOptions;
import com.group4.util.Tile;

import java.util.List;


public abstract class AI {
	
	// The Game the AI can work with
	GameAI gameai;
	
	public void updateBoardFromGame() {
		
	}
	
	public void setAIType(GameOptions gameOptions, GameType gameType) {
		this.gameai = new GameAI(gameType);
		this.gameai.setGameOptions(gameOptions);
		this.gameai.updateFromGame();
    }
	
    public abstract Tile makeMove(List<Tile> availableOptions);
    
}
