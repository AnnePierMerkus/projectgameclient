package com.group4.AI;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

import java.util.List;
import java.util.Random;

public class REVERSIAI extends AI {

	// The player the AI controls
    Player player;
    
    // The player not controller by the AI
    Player otherPlayer;
    
    // The maximun score possible
    int bestScore = Integer.MIN_VALUE;
    
    // The move just made
    Tile move = null;
    
    // The move to make
    Tile makeMove = null;

    @Override
    public Tile makeMove(List<Tile> availableOptions)
    {
        gameai.updateFromGame();

        this.bestScore = Integer.MIN_VALUE;
        this.move = null;
        player = PlayerList.getPlayer(gameai.getGame().getPlayerTurn());
        otherPlayer = PlayerList.getOtherPlayer(gameai.getGame().getPlayerTurn());
        //return availableOptions.get(0);
        if (this.depth == 0) {
            Random random = new Random();
            int rand = random.nextInt(availableOptions.size());
            return availableOptions.get(rand);
        } else if (this.depth <= 2) {
            availableOptions.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));
            return availableOptions.get(0);
        } else {
            return bestMove();
        }
    }

    /**
     * Make a move for the AI and then start Minimax to find the best move.
     * 
     * @return the best move for the AI.
     * @author AnnePierMerkus
     */
    public Tile bestMove()
    {
    	List<Tile> options = this.gameai.getGame().getGameProperty().getAvailableOptions(player);

        for (Tile tile : options) {
            if (tile.getWeight() == 20) {
                return tile;
            }
        }

        for (Tile tile : options) {
           this.gameai.makePredictionMove(tile.getIndex(), player);

           int score = minimax(this.gameai.getBoard(), false, this.depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
           this.gameai.getBoard().revert(1);
           if (score > bestScore) {
               bestScore = score;
               move = tile;
           }
       }

        if (move == null && options.size() > 0)
        {
            move = options.get(0);
        }
        return move;
    }

    /**
     * The Minimax AI that will decide the best move to make
     *
     * @param board - The board to check the moves on
     * @param maximizing - Whether to get the min or max value
     * @param depth - Amount of moves the AI looks ahead
     * @param alpha - Alpha beta pruning
     * @param beta - Alpha beta pruning
     * @return amount - The amount of tiles the AI has at the end of the loop
     * @author AnnePierMerkus
     */
    public int minimax(Board board, boolean maximizing, int depth, int alpha, int beta)
    {
        if (depth == 0 || gameai.getGameProperty().gameHasEnded()) {
            return board.getScore(player);
        }

        if (maximizing) {
            int score = Integer.MIN_VALUE;
            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(player)) {
                gameai.makePredictionMove(tile.getIndex(), player);
                score = Math.max(score, minimax(board, false, depth - 1, alpha, beta));
                this.gameai.getBoard().revert(1);
                alpha = Math.max(score, alpha);
                if (alpha >= beta)
                    break;
            }
            return score;
        }
        else {
            int score = Integer.MAX_VALUE;;
            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(otherPlayer)) {
                gameai.makePredictionMove(tile.getIndex(), otherPlayer);
                score = Math.min(score, minimax(board, true, depth - 1, alpha, beta));
                this.gameai.getBoard().revert(1);
                beta = Math.min(score, beta);

                if (beta <= alpha)
                    break;
            }
            return score;
        }
    }

}
