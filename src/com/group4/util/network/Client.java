package com.group4.util.network;

import com.group4.observers.Observable;
import com.group4.observers.Observer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

                synchronized (this.messages){
                    this.messages.add(line); //store message in arraylist

                    //notify threads that are looking at the messages arraylist that a change has occurred
                    this.messages.notify();

                    //notify observers that a message has been received
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
     * Remove all received messages from list
     */
    public synchronized void clearMessages()
    {
        this.messages.clear();
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

    /**
     * Convert the most recent message from the server with a map to a HashMap
     *
     * @return HashMap with key and values from most recent message
     */
    public HashMap<String, String> messageToMap(){
        String src = new String(this.getMessage()); //make a new pointer so that we dont have to claim the lock to messages multiple times and deal with synchronization issues
        HashMap result = new HashMap();

        if (src.contains("{")){ //check if string contains map
            String map_string_only = src.substring(src.lastIndexOf('{') + 1, src.lastIndexOf('}'));//get only the part of the string with the map

            //split the string into array and iterate
            for (String element : map_string_only.split(",")){
                result.put(element.split(":")[0].replace(" ", ""), element.split(":")[1].replace(" ", "").replace("\"", "")); //remove " character and add key and values
            }
        }

        return result;
    }

    /**
     * Convert the most recent message from the server with a list to a ArrayList
     *
     * @return ArrayList with values from most recent message
     */
    public ArrayList<String> stringToArrayList(){
        String src = new String(this.getMessage()); //make a new pointer so that we dont have to claim the lock to messages multiple times and deal with synchronization issues

        ArrayList result = new ArrayList();
        if (src.contains("[")){//check if message contains list
            src.replace(" ", "");
            String map_string_only = src.substring(src.lastIndexOf('[') + 1, src.lastIndexOf(']'));//get only the part of the string with the list

            for (String element : map_string_only.split(",")){
                result.add(element.replace(" ", "").replace("\"", ""));
            }
        }

        return result;
    }
}
