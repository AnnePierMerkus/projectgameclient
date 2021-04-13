package com.group4.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.group4.controller.GameController.GameType;
import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

/***
 * The REVERSI class defines the Reversi game.
 * Extends GameProperty.
 * 
 * @author Mobieljoy12 & GRTerpstra
 * @version 1.0
 * @since   2021-03-25
 */
public class REVERSI extends GameProperty {

	/***
	 * Get the direction offset for a given direction.
	 *
	 * @param direction - the direction in which you want to search.
	 * @return int - offset the number that calculates in which direction the program should search.
	 * @author GRTerpstra.
	 */
	private int getDirectionOffset(int direction) {
		int offset = 0;
		switch(direction) {
			case 0:
				offset = -8;
				break;
			case 1:
				offset =  -7;
				break;
			case 2:
				offset =  +1;
				break;
			case 3:
				offset =  +9;
				break;
			case 4:
				offset =  +8;
				break;
			case 5:
				offset =  +7;
				break;
			case 6:
				offset =  -1;
				break;
			case 7:
				offset =  -9;
				break;
		}
		return offset;
	}

	/**
	 * The constructor of the REVERSI class.
	 * This constructor instantiates the display names for the players.
	 * 
	 * @author mobieljoy12.
	 */
	public REVERSI() {
		this.displayNames.put("p1", "Wit");
		this.displayNames.put("p2", "Zwart");
	}
	
	/***
	 * Get the GameType that is currently running
	 * 
	 * @return GameType - The GameType that is running
	 * @author mobieljoy12.
	 */
	@Override
	public GameType getGameType() {
		return GameType.REVERSI;
	}

	/***
	 * The width of the board in number of columns.
	 * 
	 * @return int - Number of columns.
	 * @author GRTerpstra & mobieljoy12.
	 */
	@Override
	public int getBoardWidth() {
		return 8;
	}

	/***
	 * The height of the board in number of columns.
	 * 
	 * @return int - Number of columns.
	 * @author GRTerpstra & mobieljoy12.
	 */
	@Override
	public int getBoardHeight() {
		return 8;
	}

	/***
	 * The doSetup method instantiates the four starting pieces of the game.
	 * 
	 * @return Number of columns.
	 * @author GRTerpstra & mobieljoy12.
	 */
	@Override
	public void doSetup(String currentPlayerSetup) {

		// Set the default values on the board
		this.game.getBoard().getTile(28).setOccupant(PlayerList.getPlayer(currentPlayerSetup));
		this.game.getBoard().getTile(35).setOccupant(PlayerList.getPlayer(currentPlayerSetup));
		this.game.getBoard().getTile(27).setOccupant(PlayerList.getOtherPlayer(currentPlayerSetup));
		this.game.getBoard().getTile(36).setOccupant(PlayerList.getOtherPlayer(currentPlayerSetup));

		// Add these to the filled Tiles
		this.game.getBoard().addFilledTile(PlayerList.getPlayer(currentPlayerSetup), this.game.getBoard().getTile(28));
		this.game.getBoard().addFilledTile(PlayerList.getPlayer(currentPlayerSetup), this.game.getBoard().getTile(35));
		this.game.getBoard().addFilledTile(PlayerList.getOtherPlayer(currentPlayerSetup), this.game.getBoard().getTile(27));
		this.game.getBoard().addFilledTile(PlayerList.getOtherPlayer(currentPlayerSetup), this.game.getBoard().getTile(36));

		// Save init state to previous board so it can be reverted back to
		this.game.getBoard().savePrevious(this.game.getBoard().getTile(28), PlayerList.getPlayer(currentPlayerSetup));
		this.game.getBoard().savePrevious(this.game.getBoard().getTile(35), PlayerList.getPlayer(currentPlayerSetup));
		this.game.getBoard().savePrevious(this.game.getBoard().getTile(27), PlayerList.getOtherPlayer(currentPlayerSetup));
		this.game.getBoard().savePrevious(this.game.getBoard().getTile(36), PlayerList.getOtherPlayer(currentPlayerSetup));

		// Set move counter to 1 so the first move can be made
		this.game.getBoard().incMoveCounter();

	}

