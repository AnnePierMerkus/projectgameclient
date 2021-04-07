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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class ViewController {
    boolean online = false;

    GameController.Difficulty gameDifficulty;

    @FXML
    GridPane difficulty;

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
     * Set mode to local.
     * @param event The UI element used to call this function.
     */
    @FXML
    protected void local(ActionEvent event) {
        difficulty.setVisible(true);
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
        difficulty.setVisible(false);
        DifficultyGroup.selectToggle(null);
        start.setDisable(false);
        start.setSelected(false);

        online = true;
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

    @FXML void start(ActionEvent event) throws Exception {
        // Start game with correct gametype/difficult/multiplayer
        Stage stage = (Stage) start.getScene().getWindow();
        if (online) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Online.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 500, 500);
            //scene.getStylesheets().add(getClass().getResource("test.css").toExternalForm());
            stage.setScene(scene);
        }
    }

    public Pane fillInBoard() {
        Pane root = new Pane();
        GameController gameController = new SingleplayerGameController(false);
        gameController.createGame(GameController.Difficulty.EASY, GameController.GameType.TICTACTOE);
        root.setPrefSize(600, 600);
        //for(Tile tile : gameController.getOptions().getBoard().getGameBoard()) {
        //    root.getChildren().add(tile);
        //}
        return root;
    }
}
