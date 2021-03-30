package com.group4.util.network;

import com.group4.observers.Observable;
import com.group4.observers.Observer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    private PrintWriter output;

    private BufferedReader input;

    public Client(String url, int port) {
        try{
            //socket connection to server
            this.socket = new Socket(url, port);

            //output stream
            this.output = new PrintWriter(this.socket.getOutputStream(), true);

            //input stream
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        }catch (Exception e){
            System.out.println("Could not connect to server: " + e);
        }
    }


    @Override
    public void run() {
        try{
            String line;

            while ((line = this.input.readLine()) != null){
                //notify observers of new incoming message
            }

        }catch (Exception e){
            System.out.println("Client could not receive message from server: " + e);
        }
    }

    /**
     * sends a message to the connected server
     *
     * @param message String message that needs to be send to the server
     */
    public void sendmessage(String message){
        this.output.println(message);
    }

    /**
     * Close connection with server and close input and output streams
     */
    public void close(){
        try{
            this.input.close();
            this.output.close();
            this.socket.close();
        }catch (Exception e){
            System.out.println("Could not close connection with server: " + e);
        }
    }
}
