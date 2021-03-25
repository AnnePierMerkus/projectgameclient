package com.group4.util;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
	
	int rowCoordinate;
	int columnCoordinate;
	
	/***
	 * Make a new Tile
	 * 
	 * @param rowCoordinate - Row coordinate
	 * @param columnCoordinate - Column coordinate
	 */
	public Tile(int rowCoordinate, int columnCoordinate) {
		this.rowCoordinate = rowCoordinate;
		this.columnCoordinate = columnCoordinate;
		Rectangle border = new Rectangle(200, 200);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		setAlignment(Pos.CENTER);
		getChildren().addAll(border);

	}
	
	/***
	 * Get Row coordinate for this Tile
	 * 
	 * @return int - Row
	 */
	public int getRowCoordinate() {
		return this.rowCoordinate;
	}
	
	/***
	 * Get Column coordinate for this Tile
	 * 
	 * @return int - Column
	 */
	public int getColumnCoordinate() {
		return this.columnCoordinate;
	}
	
}
