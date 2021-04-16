package com.group4.AI;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/***
 * @author AnnePierMerkus
 */
public class REVERSIAI extends AI {

    Player AI;
    Player opponent;
    double bestScore = Double.MIN_VALUE;
    Tile move = null;

    private static final double EARLY_GAME_MODIFIER = 6.0;
    private static final double LATE_GAME_MODIFIER = 3.7;

    @Override
    public Tile makeMove(List<Tile> availableOptions) {
        gameai.updateFromGame();

        this.bestScore = Double.MIN_VALUE;
        this.move = null;
        AI = PlayerList.getPlayer(gameai.getGame().getPlayerTurn());
        opponent = PlayerList.getOtherPlayer(gameai.getGame().getPlayerTurn());
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
        List<Tile> options = this.gameai.getGame().getGameProperty().getAvailableOptions(AI);
        options.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));

        ExecutorService executorService = Executors.newFixedThreadPool(options.size());

        for (Tile tile : options) {
            executorService.execute(() -> {

                Board board = this.gameai.getBoard().clone();
                this.gameai.makePredictionMove(tile.getIndex(), AI, board);

                double score = minimax(board, false, this.depth, Double.MIN_VALUE, Double.MAX_VALUE);
                //this.gameai.getBoard().revert(1);
                board.revert(1);
                if (score > bestScore) {
                    bestScore = score;
                    move = tile;
                }
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

    public void revert(Board board) {
        for (Tile tile : board.getPreviousBoard().get(board.getMoveCounter()).values()) {
            if (tile.isOccupied()) {
                board.addFilledTile(tile.getOccupant(), board.getGameBoard().get(tile.getIndex()));
            } else {
                board.getFilledTiles().get(board.getGameBoard().get(tile.getIndex()).getOccupant().getId()).remove(tile.getIndex());
            }
            board.getGameBoard().get(tile.getIndex()).setOccupant(tile.getOccupant());
        }
        board.getPreviousBoard().remove(board.getMoveCounter());

    }

    public double minimax(Board board, boolean maximizing, int depth, double alpha, double beta) {
        if (depth == 0 || gameai.getGameProperty().gameHasEnded()) {

            return evaluateGame(board);
        }

        if (maximizing) {
            double score = Double.MIN_VALUE;

            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(AI, board)) {
                gameai.makePredictionMove(tile.getIndex(), AI, board);
                score = Math.max(score, minimax(board, false, depth - 1, alpha, beta));
                board.revert(1);
                alpha = Math.max(score, alpha);
                if (alpha >= beta)
                    break;
            }
            return score;
        } else {
            Double score = Double.MAX_VALUE;
            for (Tile tile : this.gameai.getGameProperty().getAvailableOptions(opponent, board)) {
                gameai.makePredictionMove(tile.getIndex(), opponent, board);
                score = Math.min(score, minimax(board, true, depth - 1, alpha, beta));
                board.revert(1);
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
    public double evaluateGame(Board board) {

        double opponentScore = 0;
        double scoreAI = 0;

        int moves = board.getMoveCounter();
        if (moves < 1) moves = 1;

        double regression = EARLY_GAME_MODIFIER - LATE_GAME_MODIFIER;
        double scoreModifier = EARLY_GAME_MODIFIER - regression * (moves / 60.0);

        for (Tile tile : board.getGameBoard().values()) {
            if (tile.getOccupant() == null) continue;
            double score = 1;
            int pos = tile.getIndex();
            if (pos % 8 == 0 || pos % 8 == 7) score *= scoreModifier;
            else if (pos % 8 == 1 || pos % 8 == 6) score /= scoreModifier;

            if (pos < 8 || pos > 55) score *= scoreModifier;
            else if (pos < 16 || pos >= 48) score /= scoreModifier;

            if (tile.getOccupant() == opponent) {
                opponentScore += score;
            } else if (tile.getOccupant() == AI) {
                scoreAI += score;
            }

        }

        double sum = opponentScore + scoreAI;
        double result = 0 + (scoreAI / sum);
        return result;
    }
}
