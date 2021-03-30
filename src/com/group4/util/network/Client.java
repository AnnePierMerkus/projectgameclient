package com.group4.util.network;

import com.group4.observers.Observable;
import com.group4.observers.Observer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * client class for connecting to multiplayer server
 * implements runnable so thread can run class
 *
 * @author Gemar Koning
 */
public class Client implements Runnable {
    private Socket socket;

    public Client(String url, int port) {
        try{
            this.socket = new Socket(url, port);
        }catch (Exception e){
            System.out.println("Could not connect to server: " + e);
        }
    }


    @Override
    public void run() {
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String input;

            while ((input = reader.readLine()) != null){

            }

        }catch (Exception e){
            System.out.println("Client could not receive message from server: " + e);
        }
    }
}
