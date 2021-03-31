package com.group4.util.network;

import com.group4.observers.Observable;
import com.group4.observers.Observer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *  Client for connection to the server
 *
 * @author Gemar Koning
 */
public class Client implements Runnable, Observable {
    private Socket socket;

    private PrintWriter output;

    private BufferedReader input;

    private ArrayList<Observer> observers;

    private ArrayList<String> messages;

    /**
     * Create connection to server
     *
     * @param url url location of server
     * @param port port number of server
     */
    public Client(String url, int port) {
        try{
            //connect to server
            this.socket = new Socket(url, port);

            //set output
            this.output = new PrintWriter(this.socket.getOutputStream(), true);

            //set input
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            //new observers list
            this.observers = new ArrayList<>();

            //new messages list
            this.messages = new ArrayList<>();
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
                //System.out.println("Output: " + line);

                //store message
                this.addMessage(line);

                //notify observers that a message from server has been received
                synchronized (this){ //synchronized in event of notifying observers while main thread is trying to add observer
                    this.notifyObservers();
                }
            }
        }catch (Exception e){
            System.out.println("something went wrong with reading input stream: " + e);
        }finally {
            this.close(); //close connection and buffers to cleanup memory
        }
    }

    /**
     * Send a message to the server
     *
     * @param message String that should be sent to the server
     */
    public void sendMessage(String message){
        this.output.println(message);
    }

    /**
     * Return the latest received message
     *
     * @return latest received message
     */
    public synchronized String getMessage(){
        return this.messages.get(this.messages.size() - 1);
    }

    /**
     * Return all messages from server
     *
     * @return arraylist of all received messages from server
     */
    public synchronized ArrayList<String> getMessages(){
        return this.messages;
    }

    /**
     * add message to message list
     *
     * method is synchronized for the event of main thread trying to grab message while trying to store message
     *
     * method is only to be used internal by client itself
     *
     * @param message the string that needs to be added to
     */
    private synchronized void addMessage(String message){
        this.messages.add(message);
    }

    /**
     * Close connection to the server
     *
     * if the thread is running this wil throw a checked exception
     */
    public void close(){
        try {
            this.socket.close(); //important to call first otherwise thread wil hang...
            this.input.close();
            this.output.close();
        }catch (Exception e){
            System.out.println("Something went wrong while trying to close the client: " + e);
        }
    }

    /**
     * Register Observer to observers list
     *
     * @param observer
     */
    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove observer from observers list
     *
     * @param observer
     */
    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that message has been received from server
     *
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : this.observers){
            observer.update(this);
        }
    }
}
