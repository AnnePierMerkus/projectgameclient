package com.group4.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Anne Pier Merkus
 */
public class ViewController extends Controller {
    boolean online = false;
    boolean co_op;

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
     * 
     * @param event
     * @author Anne Pier Merkus
     */
    @FXML
    protected void quit(ActionEvent event)
    {
        Platform.exit();
    }

    /**
     * Set mode to local.
     * 
     * @param event The UI element used to call this function.
     * @author Anne Pier Merkus
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
     * 
     * @param event The UI element used to call this function.
     * @author Anne Pier Merkus
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
     * 
     * @param event
     * @author Anne Pier Merkus
     */
    @FXML
    protected void tic_tac_toe(ActionEvent event)
    {
        selectGame(GameController.GameType.TICTACTOE);
    }

    /**
     * Select Reversi game mode.
     * 
     * @param event
     * @author Anne Pier Merkus
     */
    @FXML
    protected void reversi(ActionEvent event)
    {
        selectGame(GameController.GameType.REVERSI);
    }

    /**
     * General game options preventing double code.
     * 
     * @param gameType
     * @author Anne Pier Merkus
     */
    private void selectGame(GameController.GameType gameType)
    {
        this.gameType = gameType;
        start.setDisable(true);
        difficulty.setVisible(true);
        DifficultyGroup.selectToggle(null);
    }

    /**
     * Set the game to co-op mode.
     *
     * @param event The UI element used to call this function.
     * @author Anne Pier Merkus
     */
    @FXML
    protected void coop(ActionEvent event) {
        start.setDisable(false);
        co_op = true;
    }
    /**
     * Set difficult to easy.
     * 
     * @param event The UI element used to call this function.
     * @author Anne Pier Merkus
     */
    @FXML
    protected void easy(ActionEvent event) {
        start.setDisable(false);
        gameDifficulty = GameController.Difficulty.EASY;
        co_op = false;
    }

    /**
     * Set difficult to medium.
     * 
     * @param event The UI element used to call this function.
     * @author Anne Pier Merkus
     */
    @FXML
    protected void medium(ActionEvent event) {
        start.setDisable(false);
        gameDifficulty = GameController.Difficulty.MEDIUM;
        co_op = false;
    }

    /**
     * Set difficult to hard.
     * 
     * @param event The UI element used to call this function.
     * @author Anne Pier Merkus
     */
    @FXML
    protected void hard(ActionEvent event) {
        start.setDisable(false);
        gameDifficulty = GameController.Difficulty.HARD;
        co_op = false;
    }

    /**
     * Play button is pressed and creating the game view.
     * 
     * @param event
     * @throws Exception
     * @author Anne Pier Merkus
     */
    @FXML void start(ActionEvent event) throws Exception {
        Stage stage = (Stage) start.getScene().getWindow();
        Controller.stage = stage;
        if (online) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Connect.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 500, 500);
            scene.getStylesheets().add(getClass().getResource("../test.css").toExternalForm());

            stage.setScene(scene);
        }
        else
        {
            Scene scene = new Scene(fillInBoard(gameType, new SingleplayerGameController(gameDifficulty, co_op), false));
            stage.setScene(scene);
        }
    }
}
