package com.group4.games;

import java.util.ArrayList;
import java.util.List;

import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.Tile;

public class REVERSI extends GameProperty {

	public REVERSI() {
		this.displayNames.put("p1", "1");
		this.displayNames.put("p2", "2");
	}
	
	@Override
	public int getBoardWidth() {
		return 8;
	}

	@Override
	public int getBoardHeight() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public int playerStart() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Tile> getAvailableOptions(Player player) {
		ArrayList availableOptions = new ArrayList<Tile>();
		// TODO get and loop the board; for every stone check neighbours until whettger there is an empty spot or the board ends.
		return availableOptions;
	}

	@Override
	public boolean makeMove(Tile tile, Player player) {
		if(isLegalMove(tile, player)) {
			// TODO change or set the player of the given tile in the board.
			return true;
		}
		return false;
	}

	@Override
	public boolean isLegalMove(Tile tile, Player player) {
		if(getAvailableOptions(player) == null) {
			return false;
		}
		else if(getAvailableOptions(player).contains(tile)) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean gameHasEnded() {
		// TODO if both players have no legal moves to do: true.
		return false;
	}

	@Override
	public int getPlayerAmount() {
		return 2;
	}

}
