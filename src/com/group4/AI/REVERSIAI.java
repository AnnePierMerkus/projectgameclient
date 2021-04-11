package com.group4.AI;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;
import org.w3c.dom.ls.LSOutput;

import java.util.HashMap;
import java.util.List;

/***
 * @author AnnePierMerkus
 */
public class REVERSIAI extends AI {
	
    @Override
    public Tile makeMove(List<Tile> availableOptions)
    {
        gameai.updateFromGame();
        return bestMove(availableOptions, gameai.getBoard());
        //availableOptions.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));
        //System.out.println(availableOptions.get(0).getWeight());
        //return availableOptions.get(0);
    }


    public Tile bestMove(List<Tile> availableOptions, Board board)
    {
        int bestScore = Integer.MIN_VALUE;
        int move = 0;
        for (int i = 0; i < availableOptions.size(); i++) {
            // board = Do move availableOptions.get(i);
            int score = minimax(board, true, 1);

            if (score > bestScore) {
                bestScore = score;
                move = i;
            }
        }

        return availableOptions.get(move);
    }

    int f = 0;
    public int minimax(Board board, boolean maximizing, int depth)
    {
        System.out.println(board);
        System.out.println(this.gameai.getBoard());
        Player player = PlayerList.getOtherPlayer(gameai.getPlayerTurn());
        Player otherPlayer = PlayerList.getOtherPlayer(gameai.getPlayerTurn());

        if ( depth == 0 || board.isFull()) {
            return board.getScore(player);
        }

        if (maximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (Tile tile : this.gameai.getGame().getAvailableOptions(player)) {
                // board = do move getAvailableOptions.get(i);
                gameai.makePredictionMove(tile.getIndex(), player);
                int score = minimax(board, false, depth - 1);
                bestScore = Math.max(score, bestScore);
            }
            return bestScore;
        }
        else {
            int bestScore = Integer.MAX_VALUE;

            for (Tile tile : this.gameai.getGame().getAvailableOptions(otherPlayer)) {
                // board = do move getAvailableOptions.get(i);
                gameai.makePredictionMove(tile.getIndex(), otherPlayer);
                int score = minimax(board, true, depth - 1);
                bestScore = Math.min(score, bestScore);
            }
            return bestScore;
        }
    }
    
}
