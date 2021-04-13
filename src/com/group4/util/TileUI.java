package com.group4.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.group4.controller.GameController.GameType;
import com.group4.model.Tile;
import com.group4.util.observers.Observable;
import com.group4.util.observers.Observer;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * @author GRTerpstra & Anne Pier Merkus
 */
public class TileUI extends StackPane implements Observable {
	/**
	 * Index of the tile on the board.
	 */
	private int index;

	/**
	 * Weight of this tile depending on the position on the board.
	 */
	private int weight = 0;

	/**
	 * Player occupying this tile.
	 */
	private Player playerOnTile = null;

	/**
	 * List of observers to call when the occupant of the tile changes.
	 */
	private ArrayList<Observer> observers = new ArrayList<Observer>();

	/**
	 * Circle in black or white on the Reversi board.
	 */
	Circle circle;

	/**
	 * X/O on a tile when playing Tic_tac_toe
	 */
	Text text;

	
	/***
	 * Make a new Tile and set the relevant values.
	 * 
	 * @param index index of this tile.
	 * @param weight weight for this tile
	 * @author GRTerpstra & Anne Pier Merkus
	 */
	public TileUI(int index, int weight) {
		this.index = index;
		if (this.weight == 0)
			this.weight = weight;

		Rectangle border = new Rectangle(60, 60);

		circle = new Circle(0, 0, 25);
		circle.setFill(Color.web("#009067"));
		setStyle("-fx-background-color: #009067");
		text = new Text();
		text.setStyle("-fx-font: 50 arial; -fx-font-weight: bold;");

		getChildren().addAll(circle, text);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		setAlignment(Pos.CENTER);
		getChildren().addAll(border);
		setOnMouseClicked(mouseEvent ->
		{
			Iterator<Entry<String, Player>> it = PlayerList.players.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<String, Player> pair = it.next();
				if (pair.getValue().getPlayerState() == Player.PlayerState.PLAYING_HAS_TURN)
				{
					pair.getValue().makeMove(this);
					break;
				}
			}
		});
	}
	
	/***
	 * Get Index for this Tile
	 * 
	 * @return int - Index
	 * @author GRTerpstra
	 */
	public int getIndex() {
		return this.index;
	}

	/***
	 * Get weight for this Tile
	 *
	 * @return int - weight
	 * @author AnnePierMerkus
	 */
	public int getWeight() {
		return this.weight;
	}

	/***
	 * Get the Player on this Tile
	 * Returns null if there is no player
	 * 
	 * @return Player - The player on this tile
	 * @author mobieljoy12
	 */
	public Player getOccupant() {
		return this.playerOnTile;
	}
	
	/***
	 * Set the Player on this Tile
	 * 
	 * @param occupant - Player to set on this Tile
	 * @author mobieljoy12
	 */
	public void setOccupant(Player occupant) {
		this.playerOnTile = occupant;
		if(occupant != null && (occupant.gameProperty != null && occupant.gameProperty.getGameType() == GameType.TICTACTOE)) {
			text.setText(this.playerOnTile.getId().equals("p1") ? "X" : "0");
			if(this.playerOnTile.getId().equals("p1")) {
				text.setFill(Color.WHITE);
			}
		}
		else if(occupant != null) {
			circle.setFill(this.playerOnTile.getId().equals("p1") ? Color.WHITE : Color.BLACK);
		}
		this.notifyObservers();
	}
	
	/***
	 * Make this tile unoccupied
	 * 
	 * @author mobieljoy12
	 */
	public void reset() {
		this.playerOnTile = null;
	}
	
	/***
	 * Check if the Tile is occupied
	 * 
	 * @return boolean - Occupied
	 * @author mobieljoy12
	 */
	public boolean isOccupied() {
		return (this.playerOnTile != null);
	}

	/**
	 * Register observers for this Tile.
	 * @param observer Observer to be added.
	 */
	@Override
	public void registerObserver(Observer observer) {
		this.observers.add(observer);
	}

	/**
	 * Remove observers from this Tile.
	 * @param observer Observer to be removed.
	 */
	@Override
	public void removeObserver(Observer observer) {
		this.observers.remove(observer);
	}

	/**
	 * Notifying all the observers when the occupant changes.
	 */
	@Override
	public void notifyObservers() {
		this.observers.forEach((o) -> o.update(this));
	}
	
}
