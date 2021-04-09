package com.group4.games;

import java.util.List;

import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.Tile;

public class TICTACTOE extends GameProperty {

	public TICTACTOE() {
		this.displayNames.put("p1", "X");
		this.displayNames.put("p2", "O");
	}

	@Override
	public int getBoardWidth() {
		return 3;
	}

	@Override
	public int getBoardHeight() {
		return 3;
	}
	
	@Override
	public void doSetup() {
		// No setup needed
	}

	@Override
	public String playerStart() {
		return "p1"; // X always starts
	}

	@Override
	public List<Tile> getAvailableOptions(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean makeMove(Tile tile, Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLegalMove(Tile tile, Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean gameHasEnded() {
		// TODO Auto-generated method stub
		return false;
	}

}
