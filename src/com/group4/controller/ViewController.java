package com.group4.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class ViewController {
    @FXML protected void quit(ActionEvent event)
    {
        Platform.exit();
    }
}
