package com.group4.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.group4.controller.GameController.GameType;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;
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
public class TileUI extends StackPane {
    /**
     * Tile occupying this UI view.
     */
    Tile tile;

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
     * @author GRTerpstra & Anne Pier Merkus
     */
    public TileUI() {

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
                    pair.getValue().makeMove(tile, 0);
                    break;
                }
            }
        });
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /***
     * Set the Player on this Tile
     *
     * @param occupant - Player to set on this Tile
     * @author mobieljoy12
     */
    public void setOccupant(Player occupant, int threadId) {

        tile.setOccupant(occupant, threadId);
        if(occupant != null && (occupant.gameProperty != null && occupant.gameProperty.getGameType() == GameType.TICTACTOE)) {
            text.setText(tile.getPlayerOnTile().getId().equals("p1") ? "X" : "0");
            if(tile.getPlayerOnTile().getId().equals("p1")) {
                text.setFill(Color.WHITE);
            }
        }
        else if(occupant != null) {
            circle.setFill(occupant.getId().equals("p1") ? Color.WHITE : Color.BLACK);
        }
        tile.updateFilledTiles(threadId);
    }

}