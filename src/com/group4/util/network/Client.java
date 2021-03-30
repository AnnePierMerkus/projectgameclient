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

    @Override
    public void run() {
        String line;

        try{
            while ((line = this.input.readLine()) != null){
                //change state of client determent by switch case
            }
        }catch (Exception e){
            System.out.println("something went wrong with reading input stream: " + e);
        }
    }

    public void close(){
        try {
            this.input.close();
            this.output.close();
            this.socket.close();
        }catch (Exception e){
            System.out.println("Something whent wrong while trying to close the client: " + e);
        }
    }
}
