package com.group4.AI;

import java.util.List;
import java.util.Random;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

public class TICTACTOEAI extends AI {

	// The player the AI controls
    Player player;
    
    // The player not controller by the AI
    Player otherPlayer;

	@Override
	public Tile makeMove(List<Tile> availableOptions)
	{
		gameai.updateFromGame();

		player = PlayerList.getPlayer(gameai.getPlayerTurn());
		otherPlayer = PlayerList.getOtherPlayer(gameai.getPlayerTurn());

		if (this.depth == 0) {
			Random random = new Random();
			int rand = random.nextInt(availableOptions.size());
			return availableOptions.get(rand);
		} else {
			return bestMove(availableOptions);
		}

	}

	/**
	 * Make a move for the Ai and then start minimax to find the best move.
	 * 
	 * @return the best move for the Ai.
	 * @author AnnePierMerkus
	 */
	public Tile bestMove(List<Tile> availableOptions){

		List<Tile> options = this.gameai.getGameProperty().getAvailableOptions(player);
		int bestScore = Integer.MIN_VALUE;
		Tile move = null;

		for (Tile tile : options)
		{
			this.gameai.makePredictionMove(tile.getIndex(), player);
			int score = minimax(this.gameai.getBoard(), false);
			tile.reset();
			if (score > bestScore) {
				bestScore = score;
				move = tile;
			}
		}

		for (Tile tile : availableOptions)
		{
			if (tile.getIndex() == move.getIndex())
			{
				move = tile;
			}
		}

		return move;
	}

	/***
	 * The Minimax AI that will decide the best move to make
	 * 
	 * @param board - The board to check the moves on
	 * @param maximizing - Whether to get the min or max value
	 * @return won - Whether the AI won, 1 when it wins, -1 when it loses and 0 when it ties
     * @author AnnePierMerkus
	 */
	public int minimax(Board board, boolean maximizing)
	{
		if (gameai.getGameProperty().checkGameEnded()) {
			return gameai.getGameProperty().getPlayerWon() == null ? 0 : gameai.getGameProperty().getPlayerWon() == player ? 1 : -1;
		}

		if (maximizing) {
			int bestScore = Integer.MIN_VALUE;
			for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(player)) {
				gameai.makePredictionMove(tile.getIndex(), player);
				int score = minimax(board, false);
				tile.reset();
				bestScore = Math.max(score, bestScore);
			}
			return bestScore;
		}
		else {
			int bestScore = Integer.MAX_VALUE;

			for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(otherPlayer)) {
				gameai.makePredictionMove(tile.getIndex(), otherPlayer);
				int score = minimax(board, true);
				tile.reset();
				bestScore = Math.min(score, bestScore);
			}
			return bestScore;
		}
	}

}