	/***
	 * This method returns the player that should start the game
	 * 
	 * @return String - P1 (white) is the player that should start the game.
	 * @author GRTerpstra & mobieljoy12
	 */
	@Override
	public String playerStart() {
		// Wit always starts
		return "p1";
	}

	/**
	 * The getAvailableOptions method calculates all the moves the given player can make.
	 * 
	 * @param player - The player whose available moves should be calculates.
	 * @return ArrayList<Tile> - List of available moves.
	 * @author GRTerpstra.
	 */
	@Override
	public List<Tile> getAvailableOptions(Player player) {
		HashMap<Integer, Tile> availableOptions = new HashMap<Integer, Tile>();
		for(Tile tile : this.game.getBoard().getGameBoard().values()) {
			if(tile.getOccupant() == player) {
				for(int i = 0; i < 8; i++) {
					Tile currentTile = tile;
					int directionOffset = getDirectionOffset(i);
					boolean foundOpponentTile = false;
					while(currentTile.getIndex() + directionOffset >= 0 && currentTile.getIndex() + directionOffset <= 63) {
						if(		(i == 0 && (currentTile.getIndex() < 8)) 												||
								(i == 1 && ((currentTile.getIndex() < 8) || (currentTile.getIndex() + 1) % 8 == 0)) 	||
								(i == 2 && ((currentTile.getIndex() + 1) % 8 == 0)) 									||
								(i == 3 && ((((currentTile.getIndex() + 1) % 8 == 0)) || (currentTile.getIndex() > 55)))||
								(i == 4 && (currentTile.getIndex() > 55)) 												||
								(i == 5 && ((currentTile.getIndex() > 55) || currentTile.getIndex() % 8 == 0)) 			||
								(i == 6 && (currentTile.getIndex() % 8 == 0)) 											||
								(i == 7 && ((currentTile.getIndex() % 8 == 0) || currentTile.getIndex() < 8))
						)
						{
							break;
						}
						currentTile = this.game.getBoard().getGameBoard().get(currentTile.getIndex() + directionOffset);
						if((currentTile.getOccupant() == player) || (currentTile.getOccupant() == null && !(foundOpponentTile))) {
							break;
						}
						else if(currentTile.getOccupant() != player && currentTile.getOccupant() != null) {
							foundOpponentTile = true;
							continue;
						}
						else if(currentTile.getOccupant() == null && foundOpponentTile) {
							availableOptions.put(currentTile.getIndex(), currentTile);
							break;
						}
					}
				}
			}
		}
		return new ArrayList<Tile>(availableOptions.values());
	}

	/**
	 * The swapTiles method changes the occupant of the tiles that should change after the last move.
	 * 
	 * @param tile - the tile on which the last move was made.
	 * @param player - the Player who made the last move.
	 * @author GRTerpstra.
	 */
	public void swapTiles(Tile tile, Player player) {
		HashMap<Integer, Tile> board = this.game.getBoard().getGameBoard();
		for(int i = 0; i < 8; i++) {
			Tile currentTile = tile;
			int directionOffset = getDirectionOffset(i);
			ArrayList<Tile> candidateTiles = new ArrayList<Tile>();
			while(currentTile.getIndex() + directionOffset >= 0 && currentTile.getIndex() + directionOffset <= 63) {
				if(		(i == 0 && (currentTile.getIndex() < 8)) 												||
						(i == 1 && ((currentTile.getIndex() < 8) || (currentTile.getIndex() + 1) % 8 == 0)) 	||
						(i == 2 && ((currentTile.getIndex() + 1) % 8 == 0)) 									||
						(i == 3 && ((((currentTile.getIndex() + 1) % 8 == 0)) || (currentTile.getIndex() > 55)))||
						(i == 4 && (currentTile.getIndex() > 55)) 												||
						(i == 5 && ((currentTile.getIndex() > 55) || currentTile.getIndex() % 8 == 0)) 			||
						(i == 6 && (currentTile.getIndex() % 8 == 0)) 											||
						(i == 7 && ((currentTile.getIndex() % 8 == 0) || currentTile.getIndex() < 8))
				)
				{
					break;
				}
				currentTile = board.get(currentTile.getIndex() + directionOffset);
				if((currentTile.getOccupant() == player && candidateTiles.isEmpty()) || (currentTile.getOccupant() == null)) {
					break;
				}
				else if(currentTile.getOccupant() != player && currentTile.getOccupant() != null) {
					candidateTiles.add(currentTile);
					continue;
				}
				else if(currentTile.getOccupant() == player && !(candidateTiles.isEmpty())) {
					for(Tile candidateTile : candidateTiles) {
						this.game.getBoard().savePrevious(candidateTile, candidateTile.getOccupant());
						this.game.getBoard().getTile(candidateTile.getIndex()).setOccupant(player);
					}
					break;
				}
			}
		}
	}

