package com.group4.AI;

import com.group4.controller.GameController.GameType;
import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

public class GameAI extends GameOptions {
	
	// Holds the GameOptions value
	private GameOptions gameOptions;
	
	/***
	 * A mock game that the AI can create to perform the predictions
	 * 
	 * @param gameType - The gameType for the game
	 * @author mobieljoy12
	 */
	public GameAI(GameType gameType) {
		super(gameType);
	}
	
	/***
	 * Update this game from the actual game that is going on
	 *
	 * @author mobieljoy12
	 */
	public void updateFromGame() {
		// Update the board
		for(Tile tile : this.gameOptions.getBoard().getGameBoard(0).values()) {
			this.board.getTile(0, tile.getIndex()).setOccupant(tile.getOccupant(), 0);
		}
		
		// Update the previous board
		this.board.emptyAllPrevious(0);
		for(int moveCount : this.gameOptions.getBoard().getPreviousBoard(0).keySet()) {
			for(Tile tile : this.gameOptions.getBoard().getPreviousBoard(0).get(moveCount).values()) {
				this.board.savePrevious(0, moveCount, tile, tile.getOccupant());
			}
		}
		
		// Update the filled tiles
		this.board.resetFilledTiles(0);
		for(Player p : PlayerList.players.values()) {
			for(Tile tile : this.gameOptions.getBoard().getFilledTiles(0).get(p.getId()).values()) {
				this.board.addFilledTile(0, p, tile);
			}
		}
		
		// Update the moveCounter
		this.board.setMoveCounter(0, this.gameOptions.getBoard().getMoveCounter(0));
		
		// Update the playerturn
		this.playerTurn = this.gameOptions.getPlayerTurn();
	}

	public void copyThread(int threadFrom, int threadTo)
	{
		// Update the board
		//for(Tile tile : this.getBoard().getGameBoard(threadFrom).values()) {
		//	this.getBoard().getTile(threadTo, tile.getIndex()).setOccupant(tile.getOccupant(), threadTo);
		//}


		this.getBoard().createExtra(threadFrom, threadTo);

		// Update the previous board
		this.getBoard().emptyAllPrevious(threadFrom);
		for(int moveCount : this.getBoard().getPreviousBoard(threadFrom).keySet()) {
			for(Tile tile : this.getBoard().getPreviousBoard(threadFrom).get(moveCount).values()) {
				this.getBoard().savePrevious(threadTo, moveCount, tile, tile.getOccupant());
			}
		}
		// Update the filled tiles
		this.getBoard().resetFilledTiles(threadTo);
		for(Player p : PlayerList.players.values()) {
			for(Tile tile : this.getBoard().getFilledTiles(threadFrom).get(p.getId()).values()) {
				this.getBoard().addFilledTile(threadTo, p, tile);
			}
		}

		// Update the moveCounter
		this.getBoard().setMoveCounter(this.getBoard().getMoveCounter(threadFrom), threadTo);
	}
	/***
	 * Set the GameOptions
	 * 
	 * @param gameOptions
	 * @author mobieljoy12
	 */
	public void setGameOptions(GameOptions gameOptions) {
		this.gameOptions = gameOptions;
	}

	/***
	 * Get the current GameOptions
	 * 
	 * @return GameOptions
	 * @author mobieljoy12
	 */
	public GameOptions getGame() {
		return this.gameOptions;
	}

	/***
	 * Add a move to the prediction board
	 * 
	 * @param tileIndex - The index of the Tile to put the move on
	 * @param player - The Player to set to the tile
	 * @param threadId - The thread to make the move on
	 * @author mobieljoy12
	 */
	public void makePredictionMove(int tileIndex, Player player, int threadId) {
		//this.board.getTile(tileIndex).setOccupant(player);
		this.getGameProperty().makeMove(this.board.getTile(threadId, tileIndex), player, threadId);
		//this.getGame().makeMove(this.board.getTile(tileIndex), player);
	}
	
	@Override
	public String toggleTurn() {
		return (this.playerTurn.equals("p1")) ? "p2" : "p1";
	}
	
}
