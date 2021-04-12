package com.group4.AI;

import java.util.List;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

public class TICTACTOEAI extends AI {

	Player player;
	Player otherPlayer;

	@Override
	public Tile makeMove(List<Tile> availableOptions)
	{
		gameai.updateFromGame();

		player = PlayerList.getPlayer(gameai.getPlayerTurn());
		otherPlayer = PlayerList.getOtherPlayer(gameai.getPlayerTurn());

		int bestScore = Integer.MIN_VALUE;
		Tile move = null;

		return move;
	}

	public int minimax(Board board, boolean maximizing)
	{
		if (gameai.getGameProperty().gameHasEnded()) {
			//return Tictactoewinner
			//return board.getScore(player);
		}

		if (maximizing) {
			int bestScore = Integer.MIN_VALUE;
			for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(player)) {
				gameai.makePredictionMove(tile.getIndex(), player);
				int score = minimax(board, false);
				this.gameai.getBoard().revert(1);
				bestScore = Math.max(score, bestScore);
			}
			return bestScore;
		}
		else {
			int bestScore = Integer.MAX_VALUE;;
			for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(otherPlayer)) {
				gameai.makePredictionMove(tile.getIndex(), otherPlayer);
				int score = minimax(board, true);
				this.gameai.getBoard().revert(1);
				bestScore = Math.min(score, bestScore);
			}
			return bestScore;
		}
	}

}
