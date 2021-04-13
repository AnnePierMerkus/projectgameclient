package com.group4.AI;

import com.group4.model.Board;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.TileUI;

import java.util.List;
import java.util.Random;

/***
 * @author AnnePierMerkus
 */
public class REVERSIAI extends AI {

    Player player;
    Player otherPlayer;
    double bestScore = Double.MIN_VALUE;
    TileUI move = null;
    TileUI makeMove = null;

    private static final double INITIAL_MODIFIER = 6.0;        // How large is the value modifier initially
    private static final double END_GAME_MODIFIER = 3.7;    // How much value will it have lost at the end of the game

    @Override
    public TileUI makeMove(List<TileUI> availableOptions) {
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
    public TileUI bestMove() {
        List<TileUI> options = this.gameai.getGame().getGameProperty().getAvailableOptions(player);
        options.sort((t1, t2) -> Integer.compare(t2.getWeight(), t1.getWeight()));
        for (TileUI tileUI : options) {
            Board backup = this.gameai.getBoard().clone();
            this.gameai.makePredictionMove(tileUI.getIndex(), player, backup);

            double score = minimax(backup, false, this.depth, Double.MIN_VALUE, Double.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                move = tileUI;
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
    public double minimax(Board board, boolean maximizing, int depth, double alpha, double beta) {
        if (depth == 0 || gameai.getGameProperty().gameHasEnded()) {
            return evaluateGame(board);
        }

        if (maximizing) {
            double score = Double.MIN_VALUE;
            for (TileUI tileUI : board.getAvailableOptions(player)) {
                Board backup = board.clone();
                gameai.makePredictionMove(tileUI.getIndex(), player, backup);
                score = Math.max(score, minimax(backup, false, depth - 1, alpha, beta));
                alpha = Math.max(score, alpha);
                if (alpha >= beta)
                    break;
            }
            return score;
        } else {
            Double score = Double.MAX_VALUE;
            ;
            for (TileUI tileUI : board.getAvailableOptions(otherPlayer)) {
                Board backup = board.clone();
                gameai.makePredictionMove(tileUI.getIndex(), otherPlayer, backup);
                score = Math.min(score, minimax(backup, true, depth - 1, alpha, beta));
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

        double playerOneScore = 0;
        double playerTwoScore = 0;

        int stones = board.getMoveCounter();
        if (stones < 1) stones = 1;

        double modifierRegression = INITIAL_MODIFIER - END_GAME_MODIFIER;    // How much value will it have lost at the end of the game
        double modifier = INITIAL_MODIFIER - modifierRegression * (stones / 60.0);

        for (TileUI tileUI : board.getGameBoard().values()) {
            if (tileUI.getOccupant() == null) continue;
            double value = 1;
            int pos = tileUI.getIndex();
            if (pos % 8 == 0 || pos % 8 == 7) value *= modifier;
            else if (pos % 8 == 1 || pos % 8 == 6) value /= modifier;

            if (pos < 8 || pos > 55) value *= modifier;
            else if (pos < 16 || pos >= 48) value /= modifier;

            if (tileUI.getOccupant() == otherPlayer) {
                playerOneScore += value;
            } else if (tileUI.getOccupant() == player) {
                playerTwoScore += value;
            }

        }

        double sum = playerOneScore + playerTwoScore;
        double result = 0 + (playerTwoScore / sum);
        return result;

        /*
        int one = countPlayerStones(OthelloGame.PLAYER_ONE);
        int two = countPlayerStones(OthelloGame.PLAYER_TWO);
        double sum = one + two;
        return OthelloGame.PLAYER_ONE + (one / sum);
        */
    }
}
