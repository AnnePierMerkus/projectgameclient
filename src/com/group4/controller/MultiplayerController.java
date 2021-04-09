package com.group4.controller;

import com.group4.model.Challenge;
import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.Player.PlayerState;
import com.group4.util.PlayerList;
import com.group4.util.Tile;
import com.group4.util.network.Client;
import com.group4.util.network.NetworkPlayer;
import com.group4.util.network.NetworkPlayerStates.InMatchNoTurnState;
import com.group4.util.network.NetworkPlayerStates.InMatchPlayerTurnState;
import com.group4.util.network.NetworkPlayerStates.LoginState;
import com.group4.view.MyToggleButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Multiplayer Controller for controlling multiplayer view and network player
 *
 * @author Gemar Koning
 */
public class MultiplayerController extends GameController {
	
    protected Client client;

    protected Thread client_thread;

    public NetworkPlayer networkPlayer;

    @FXML
    StackPane loginScreen;

    @FXML
    GridPane matchmaking;

    @FXML
    GridPane playersGrid;

    @FXML
    HBox challenge;

    @FXML
    Text onlinePlayers;

    @FXML
    MyToggleButton findPlayers;

    @FXML
    MyToggleButton joinLobby;

    @FXML
    ToggleGroup PlayersGroup;

    @FXML
    ToggleGroup GameGroup;

    //--------------login-screen---------------

    @FXML
    TextField username;

    @FXML
    MyToggleButton connect;

    //--------------end-login-screen-----------

    //--------------Notification---------------
    @FXML
    GridPane Notification;

    @FXML
    Text notification_text;

    @FXML
    MyToggleButton notificationAcceptBtn;

    @FXML
    MyToggleButton notificationDeclineBtn;

    //-------------end-notification-------------

    String startingPlayer;

    //variable to store received challenges
    private Challenge current_challenge;

    public MultiplayerController(){
        try{
            //create client for communication with server
            this.client = new Client("localhost", 7789);

            //start client on new thread for responsiveness
            this.client_thread = new Thread(client);
            this.client_thread.start();

            //create networkplayer
            this.networkPlayer = new NetworkPlayer("p1", client);

            //add networkplayer to players list
            PlayerList.addPlayer(this.networkPlayer);

            //create and add second player that will be used by the server
            PlayerList.addPlayer(new Player("p2"));

            //register new observer to client for
            this.client.registerObserver((Object object) -> {
                Client client2 = (Client) object; //upper cast object to client

                String message = client2.getMessage(); //get the most recent message

                //match is over set networkplayer state and end the game
                if (message.contains("WIN") || message.contains("LOSS") || message.contains("DRAW")){
                    this.networkPlayer.setState(new LoginState());
                    this.endGame();
                }

                //a match has been started by the server
                if (message.contains("MATCH")){
                    HashMap<String, String> messageToMap = client2.messageToMap();

                    if (messageToMap.get("PLAYERTOMOVE").equals(this.networkPlayer.getName())){
                        this.startingPlayer = "p1";
                    }else{
                        this.startingPlayer = "p2";
                    }

                    if (messageToMap.get("GAMETYPE").equals("Tic-tac-toe")){
                        this.createGame(GameType.TICTACTOE);
                    }else {
                        this.createGame(GameType.REVERSI);
                    }
                }
            });

            //register observer for adding players
            this.client.registerObserver((object -> {
                Platform.runLater(() -> {
                    //remove any previous added players
                    this.clearPlayerView();

                    //add new players
                    this.addPlayers(object);
                });
            }));

            this.client.registerObserver((object -> {
                Platform.runLater(() -> {
                    this.receiveChallenge(object);
                });
            }));

            //register turn method to determin players turn
            this.client.registerObserver((this::setTurn));

        }catch (Exception e){
            System.out.println("Could not connect to server: " + e);
        }
    }

    @FXML
    protected void tic_tac_toe(ActionEvent event)
    {
        showPlayers(false);
    }

    @FXML
    protected void reversi(ActionEvent event)
    {
        showPlayers(false);
    }

