package com.group4.controller;

import com.group4.util.network.Client;
import com.group4.view.MyToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *Connect Controller responsible for creating and connecting client with server
 *
 * @author Gemar Koning
 */
public class ConnectController extends Controller{

    @FXML
    TextField ipAdres;

    @FXML
    TextField portAdres;

    @FXML
    MyToggleButton connect;

    /**
     * Call connect to enter
     * @param event
     */
    public void connectEnter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER)
            connectToServer();
    }

    /**
     * Connect to the server and create a new Client instance
     */
    public void connectToServer(){
        String ip_adres = this.ipAdres.getText();
        String port_adres = this.portAdres.getText().replaceAll("\\D", "");

        if (ip_adres.length() > 0 && port_adres.length() > 0){
            try{
                Client client = new Client(ip_adres, Integer.parseInt(port_adres));

                //go to multiplayer controller
                this.swap((Stage) this.portAdres.getScene().getWindow(), "Online.fxml");

                //grab the new current controller to call start function and set the connected client
                ((MultiplayerController) this.getCurrentController()).start(client);
            }catch (Exception e){
                System.out.println("Could not connect to server: " + e);
            }
        }

        this.connect.setSelected(false);
    }
}
