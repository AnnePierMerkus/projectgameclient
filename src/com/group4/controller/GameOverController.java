package com.group4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class GameOverController extends Controller{
    @FXML
    private Text result;

    @FXML
    private Text scorePlayer1;

    @FXML
    private Text scorePlayer2;

    @FXML
    private Button quitBtn;

    public Button getQuitBtn(){
        return this.quitBtn;
    }

    public Text getResultText(){
        return this.result;
    }

    public Text getScorePlayer1Text(){
        return this.scorePlayer1;
    }

    public Text getScorePlayer2Text(){
        return this.scorePlayer2;
    }

    public void setScoreVisibility(boolean value){
        this.getScorePlayer1Text().setVisible(value);
        this.getScorePlayer2Text().setVisible(value);
    }
}
