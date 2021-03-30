package com.group4.util.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *  Client for connection to the server
 *
 * @author Gemar Koning
 */
public class Client implements Runnable{
    private Socket socket;

    private PrintWriter output;

    private BufferedReader input;

    /**
     * Create connection to server
     *
     * @param url url location of server
     * @param port port number of server
     */
    public Client(String url, int port){
        try{
            //connect to server
            this.socket = new Socket(url, port);

            //set output
            this.output = new PrintWriter(this.socket.getOutputStream(), true);

            //set input
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        }catch (Exception e){
            System.out.println("Could not connect to server: " + e);
        }
    }

    /**
     * Automatically executed by thread. listens to the server
     */
    @Override
    public void run() {
        String line;

        try{
            while ((line = this.input.readLine()) != null){
                //change state of client determent by switch case
                System.out.println(line);
            }
        }catch (Exception e){
            System.out.println("something went wrong with reading input stream: " + e);
        }
    }

    /**
     * send a message to the server
     *
     * @param message String that should be sent to the server
     */
    public void sendMessage(String message){
        this.output.println(message);
    }

    /**
     * Close connection to the server
     */
    public void close(){
        try {
            this.input.close();
            this.output.close();
            this.socket.close();
        }catch (Exception e){
            System.out.println("Something went wrong while trying to close the client: " + e);
        }
    }
}
