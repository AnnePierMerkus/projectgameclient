package com.group4.controller;

import com.group4.util.Tile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Anne Pier Merkus
 */
public class ViewController extends Controller {
    boolean online = false;
    int depth;

    GameController.Difficulty gameDifficulty;
    GameController.GameType gameType;

    /**
     * The following variables are all declared and created in fxml files to control the views.
     */

    @FXML
    GridPane difficulty;

    @FXML
    GridPane game;

    @FXML
    ToggleButton start;

    @FXML
    ToggleGroup ModeGroup;

    @FXML
    ToggleGroup GameGroup;

    @FXML
    ToggleGroup DifficultyGroup;

    public void initialize()
    {
        start.setDisable(true);
    }

    /**
     * Quit the game.
     * @param event
     */
    @FXML
    protected void quit(ActionEvent event)
    {
        Platform.exit();
    }

    /**
     * Set mode to local.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void local(ActionEvent event) {
        game.setVisible(true);
        start.setDisable(true);
        start.setSelected(false);

        online = false;
    }

    /**
     * Set mode to multiplayer online.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void online(ActionEvent event) {
        game.setVisible(false);
        difficulty.setVisible(false);

        DifficultyGroup.selectToggle(null);
        GameGroup.selectToggle(null);

        start.setDisable(false);
        start.setSelected(false);

        online = true;
    }

    /**
     * Select tic_tac_toe game mode.
     * @param event
     */
    @FXML
    protected void tic_tac_toe(ActionEvent event)
    {
        selectGame(GameController.GameType.TICTACTOE);
    }

    /**
     * Select Reversi game mode.
     * @param event
     */
    @FXML
    protected void reversi(ActionEvent event)
    {
        selectGame(GameController.GameType.REVERSI);
    }

    /**
     * General game options preventing double code.
     * @param gameType
     */
    private void selectGame(GameController.GameType gameType)
    {
        this.gameType = gameType;
        start.setDisable(true);
        difficulty.setVisible(true);
        DifficultyGroup.selectToggle(null);
    }

    /**
     * Set difficult to easy.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void easy(ActionEvent event) {
        start.setDisable(false);
        gameDifficulty = GameController.Difficulty.EASY;
    }

    /**
     * Set difficult to medium.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void medium(ActionEvent event) {
        start.setDisable(false);
        gameDifficulty = GameController.Difficulty.MEDIUM;
    }

    /**
     * Set difficult to hard.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void hard(ActionEvent event) {
        start.setDisable(false);
        gameDifficulty = GameController.Difficulty.HARD;
    }

    /**
     * Play button is pressed and creating the game view.
     * @param event
     * @throws Exception
     */
    @FXML void start(ActionEvent event) throws Exception {
        Stage stage = (Stage) start.getScene().getWindow();

        if (online) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Connect.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 500, 500);
            scene.getStylesheets().add(getClass().getResource("../test.css").toExternalForm());

            stage.setScene(scene);
        }
        else
        {
            Scene scene = new Scene(fillInBoard(gameType, new SingleplayerGameController(gameDifficulty), false));
            stage.setScene(scene);
        }
    }
}
