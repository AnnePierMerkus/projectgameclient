package com.group4.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.group4.controller.GameController.GameType;
import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

public class TICTACTOE extends GameProperty {
	
	private Player playerWon = null;
	
	public TICTACTOE() {
		this.displayNames.put("p1", "X");
		this.displayNames.put("p2", "O");
	}

	@Override
	public GameType getGameType() {
		return GameType.TICTACTOE;
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
	public void doSetup(String currentPlayerSetup) {
		// No setup needed
	}

	@Override
	public String playerStart() {
		return "p1"; // X always starts
	}

	@Override
	public List<Tile> getAvailableOptions(Player player) {
		ArrayList<Tile> availableOptions = new ArrayList<Tile>();
		HashMap<Integer, Tile> board = this.game.getBoard().getGameBoard();
		for(Tile tile : board.values()) {
			if(tile.getOccupant() == null) {
				availableOptions.add(tile);
			}
		}
		return availableOptions;
	}

	@Override
	public boolean makeMove(Tile tile, Player player) {
		if(tile != null) {
		}		
		if(this.isLegalMove(tile, player)) {
			tile.setOccupant(player);
			this.gameHasEnded();
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
		return false;
	}
	
	@Override
	public boolean gameHasEnded() {
		for(Player player : PlayerList.players.values()) {
			ArrayList<Integer> playerTiles = new ArrayList<Integer>();
			for(Tile tile : this.game.getBoard().getGameBoard().values()) {
				if(tile.getOccupant() == player) {
					playerTiles.add(tile.getIndex());
				}
			}
			if((playerTiles.contains(0) && playerTiles.contains(1) && playerTiles.contains(2)) ||
			   (playerTiles.contains(3) && playerTiles.contains(4) && playerTiles.contains(5)) ||
			   (playerTiles.contains(6) && playerTiles.contains(7) && playerTiles.contains(8)) ||
			   (playerTiles.contains(0) && playerTiles.contains(3) && playerTiles.contains(6)) ||
			   (playerTiles.contains(1) && playerTiles.contains(4) && playerTiles.contains(7)) ||
			   (playerTiles.contains(2) && playerTiles.contains(5) && playerTiles.contains(8)) ||
			   (playerTiles.contains(0) && playerTiles.contains(4) && playerTiles.contains(8)) ||
			   (playerTiles.contains(2) && playerTiles.contains(4) && playerTiles.contains(6))
			  ) {
				this.playerWon = player;
				this.gameEnded = true;
				return true;
			}
			if(this.getAvailableOptions(player).isEmpty()) {
				this.gameEnded = true;
				return true;
			}
		}		
		return false;
	}

	@Override
	public Player getPlayerWon() {
		return this.playerWon;
	}

}
