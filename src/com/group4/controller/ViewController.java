package com.group4.controller;

import com.group4.util.Tile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;


public class ViewController {
    GameController.GameType gameType;

    GameController.Difficulty gameDifficulty;

    @FXML
    GridPane Mode;

    @FXML
    GridPane difficulty;

    @FXML
    ToggleButton tic_tac_toe;

    @FXML
    ToggleButton reversi;

    @FXML
    ToggleButton start;

    @FXML
    ToggleGroup ModeGroup;

    @FXML
    ToggleGroup DifficultyGroup;

    public void initialize()
    {
        start.setDisable(true);
    }

    @FXML
    protected void quit(ActionEvent event)
    {
        Platform.exit();
    }

    /**
     * Calls selectGame.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void tic_tac_toe(ActionEvent event) {
        selectGame(GameController.GameType.TICTACTOE);
    }

    /**
     * Calls selectGame.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void reversi(ActionEvent event) {
        selectGame(GameController.GameType.REVERSI);
    }

    /**
     * Sets gameType to the selected game and turns UI elements on/off.
     * @param gameType The selected game.
     */
    private void selectGame(GameController.GameType gameType)
    {
        this.gameType = gameType;
        Mode.setVisible(true);
        difficulty.setVisible(false);
        ModeGroup.selectToggle(null);
        DifficultyGroup.selectToggle(null);
        start.setDisable(true);
        start.setSelected(false);
    }

    /**
     * Set mode to local.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void local(ActionEvent event) {
        difficulty.setVisible(true);
        start.setDisable(true);
        start.setSelected(false);
    }

    /**
     * Set mode to multiplayer online.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void online(ActionEvent event) {
        difficulty.setVisible(false);
        DifficultyGroup.selectToggle(null);
        start.setDisable(false);
        start.setSelected(false);
        // Set multiplayer stuff.
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

    @FXML void start(ActionEvent event) {
        // Start game with correct gametype/difficult/multiplayer
        Stage stage = (Stage) start.getScene().getWindow();
        System.out.println(stage);
        stage.setScene(new Scene(fillInBoard()));
    }

    public Pane fillInBoard() {
        Pane root = new Pane();
        GameController gameController = new GameController(false);
        gameController.createGame(GameController.Difficulty.EASY, GameController.GameType.TICTACTOE);
        root.setPrefSize(600, 600);
        for(Tile tile : gameController.getGameOptions().getBoard().getGameBoard()) {
            root.getChildren().add(tile);
        }
        return root;
    }
}
