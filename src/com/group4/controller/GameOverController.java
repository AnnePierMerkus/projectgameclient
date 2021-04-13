package com.group4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * Controller that is responsible for the GameOverView
 *
 * @author Gemar Koning
 */
public class GameOverController extends Controller{
    @FXML
    private Text result;

    @FXML
    private Text scorePlayer1;

    @FXML
    private Text scorePlayer2;

    @FXML
    private Button quitBtn;

    /**
     * get quit button from view
     *
     * @return quit button
     */
    public Button getQuitBtn(){
        return this.quitBtn;
    }

    /**
     * Get result Text
     *
     * @return result Text
     */
    public Text getResultText(){
        return this.result;
    }

    /**
     * Get score player one
     *
     * @return text score player one
     */
    public Text getScorePlayer1Text(){
        return this.scorePlayer1;
    }

    /**
     * get score player two
     *
     * @return text score player two
     */
    public Text getScorePlayer2Text(){
        return this.scorePlayer2;
    }

    /**
     * Set the scores from player 1 and 2 visibility
     *
     * @param value visibility
     */
    public void setScoreVisibility(boolean value){
        this.getScorePlayer1Text().setVisible(value);
        this.getScorePlayer2Text().setVisible(value);
    }
}
