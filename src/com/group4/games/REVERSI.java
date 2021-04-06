package com.group4.games;

import java.util.ArrayList;
import java.util.List;

import com.group4.model.GameOptions;
import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.Tile;

public class REVERSI extends GameProperty {

	public REVERSI(GameOptions gameoptions) {
		super(gameoptions);
		this.displayNames.put("p1", "1");
		this.displayNames.put("p2", "2");
	}
	
	@Override
	public int getBoardWidth() {
		return 8;
	}

	@Override
	public int getBoardHeight() {
		return 8;
	}

	@Override
	public int playerStart() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Tile> getAvailableOptions(Player player) {
		ArrayList<Tile> availableOptions = new ArrayList<Tile>();
		// TODO get and loop the board; for every stone check neighbours until whettger there is an empty spot or the board ends.
		return availableOptions;
	}

	@Override
	public boolean makeMove(Tile tile, Player player) {
		if(this.isLegalMove(tile, player)) {
			// TODO change or set the player of the given tile in the board.
			tile.setOccupant(player);
			return true;
		}
		return false;
	}

	@Override
	public boolean isLegalMove(Tile tile, Player player) {
		List<Tile> availableOptions = this.getAvailableOptions(player);
		if(availableOptions.isEmpty()) {
			return false;
		}
		else if(availableOptions.contains(tile)) {
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

}
