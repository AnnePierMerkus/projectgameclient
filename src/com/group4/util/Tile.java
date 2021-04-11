package com.group4.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.group4.util.observers.Observable;
import com.group4.util.observers.Observer;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane implements Observable {
	
	private int index;

	private int weight = 0;

	private Player playerOnTile = null;
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();

	Circle circle;
	
	/***
	 * Make a new Tile
	 * 
	 * @param index
	 * @author GRTerpstra
	 */
	public Tile(int index, int weight) {
		this.index = index;
		if (this.weight == 0)
			this.weight = weight;

		Rectangle border = new Rectangle(75, 75);

		circle = new Circle(0, 0, 30);
		circle.setFill(Color.web("#009067"));
		setStyle("-fx-background-color: #009067");

		getChildren().add(circle);
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
		if(occupant != null) {
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

	@Override
	public void registerObserver(Observer observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		this.observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		this.observers.forEach((o) -> o.update(this));
	}
	
}
