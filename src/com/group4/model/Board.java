package com.group4.model;

import java.util.HashMap;

import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;
import com.group4.util.observers.TileObserver;

/**
 * Class that creates and defines the game board.
 *
 * @author mobieljoy12
 */
public class Board {

    // threadId -> (tileId, Tile) ~ GameBoard
    private HashMap<Integer, HashMap<Integer, Tile>> gameBoard = new HashMap<Integer, HashMap<Integer, Tile>>();

    // threadId -> (playerId -> (tileId, Tile)) ~ FilledTiles
    private HashMap<Integer, HashMap<String, HashMap<Integer, Tile>>> filledTiles = new HashMap<Integer, HashMap<String, HashMap<Integer, Tile>>>();

    // threadId -> (moveCount -> (tileId, Tile)) ~ PreviousBoard
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Tile>>> previousBoard = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Tile>>>();

    // (threadId, moveCounter) ~ MoveCounter
    private HashMap<Integer, Integer> moveCounter = new HashMap<Integer, Integer>();
    private int height;
    private int width;

    /***
     * Decrement the move counter
     *
     * @param threadId - The thread to decrease the movecounter for
     * @author mobieljoy12
     */
    private void decMoveCounter(int threadId) {
        if (!this.moveCounter.containsKey(threadId)) this.moveCounter.put(threadId, 0);
        if (this.moveCounter.get(threadId) > 0) this.moveCounter.put(threadId, this.moveCounter.get(threadId) - 1);
    }

