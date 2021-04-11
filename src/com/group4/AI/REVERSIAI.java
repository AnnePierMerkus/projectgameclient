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
    @Override
    public Tile makeMove(List<Tile> availableOptions)
    {
        gameai.updateFromGame();
        player = PlayerList.getPlayer(gameai.getPlayerTurn());
        otherPlayer = PlayerList.getOtherPlayer(gameai.getPlayerTurn());
        return bestMove();
        //availableOptions.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));
        //System.out.println(availableOptions.get(0).getWeight());
        //return availableOptions.get(0);
    }


    public Tile bestMove()
    {
        int bestScore = Integer.MIN_VALUE;
        Tile move = null;
        //this.gameai.makePredictionMove(this.gameai.getGameProperty().getAvailableOptions(player).get(0).getIndex(), player);
        System.out.println(this.gameai.getBoard().getScores());
        //int score = minimax(this.gameai.getBoard(), false, 2);

        for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(player)) {
            System.out.println(tile.getIndex());
            this.gameai.makePredictionMove(tile.getIndex(), player);
            System.out.println(this.gameai.getBoard().getScores());
            //int score = minimax(this.gameai.getBoard(), false, 2);
            tile.reset();

            //if (score > bestScore) {
             //   bestScore = score;
              //  move = tile;
            //}
        }
        System.out.println(f);
        System.out.println(loop1);
        System.out.println(loop2);

        return this.gameai.getGameProperty().getAvailableOptions(player).get(0);
    }

    int f = 0;
    int loop1 = 0;
    int loop2 = 0;
    public int minimax(Board board, boolean maximizing, int depth)
    {
        f++;
        if ( depth == 0 || gameai.getGameProperty().gameHasEnded()) {
            int score = board.getScore(player);
            return score;
        }

        if (maximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(player)) {
                loop1++;

                gameai.makePredictionMove(tile.getIndex(), player);
                int score = minimax(board, false, depth - 1);
                tile.reset();
                bestScore = Math.max(score, bestScore);
            }
            return bestScore;
        }
        else {
            int bestScore = Integer.MAX_VALUE;;
            System.out.println(this.gameai.getGameProperty().getAvailableOptions(otherPlayer).size());
            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(otherPlayer)) {
                loop2++;
                System.out.println(tile.getIndex());

                gameai.makePredictionMove(tile.getIndex(), otherPlayer);
                int score = minimax(board, true, depth - 1);
                tile.reset();
                bestScore = Math.min(score, bestScore);
            }
            return bestScore;
        }
    }
    
}