    @FXML
    protected void challenge(ActionEvent event)
    {
        ToggleButton challenge_btn = (ToggleButton) event.getSource();

        ToggleButton selected_player_btn = (ToggleButton) this.PlayersGroup.getSelectedToggle();
        ToggleButton selected_game_btn = (ToggleButton) this.GameGroup.getSelectedToggle();

        if (selected_game_btn != null){
            if (selected_player_btn != null){
                //challenge player with selected player and selected game
                this.networkPlayer.challenge(selected_player_btn.getText(), selected_game_btn.getText());
            }else{
                System.out.println("Please select a valid game");
            }
        }else {
            System.out.println("Please select a game");
        }

        challenge_btn.setSelected(false);
    }

    @FXML
    protected void queue(ActionEvent event) throws Exception
    {
        showPlayers(true);

        //send online players commando to server
        this.networkPlayer.getOnlinePlayers();
    }

    private void showPlayers(boolean show)
    {
        findPlayers.setSelected(false);
        playersGrid.setVisible(show);
        challenge.setVisible(show);
        onlinePlayers.setVisible(show);
    }

    /**
     * Add players to view with observer
     *
     * @author Anne Pier / Gemar Koning
     *
     * @param object
     */
    public void addPlayers(Object object)
    {
        Client client = (Client) object;

        if (client.getMessage().contains("PLAYERLIST")){
            for (String player_name : client.messageToArrayList()){
                if (!player_name.equals(this.networkPlayer.getName())){
                    try{
                        HBox playerView = FXMLLoader.load(getClass().getResource("playerView.fxml"));
                        ToggleButton playerButton = (ToggleButton) playerView.getChildren().get(0);

                        //add player name to button
                        playerButton.setText(player_name);

                        playerButton.setToggleGroup(PlayersGroup);
                        playerButton.setOnAction(event -> {

                        });

                        playersGrid.add(playerView, 0, playersGrid.getChildren().size());
                        playersGrid.getRowConstraints().add(new RowConstraints(40, 40, 40));

                        System.out.println(matchmaking.getRowConstraints());
                    }catch (Exception e){
                        System.out.println("Player could not be added to view." + e);
                    }
                }
            }
        }
    }

    public void setTurn(Object object){
        Client client = (Client) object; //upper cast object to client

        String message = client.getMessage(); //get the most recent message

        //check if the server has received a move
        if (message.contains("GAME MOVE")){
            HashMap<String, String> hashmap_msg = client.messageToMap(); //parse message from server to map

            //check if player move is the opponent of client user
            if (!hashmap_msg.get("PLAYER").equals(this.networkPlayer.getName())){
                //its not the players turn so set the player to no turn state
                this.networkPlayer.setState(new InMatchNoTurnState());

                //let the server make a move on the board
                PlayerList.getPlayer("p2").makeMove(new Tile(Integer.parseInt(hashmap_msg.get("MOVE"))));
            }
        }

        //its network players turn to make a move set state
        if (message.contains("YOURTURN")){
            this.networkPlayer.setState(new InMatchPlayerTurnState());
        }
    }

    //clear all players from screen
    public void clearPlayerView(){
        this.playersGrid.getChildren().clear();
    }

    @FXML
    protected void loginEnter(KeyEvent event)
    {
        if(event.getCode() == KeyCode.ENTER)
            login(null);
    }

    //log the player into the multiplayer server and set username
    @FXML
    public void login(ActionEvent event){
        if (client != null){
            if (!username.getText().trim().isEmpty()){
                this.networkPlayer.setName(username.getText());
                this.networkPlayer.login();

                loginScreen.setVisible(false);
                matchmaking.setVisible(true);
            }else{
                System.out.println("Please enter a username");
            }
        }

        this.connect.setSelected(false);
    }

    //logout from server
    public void logout(){
        this.networkPlayer.logout();
    }

