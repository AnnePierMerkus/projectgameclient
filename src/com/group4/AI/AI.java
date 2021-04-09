package com.group4.AI;

import com.group4.model.Board;
import com.group4.util.Tile;

import java.util.List;


public abstract class AI {
	
	// The board the AI can work with
	Board AIBoard;
	
	public void updateBoardFromGame() {
		
	}
	
    public abstract Tile makeMove(List<Tile> availableOptions);
    
}
