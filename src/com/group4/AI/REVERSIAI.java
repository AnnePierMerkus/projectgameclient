package com.group4.AI;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/***
 * @author AnnePierMerkus
 */
public class REVERSIAI extends AI {

    Player player;
    Player otherPlayer;
    double bestScore = Double.MIN_VALUE;
    Tile move = null;
    Tile makeMove = null;
    int i = 0;

    private static final double INITIAL_MODIFIER = 6.0;        // How large is the value modifier initially
    private static final double END_GAME_MODIFIER = 3.7;    // How much value will it have lost at the end of the game

    @Override
    public Tile makeMove(List<Tile> availableOptions) {
        gameai.updateFromGame();

        this.bestScore = Double.MIN_VALUE;
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
     * Make a move for the Ai and then start minimax to find the best move.
     *
     * @return the best move for the Ai.
     */
    public Tile bestMove() {
        List<Tile> options = this.gameai.getGame().getGameProperty().getAvailableOptions(player, 0);
        options.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        i = 0;
        for (Tile tile : options) {
            executorService.execute(() -> {
                if (i < options.size() && i > 0) this.gameai.copyThread(i - 1, i);

                //if (i + 1 < options.size()) this.gameai.copyThread(i, i + 1);
                this.gameai.makePredictionMove(tile.getIndex(), player, i);
                double score = minimax(this.gameai.getBoard(), false, 6, Double.MIN_VALUE, Double.MAX_VALUE, i);

                this.gameai.getBoard().revert(i, 1);
                if (score > bestScore) {
                    bestScore = score;
                    move = tile;
                }
                i++;
            });

        }

        executorService.shutdown();
        while (true) {
            try {
                if (!!executorService.awaitTermination(24L, TimeUnit.HOURS)) break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (move == null && options.size() > 0) {
            move = options.get(0);
        }
        System.out.println(f);
        return move;
    }

    /**
     * @param board
     * @param maximizing
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    int f;

    public double minimax(Board board, boolean maximizing, int depth, double alpha, double beta, int threadId) {
        if (depth == 0 || gameai.getGameProperty().gameHasEnded(threadId)) {
            f++;
            return evaluateGame(board, threadId);
        }

        if (maximizing) {
            double score = Double.MIN_VALUE;
            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(player, threadId)) {
                gameai.makePredictionMove(tile.getIndex(), player, threadId);
                score = Math.max(score, minimax(board, false, depth - 1, alpha, beta, threadId));
                this.gameai.getBoard().revert(threadId, 1);
                alpha = Math.max(score, alpha);
                if (alpha >= beta)
                    break;
            }
            return score;
        } else {
            Double score = Double.MAX_VALUE;
            ;
            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(otherPlayer, threadId)) {
                gameai.makePredictionMove(tile.getIndex(), otherPlayer, threadId);
                score = Math.min(score, minimax(board, true, depth - 1, alpha, beta, threadId));
                this.gameai.getBoard().revert(threadId, 1);
                beta = Math.min(score, beta);

                if (beta <= alpha)
                    break;
            }
            return score;
        }
    }

    /**
     * Determine who is likely winning
     *
     * @return
     */
    public double evaluateGame(Board board, int threadId) {

        double playerOneScore = 0;
        double playerTwoScore = 0;

        int stones = board.getMoveCounter(threadId);
        if (stones < 1) stones = 1;

        double modifierRegression = INITIAL_MODIFIER - END_GAME_MODIFIER;    // How much value will it have lost at the end of the game
        double modifier = INITIAL_MODIFIER - modifierRegression * (stones / 60.0);

        for (Tile tile : board.getGameBoard(threadId).values()) {
            if (tile.getOccupant() == null) continue;
            double value = 1;
            int pos = tile.getIndex();
            if (pos % 8 == 0 || pos % 8 == 7) value *= modifier;
            else if (pos % 8 == 1 || pos % 8 == 6) value /= modifier;

            if (pos < 8 || pos > 55) value *= modifier;
            else if (pos < 16 || pos >= 48) value /= modifier;

            if (tile.getOccupant() == otherPlayer) {
                playerOneScore += value;
            } else if (tile.getOccupant() == player) {
                playerTwoScore += value;
            }

        }

        double sum = playerOneScore + playerTwoScore;
        double result = 0 + (playerTwoScore / sum);
        return result;
    }
}
