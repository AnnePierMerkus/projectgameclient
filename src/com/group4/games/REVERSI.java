package com.group4.games;

import java.util.ArrayList;
import java.util.HashMap;
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
		return 8;
	}
	
	@Override
	public void doSetup() {
		// Set the default values on the board
		this.game.getBoard().getTile(27).setOccupant(this.game.getPlayer("p1"));
		this.game.getBoard().getTile(36).setOccupant(this.game.getPlayer("p1"));
		this.game.getBoard().getTile(28).setOccupant(this.game.getPlayer("p2"));
		this.game.getBoard().getTile(35).setOccupant(this.game.getPlayer("p2"));
	}

	@Override
	public int playerStart() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Tile> getAvailableOptions(Player player) {
		ArrayList<Tile> availableOptions = new ArrayList<Tile>();
		HashMap<Integer, Tile> board = this.game.getBoard().getGameBoard();
		for(Tile tile : board.values()) {
			if(tile.getOccupant() == player) {
				for(int i = 0; i < 7; i++) {
					Tile currentTile = tile;
					int directionOffset = getDirectionOffset(i);
					boolean foundOpponentTile = false;
					while(currentTile.getIndex() + directionOffset >= 0 && currentTile.getIndex() + directionOffset <= 63) {
						currentTile = board.get(currentTile.getIndex() + directionOffset);
						if((currentTile.getOccupant() == player) || (currentTile.getOccupant() == null && !(foundOpponentTile))) {
							break;
						}
						else if(currentTile.getOccupant() != player && currentTile.getOccupant() != null) {
							foundOpponentTile = true;
							continue;
						}
						else if(currentTile.getOccupant() == null && foundOpponentTile) {
							availableOptions.add(currentTile);
							break;
						}						
					}
				}
			}
		}
		return availableOptions;
	}
	
	public HashMap<Integer, Tile> swapTiles(Tile tile, Player player) {
		HashMap<Integer, Tile> board = this.game.getBoard().getGameBoard();
		for(int i = 0; i < 7; i++) {
			Tile currentTile = tile;
			int directionOffset = getDirectionOffset(i);
			ArrayList<Tile> candidateTiles = new ArrayList<Tile>();
			while(currentTile.getIndex() + directionOffset >= 0 && currentTile.getIndex() + directionOffset <= 63) {
				currentTile = board.get(currentTile.getIndex() + directionOffset);
				if((currentTile.getOccupant() == player && candidateTiles.isEmpty()) || (currentTile.getOccupant() == null && candidateTiles.isEmpty())) {
					break;
				}
				else if(currentTile.getOccupant() != player && currentTile.getOccupant() != null) {
					candidateTiles.add(currentTile);
					continue;
				}
				else if(currentTile.getOccupant() == null && !(candidateTiles.isEmpty())) {
					for(Tile candidateTile : candidateTiles) {
						candidateTile.setOccupant(player);
						board.put(candidateTile.getIndex(), candidateTile);
					}	
					break;
				}	
			}
		}
		return board;
	}

	@Override
	public boolean makeMove(Tile tile, Player player) {
		if(this.isLegalMove(tile, player)) {
			tile.setOccupant(player);
			// TODO update change in board
			swapTiles(tile, player);
			return true;
		}
		return false;
	}

	@Override
	public boolean isLegalMove(Tile tile, Player player) {
		// TODO make it so that the getAvailableOptions method only gets called once every turn. 
		// this method should have access to the array of available moves without calling the getAvailableOption method.
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
	
	private int getDirectionOffset(int direction) {
		int offset = 0;
		switch(direction) {
			case 0:
				offset = -8;
			case 1:
				offset =  -7;
			case 2:
				offset =  +1;
			case 3:
				offset =  +9;
			case 4:
				offset =  +8;
			case 5:
				offset =  +7;
			case 6:
				offset =  -1;
			case 7:
				offset =  -9;
		}
		return offset;
	}
}
