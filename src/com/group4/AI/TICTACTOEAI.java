package com.group4.AI;

import java.util.List;
import java.util.Random;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.TileUI;

public class TICTACTOEAI extends AI {

	// The player the AI controls
    Player player;
    
    // The player not controller by the AI
    Player otherPlayer;

	@Override
	public TileUI makeMove(List<TileUI> availableOptions)
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
	public TileUI bestMove(List<TileUI> availableOptions){

		List<TileUI> options = this.gameai.getGameProperty().getAvailableOptions(player);
		int bestScore = Integer.MIN_VALUE;
		TileUI move = null;

		for (TileUI tileUI : options)
		{
			this.gameai.makePredictionMove(tileUI.getIndex(), player);
			int score = minimax(this.gameai.getBoard(), false);
			tileUI.reset();
			if (score > bestScore) {
				bestScore = score;
				move = tileUI;
			}
		}

		for (TileUI tileUI : availableOptions)
		{
			if (tileUI.getIndex() == move.getIndex())
			{
				move = tileUI;
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
			for (TileUI tileUI : this.gameai.getGameProperty().getAvailableOptions(player)) {
				gameai.makePredictionMove(tileUI.getIndex(), player);
				int score = minimax(board, false);
				tileUI.reset();
				bestScore = Math.max(score, bestScore);
			}
			return bestScore;
		}
		else {
			int bestScore = Integer.MAX_VALUE;

			for (TileUI tileUI : this.gameai.getGameProperty().getAvailableOptions(otherPlayer)) {
				gameai.makePredictionMove(tileUI.getIndex(), otherPlayer);
				int score = minimax(board, true);
				tileUI.reset();
				bestScore = Math.min(score, bestScore);
			}
			return bestScore;
		}
	}

}
