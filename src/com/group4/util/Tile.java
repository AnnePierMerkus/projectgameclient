package com.group4.util;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
	
	int rowCoordinate;
	int columnCoordinate;
	
	public Tile(int rowCoordinate, int columnCoordinate) {
		this.rowCoordinate = rowCoordinate;
		this.columnCoordinate = columnCoordinate;
		Rectangle border = new Rectangle(200, 200);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		setAlignment(Pos.CENTER);
		getChildren().addAll(border);
		
	}
	
	public int getRowCoordinate() {
		return this.rowCoordinate;
	}
	
	public int getColumnCoordinate() {
		return this.columnCoordinate;
	}
	
}
