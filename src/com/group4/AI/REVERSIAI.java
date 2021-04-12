package com.group4.AI;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;
import java.util.List;
import java.util.function.Consumer;

/***
 * @author AnnePierMerkus
 */
public class REVERSIAI extends AI {

    Player player;
    Player otherPlayer;

    @Override
    public Tile makeMove(List<Tile> availableOptions)
    {
        gameai.updateFromGame();
        this.bestScore = Integer.MIN_VALUE;
        this.move = null;
        player = PlayerList.getPlayer(gameai.getPlayerTurn());
        otherPlayer = PlayerList.getOtherPlayer(gameai.getPlayerTurn());
        //return availableOptions.get(0);
        return bestMove();

        // random AI
        //availableOptions.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));
        //System.out.println(availableOptions.get(0).getWeight());
        //return availableOptions.get(0);
    }

    int bestScore = Integer.MIN_VALUE;
    Tile move = null;
    public Tile bestMove()
    {

        //this.gameai.makePredictionMove(this.gameai.getGameProperty().getAvailableOptions(player).get(0).getIndex(), player);
        //System.out.println(this.gameai.getGameProperty().getAvailableOptions(otherPlayer).size());
        //int score = minimax(this.gameai.getBoard(), false, 2);
       //this.gameai.getGame().getGameProperty().getAvailableOptions(player).parallelStream().forEach(tile -> {
        for (Tile tile : this.gameai.getGame().getGameProperty().getAvailableOptions(player)) {
           this.gameai.makePredictionMove(tile.getIndex(), player);

           int score = minimax(this.gameai.getBoard(), false, 6, Integer.MIN_VALUE, Integer.MAX_VALUE);
           System.out.println("dddd");

           this.gameai.getBoard().revert(1);
           if (score > bestScore) {
               bestScore = score;
               move = tile;
           }
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
                score = minimax(board, false, depth - 1, alpha, beta);
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
                score = minimax(board, true, depth - 1, alpha, beta);
                this.gameai.getBoard().revert(1);
                beta = Math.min(score, beta);

                if (beta <= alpha)
                    break;
            }
            return score;
        }
    }

}