    /**
     * Method that creates a new board which size is defined by the given height and width.
     *
     * @param height the number of rows the board has to contain.
     * @param width  the number of columns the board has to contain.
     * @author GRTerpstra
     */
    public Board(int height, int width) {
        this.height = height;
        this.width = width;

        // New filledTiles list for thread 0
        this.filledTiles.put(0, new HashMap<String, HashMap<Integer, Tile>>());

        // Fill the players with an empty list
        this.filledTiles.get(0).put("p1", new HashMap<Integer, Tile>());
        this.filledTiles.get(0).put("p1", new HashMap<Integer, Tile>());

        // The tileObserver all Tiles will use
        TileObserver tileObserver = new TileObserver(this);

        // Initiate the board list in thread 0
        this.gameBoard.put(0, new HashMap<Integer, Tile>());

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // ((row * getRowWidth()) + column)
                int weight = 0;
                if ((row == 0 || row == height - 1) && (col == 0 || col == width - 1)) {
                    weight = 20;
                } else if ((row == 0 || row == 1 || row == height - 1 || row == height - 2) && (col == 0 || col == 1 || col == width - 1 || col == width - 2)) {
                    weight = 1;
                } else if (row == 0 || row == height - 1 || col == 0 || col == width - 1) {
                    weight = 10;
                } else if (row == 1 || row == height - 2 || col == 1 || col == width - 2) {
                    weight = 2;
                } else {
                    weight = 5;
                }

                Tile tile = new Tile((row * this.width) + col, weight);
                tile.registerObserver(tileObserver);
                // Add the tiles to thread 0
                this.gameBoard.get(0).put(tile.getIndex(), tile);
            }
        }
    }

    /***
     * Get the current filledTiles HashMap
     *
     * @param threadId - The thread to get the filled tiles for
     * @return HashMap<String, HashMap < Integer, Tile>> - filledTiles
     * @author mobieljoy12
     */
    public HashMap<String, HashMap<Integer, Tile>> getFilledTiles(int threadId) {
        return this.filledTiles.get(threadId);
    }

    /***
     * Get the current previousBoard HashMap
     *
     * @param threadId - The thread to get the previous board for
     * @return HashMap<Integer, HashMap < Integer, Tile>> - previousBoard
     * @author mobieljoy12
     */
    public HashMap<Integer, HashMap<Integer, Tile>> getPreviousBoard(int threadId) {
        return this.previousBoard.get(threadId);
    }

    /***
     * Set the moveCounter to a given value
     *
     * @param threadId - The thread to set the movecounter for
     * @param newCount - New value
     * @author mobieljoy12
     */
    public void setMoveCounter(int threadId, int newCount) {
        System.out.println("setcounter" + threadId);
        this.moveCounter.put(threadId, newCount);
    }

    /**
     * Method that clears all settings of the board.
     *
     * @param threadId - The thread to reset the board for
     * @author GRTerpstra & mobieljoy12
     */
    public void reset(int threadId) {
        this.gameBoard.put(threadId, new HashMap<Integer, Tile>());
    }

    /***
     * Get the current move counter
     * Move 0 is the initial board
     *
     * @param threadId - The thread to get the movecounter for
     * @return int - Current move counter
     * @author mobieljoy12
     */
    public int getMoveCounter(int threadId) {
        if (!this.moveCounter.containsKey(threadId)) {
            this.moveCounter.put(threadId, 0);
            return 0;
        }
        return this.moveCounter.get(threadId);
    }

    /***
     * Increment the move counter
     *
     * @param threadId - The thread to increment the movecounter for
     * @author mobieljoy12
     */
    public void incMoveCounter(int threadId) {
        if (!this.moveCounter.containsKey(threadId)) this.moveCounter.put(threadId, 1);
        else this.moveCounter.put(threadId, this.moveCounter.get(threadId) + 1);
    }

    /***
     * Empty all the previous board values
     *
     * @param threadId - The thread to empty all previous board values for
     * @author mobieljoy12
     */
    public void emptyAllPrevious(int threadId) {
        this.previousBoard.put(threadId, new HashMap<Integer, HashMap<Integer, Tile>>());
    }

    /***
     * Save a previous tile to a moveCount
     * For AI purposes
     *
     * @param threadId - The thread to save the previous tile to
     * @param moveCount - The move to save the tile to
     * @param tile - The Tile to save
     * @param player - The Player to put on the Tile
     * @author mobieljoy12
     */
    public void savePrevious(int threadId, int moveCount, Tile tile, Player player) {
        if (!this.previousBoard.containsKey(threadId))
            this.previousBoard.put(threadId, new HashMap<Integer, HashMap<Integer, Tile>>());
        if (!this.previousBoard.get(threadId).containsKey(moveCount))
            this.previousBoard.get(threadId).put(moveCount, new HashMap<Integer, Tile>());
        Tile prevTile = new Tile(tile.getIndex(), tile.getWeight());
        prevTile.setOccupant(player, threadId);

        this.previousBoard.get(threadId).get(moveCount).put(tile.getIndex(), prevTile);
    }

    /***
     * Save the current board before making a move so it can be reverted back to
     *
     * @param threadId - The thread to save the previous tile to
     * @param tile - The Tile to save
     * @param player - The Player to put on the Tile
     * @author mobieljoy12
     */
    public void savePrevious(int threadId, Tile tile, Player player) {
        if (!this.previousBoard.containsKey(threadId))
            this.previousBoard.put(threadId, new HashMap<Integer, HashMap<Integer, Tile>>());
        if (!this.previousBoard.get(threadId).containsKey(this.getMoveCounter(threadId)))
            this.previousBoard.get(threadId).put(this.getMoveCounter(threadId), new HashMap<Integer, Tile>());
        Tile prevTile = new Tile(tile.getIndex(), tile.getWeight());
        prevTile.setOccupant(player, threadId);

        this.previousBoard.get(threadId).get(this.getMoveCounter(threadId)).put(tile.getIndex(), prevTile);
    }

    /***
     * Revert the board back to the previous move
     *
     * @param threadId - The thread to revert on
     * @param moves - Amount of moves to revert back
     * @author mobieljoy12
     */
    public void revert(int threadId, int moves) {
        for (int counter = 0; counter < moves; counter++) {
            this.decMoveCounter(threadId);
            for (Tile tile : this.previousBoard.get(threadId).get(this.getMoveCounter(threadId)).values()) {
                if (tile.isOccupied()) {
                    this.addFilledTile(threadId, tile.getOccupant(), this.gameBoard.get(threadId).get(tile.getIndex()));
                } else {
                    this.filledTiles.get(threadId).get(this.gameBoard.get(threadId).get(tile.getIndex()).getOccupant().getId()).remove(tile.getIndex());
                }
                this.gameBoard.get(threadId).get(tile.getIndex()).setOccupant(tile.getOccupant(), threadId);
            }
            this.previousBoard.get(threadId).remove(this.getMoveCounter(threadId));
        }
    }

    /**
     * Method that returns the gameboard which consists of an ArrayList of tiles.
     *
     * @param threadId - The thread to get the gameboard for
     * @return ArrayList<Tile> t the gameboard.
     * @author GRTerpstra
     */
    public HashMap<Integer, Tile> getGameBoard(int threadId) {
        return this.gameBoard.get(threadId);
    }

    /**
     * Method that returns the height aka number of rows of the board.
     *
     * @return int - the height of the board.
     * @author GRTerpstra
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Method that returns the width aka number of columns of the board.
     *
     * @return int - the width of the board.
     * @author GRTerpstra
     */
    public int getWidth() {
        return this.width;
    }

    /***
     * Reset the whole filledTiles HashMap
     *
     * @param threadId - The thread to reset the HashMap for
     * @author mobieljoy12
     */
    public void resetFilledTiles(int threadId) {
        if (!this.filledTiles.containsKey(threadId))
            this.filledTiles.put(threadId, new HashMap<String, HashMap<Integer, Tile>>());
        this.filledTiles.get(threadId).put("p1", new HashMap<Integer, Tile>());
        this.filledTiles.get(threadId).put("p2", new HashMap<Integer, Tile>());
    }

    /***
     * Add a Tile to a Player
     *
     * @param threadId - The thread to add to
     * @param player - The Player to add it to
     * @param tile - The Tile to add
     * @author mobieljoy12
     */
    public void addFilledTile(int threadId, Player player, Tile tile) {
        String otherPlayerId = PlayerList.getOtherPlayer(player.getId()).getId();

        if (!this.filledTiles.containsKey(threadId))
            this.filledTiles.put(threadId, new HashMap<String, HashMap<Integer, Tile>>());
        if (!this.filledTiles.get(threadId).containsKey(player.getId()))
            this.filledTiles.get(threadId).put(player.getId(), new HashMap<Integer, Tile>());
        if (!this.filledTiles.get(threadId).containsKey(otherPlayerId))
            this.filledTiles.get(threadId).put(otherPlayerId, new HashMap<Integer, Tile>());
        if (this.filledTiles.get(threadId).get(otherPlayerId).containsKey(tile.getIndex())) {
            this.filledTiles.get(threadId).get(otherPlayerId).remove(tile.getIndex());
        }
        this.filledTiles.get(threadId).get(player.getId()).put(tile.getIndex(), tile);
    }

    /***
     * Check whether the board is completely full
     *
     * @param threadId - The thread to check for
     * @return boolean - Full
     * @author mobieljoy12
     */
    public boolean isFull(int threadId) {
        int filledTileCount = 0;
        if (!this.filledTiles.containsKey(threadId))
            this.filledTiles.put(threadId, new HashMap<String, HashMap<Integer, Tile>>());
        for (String pId : this.filledTiles.get(threadId).keySet()) {
            filledTileCount += this.filledTiles.get(threadId).get(pId).size();
        }
        //System.out.println("Full board is: " + (this.getWidth() * this.getHeight()) + " tiles, " + filledTileCount + " are filled");

        return ((this.getWidth() * this.getHeight()) == filledTileCount);
    }

    /***
     * Get scores of all players
     *
     * @param threadId - The thread to check the scores for
     * @return HashMap<Player, Integer> - Hashmap holding scores per player
     * @author mobieljoy12
     */
    public HashMap<String, Integer> getScores(int threadId) {
        HashMap<String, Integer> tempScores = new HashMap<String, Integer>(); // New HashMap
        for (String pId : this.filledTiles.get(threadId).keySet()) {
            tempScores.put(pId, this.filledTiles.get(threadId).get(pId).size());
        }
        return tempScores;
    }

    /***
     * Get the score for a specific Player
     *
     * @param threadId - The thread to get the score for
     * @param player - The player for which you want the score
     * @return int - Score in tiles
     * @author mobieljoy12
     */
    public int getScore(int threadId, Player player) {
        if (!this.filledTiles.containsKey(threadId)) return 0;
        if (!this.filledTiles.get(threadId).containsKey(player.getId())) return 0;
        return this.filledTiles.get(threadId).get(player.getId()).size();
    }

    /***
     * Get a Tile from the gameboard
     * Returns null if index is out of bounds
     *
     * @param threadId - The thread to get the tile from
     * @param index - The index for the Tile that is requested
     * @return Tile or null
     * @author mobieljoy12
     */
    public Tile getTile(int threadId, int index) {
        if (!this.gameBoard.containsKey(threadId)) return null;
        return (this.gameBoard.get(threadId).containsKey(index)) ? this.gameBoard.get(threadId).get(index) : null;
    }

    public void createExtra(int threadFrom, int threadTo) {
        this.gameBoard.put(threadTo, (HashMap<Integer, Tile>) this.gameBoard.get(threadFrom).clone());
	}
	public void createExtraPrevious(int threadFrom, int threadTo) {
        this.previousBoard.put(threadTo, (HashMap<Integer, HashMap<Integer, Tile>> ) this.previousBoard.get(threadFrom).clone());
    }

    /***
     * Get a Tile from the gameboard
     * Returns null if index is out of bounds
     *
     * @param threadId - The thread to get the tile from
     * @param row - The row of the Tile requested
     * @param col - The column of the Tile requested
     * @return Tile or null
     * @author mobieljoy12
     */
    public Tile getTile(int threadId, int row, int col) {
        int index = ((row * this.height) + col);
        if (!this.gameBoard.containsKey(threadId)) return null;
        return (this.gameBoard.get(threadId).containsKey(index)) ? this.gameBoard.get(threadId).get(index) : null;
    }

}