	/**
	 * The makeMove method implements all the changes made to the board after checking if the move is legal.
	 * 
	 * @param tile - The tile on which the move should be made.
	 * @param player - the player who makes the move.
	 * @return boolean - true if the move has been made, false otherwise.
	 * @author GRTerpstra.
	 */
	@Override
	public boolean makeMove(Tile tile, Player player) {
		if(this.checkGameEnded()) return false;
		if(this.isLegalMove(tile, player)) {
			this.game.getBoard().savePrevious(tile, tile.getOccupant());
			tile.setOccupant(player);
			swapTiles(tile, player);
			this.game.getBoard().incMoveCounter(); // Increment move counter, this move is done
			this.gameHasEnded();
			return true;
		}
		return false;
	}

	/**
	 * The isLegalMove method checks if the move that is about to happen is legal.
	 * 
	 * @param tile - The tile on which the move should be made.
	 * @param player - the player who makes the move.
	 * @return boolean - true if the move is legal, false otherwise.
	 * @author GRTerpstra.
	 */
	@Override
	public boolean isLegalMove(Tile tile, Player player) {
		List<Tile> availableOptions = this.getAvailableOptions(player);
		player.setAvailableOptions(availableOptions);
		if(availableOptions.isEmpty()) {
			return false;
		}
		else if(availableOptions.contains(tile)) {
			return true;
		}
		return false;
	}

	/**
	 * The gameHasEnded method checks if the current game should terminate.
	 * 
	 * @return boolean - true if the game should end, false otherwise.
	 * @author mobieljoy12.
	 */
	@Override
	public boolean gameHasEnded() {
		if(!PlayerList.getPlayer("p1").hasMovesLeft() && !PlayerList.getPlayer("p2").hasMovesLeft()) {
			this.gameEnded = true;
			System.out.println("game ended");


			return true;
		}else if(!PlayerList.getPlayer("p1").hasMovesLeft() || !PlayerList.getPlayer("p2").hasMovesLeft()) {
			if(this.game.getBoard().getGameBoard().size() < 5) {
				this.matchPoint = true;

			}
		}else {
			this.matchPoint = false;
		}
		return false;
	}

	/**
	 * The getPlayerWon method checks and returns the player who has won the game.
	 * 
	 * @return Player - the player who has won the game.
	 * @author mobieljoy12.
	 */
	@Override
	public Player getPlayerWon() {
		HashMap<String, Integer> scores = this.game.getBoard().getScores();
		if(!this.gameEnded) {
			return null;
		}else {
			if(scores.get(this.game.getPlayerTurn()) > scores.get(PlayerList.getOtherPlayerId(this.game.getPlayerTurn()))) {
				return PlayerList.getPlayer(this.game.getPlayerTurn());
			}else if(scores.get(this.game.getPlayerTurn()) < scores.get(PlayerList.getOtherPlayerId(this.game.getPlayerTurn()))) {
				return PlayerList.getOtherPlayer(this.game.getPlayerTurn());
			}
			return null;
		}
	}

}