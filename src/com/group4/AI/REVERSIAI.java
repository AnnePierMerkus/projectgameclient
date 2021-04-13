package com.group4.AI;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

import java.util.List;

/***
 * @author AnnePierMerkus
 */
public class REVERSIAI extends AI {

    Player player;
    Player otherPlayer;
    int bestScore = Integer.MIN_VALUE;
    Tile move = null;
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
        Tile bestMove = bestMove();
        
        return bestMove;

        //availableOptions.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));
        //System.out.println(availableOptions.get(0).getWeight());
        //return availableOptions.get(0);
    }

    public Tile bestMove()
    {
        //this.gameai.makePredictionMove(this.gameai.getGameProperty().getAvailableOptions(player).get(0).getIndex(), player);
        //System.out.println(this.gameai.getGameProperty().getAvailableOptions(otherPlayer).size());
        //int score = minimax(this.gameai.getBoard(), false, 2);
       //this.gameai.getGame().getGameProperty().getAvailableOptions(player).parallelStream().forEach(tile -> {
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

    int f = 0;
    int loop1 = 0;
    int loop2 = 0;
    public int minimax(Board board, boolean maximizing, int depth, int alpha, int beta)
    {
        f++;
        if (depth == 0 || gameai.getGameProperty().gameHasEnded()) {
            return board.getScore(player);
        }

        if (maximizing) {
            int score = Integer.MIN_VALUE;
            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(player)) {
                loop1++;
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
                loop2++;
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
