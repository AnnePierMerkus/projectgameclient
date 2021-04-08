package com.group4.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.group4.util.observers.Observable;
import com.group4.util.observers.Observer;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane implements Observable {
	
	private int index;

	private int weight = 0;

	private Player playerOnTile = null;
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	/***
	 * Make a new Tile
	 * 
	 * @param index
	 * @author GRTerpstra
	 */
	public Tile(int index, int weight) {
		this.index = index;
		if (weight == 0)
			this.weight = weight;
		Rectangle border = new Rectangle(100, 100);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		setAlignment(Pos.CENTER);
		getChildren().addAll(border);

		setOnMouseClicked(mouseEvent ->
		{
			/*PlayerList.players.values().forEach((p) -> {
				if (p.getPlayerState() == Player.PlayerState.PLAYING_HAS_TURN) {
					p.makeMove(this);
				}
			});*/
			Iterator it = PlayerList.players.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry pair = (Map.Entry)it.next();
				if (((Player)pair.getValue()).getPlayerState() == Player.PlayerState.PLAYING_HAS_TURN)
				{
					((Player) pair.getValue()).makeMove(this);
					break;
				}
			}
		});
	}

	public Tile(int index) {
		this.index = index;
		this.weight = weight;
		Rectangle border = new Rectangle(100, 100);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		setAlignment(Pos.CENTER);
		getChildren().addAll(border);

		setOnMouseClicked(mouseEvent ->
		{
			PlayerList.players.values().forEach((p) -> p.makeMove(this));
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
			String color = (this.playerOnTile.getId().equals("p1")) ? "blue" : "red";
			setStyle("-fx-background-color: " + color);
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
		this.notifyObservers();
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