    //subscribe to game type
    public void subscribe(ActionEvent event){
        ToggleButton join_lobby_btn = (ToggleButton) event.getSource();

        ToggleButton selected_game_btn = (ToggleButton) this.GameGroup.getSelectedToggle();

        if (selected_game_btn != null){
            this.networkPlayer.subscribe(selected_game_btn.getText());// game name from view form field here
        }else{
            System.out.println("please select a game to join");
        }

        joinLobby.setSelected(false);
    }

    //get available games from server
    public void getAvailableGames(){
        this.networkPlayer.getAvailableGames();
    }

    @Override
    public void createGame(GameType gameType) {
    	
    	this.game = new GameOptions(Difficulty.MEDIUM, gameType, this.startingPlayer);
    	
    	// Set PlayerState to has turn, turns will be monitored by server instead
    	for(Player p : PlayerList.players.values()) {
    		p.setPlayerState(PlayerState.PLAYING_HAS_TURN);
    		p.setGameProperty(this.game.getGameProperty());
    	}
    	
    	this.game.setGameState(GameState.PLAYING);

        //set player state
        this.networkPlayer.setState(new InMatchNoTurnState());

        Stage stage = (Stage) notificationAcceptBtn.getScene().getWindow();

        Platform.runLater(() -> {
            Scene scene = new Scene(fillInBoard());
            stage.setScene(scene);
        });
    }

    @Override
    public void createGame(Difficulty difficulty, GameType gameType) {
    	
    	this.game = new GameOptions(difficulty, gameType, this.startingPlayer);
    	
    	// Set PlayerState to has turn, turns will be monitored by server instead
    	for(Player p : PlayerList.players.values()) {
    		p.setPlayerState(PlayerState.PLAYING_HAS_TURN);
    		p.setGameProperty(this.game.getGameProperty());
    	}
        
        this.game.setGameState(GameState.PLAYING);

        //set player state
        this.networkPlayer.setState(new InMatchNoTurnState());
    }

    @Override
    public void endGame() {
        PlayerList.cleanUp(); //set players back to no game state
    }

    public void giveUp(){
        this.networkPlayer.forfeit(); //give up on current game this wil end the game
        this.endGame();
    }

    public void receiveChallenge(Object object){
        Client client = (Client) object;
        String message = client.getMessage();

        if (message.contains("GAME CHALLENGE")){
            HashMap<String, String> message_to_map = client.messageToMap();

            //challenge received and not canceled
            if (!message.contains("CANCELLED")){
                //make a new current challenge
                this.current_challenge = new Challenge(message_to_map.get("CHALLENGER"), message_to_map.get("CHALLENGENUMBER"), message_to_map.get("GAMETYPE"));

                this.revealNotification("You have been challenged by " + this.current_challenge.getUsername(), true);
            }else{

                this.revealNotification("Challenge has been cancelled, number" + message_to_map.get("CHALLENGENUMBER"), false);

                System.out.println("Challenge has been cancelled by player");
            }
        }
    }

    public void notificationAccept(){
        this.networkPlayer.acceptChallenge(this.current_challenge.getCode());
        this.Notification.setVisible(false);
        this.notificationAcceptBtn.setVisible(false);
    }

    public GridPane fillInBoard() {
        GridPane root = new GridPane();
        root.setPrefSize(600, 600);

        Iterator it = getOptions().getBoard().getGameBoard().entrySet().iterator();
        int row = 0;
        int column = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            root.add((Tile)pair.getValue(), column, row);
            column++;

            if (((int)pair.getKey() + 1) % getOptions().getBoard().getWidth() == 0)
            {
                column = 0;
                row++;
            }
        }
        return root;
    }

    public void notificationDecline(){
        this.Notification.setVisible(false);
        this.notificationDeclineBtn.setVisible(false);
    }

    public void revealNotification(String msg, boolean controls){
        this.Notification.setVisible(true);
        this.notification_text.setText(msg);

        if (controls){
            //reveal notification buttons
            this.notificationAcceptBtn.setVisible(true);
            this.notificationDeclineBtn.setVisible(true);
        }else {
            //reveal notification buttons
            this.notificationAcceptBtn.setVisible(false);
            this.notificationDeclineBtn.setVisible(false);
        }
    }
}
