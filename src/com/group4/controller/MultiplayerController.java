package com.group4.controller;

import com.group4.AI.AI;
import com.group4.games.REVERSI;
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Map.Entry;

/**
 * Multiplayer Controller for controlling multiplayer view and network player
 *
 * @author Gemar Koning
 */
public class MultiplayerController extends Controller{
    // Main Window
    Stage stage;
    Scene thisScene;

    protected Client client;

    protected Thread client_thread;

    public NetworkPlayer networkPlayer;

    @FXML
    StackPane loginScreen;

    @FXML
    StackPane matchmaking;

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

    //--------------Notification---------------
    @FXML
    NotificationController notificationController;

    //--------------AI-------------------------
    @FXML
    ToggleButton AIBtn;

    @FXML
    TextField AIDepth;

    AI AI;

    //variable to store received challenges
    private Challenge current_challenge;

    private MultiplayerGameController multiplayerGameController;

    public void start(Client client){
        this.multiplayerGameController = new MultiplayerGameController();

        try{
            //create client for communication with server
            //this.client = new Client("localhost", 7789);
            this.client = client;

            //start client on new thread for responsiveness
            this.client_thread = new Thread(client);
            this.client_thread.start();

            //create networkplayer
            this.networkPlayer = new NetworkPlayer("p1", client);

            //add networkplayer to players list
            PlayerList.addPlayer(this.networkPlayer);

            //create and add second player that will be used by the server
            PlayerList.addPlayer(new Player("p2"));

            //register new observer to client for starting a match when server starts a match
            this.client.registerObserver(this::startMatch);

            //register turn method to determine players turn
            this.client.registerObserver((this::setTurn));

            //register end match method
            this.client.registerObserver(this::endMatch);

            //--------------Observers-for-changing-view-----------------------------

            //register observer for adding players
            this.client.registerObserver((object -> {
                Platform.runLater(() -> {
                    //remove any previous added players
                    this.clearPlayerView();

                    //add new players
                    this.addPlayers(object);
                });
            }));

            //register observer for receiving challenges from online players
            this.client.registerObserver((object -> {
                Platform.runLater(() -> {
                    this.receiveChallenge(object);
                });
            }));

        }catch (Exception e){
            System.out.println("Could not connect to server: " + e);
        }
    }

    @FXML
    protected void tic_tac_toe(ActionEvent event)
    {
        showPlayers(false);
        showAI(true);
        resetAI();
    }

    @FXML
    protected void reversi(ActionEvent event)
    {
        showPlayers(false);
        showAI(true);
        resetAI();
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
                this.notificationController.revealNotification("Challenge for " + selected_game_btn.getText() + " has been send to " + selected_player_btn.getText());
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

    private void showAI(boolean show){
        this.AIBtn.setVisible(show);
        this.AIDepth.setVisible(show);
    }

    private void resetAI(){
        this.AIDepth.clear();
        this.AIDepth.setDisable(false);
        this.AIBtn.setSelected(false);
        this.AIBtn.setText("Enable AI");
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
                if (this.networkPlayer.login()){
                    loginScreen.setVisible(false);
                    matchmaking.setVisible(true);
                }
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
            this.notificationController.revealNotification("Joining " + selected_game_btn.getText());
            this.networkPlayer.subscribe(selected_game_btn.getText());// game name from view form field here
        }else{
            System.out.println("please select a game to join");
        }

        joinLobby.setSelected(false);
    }

    /**
     * Create a new AI instance to be used by networkplayer
     *
     * @param event action event
     */
    public void setAI(Event event){
        //when true create AI instance to be used by networkplayer
        if (this.AIBtn.isSelected() && this.AIDepth.getText().replaceAll("\\D", "").length() > 0){
            this.AIDepth.setDisable(true);
            ToggleButton selected_game_btn =  (ToggleButton) this.GameGroup.getSelectedToggle();

            if (selected_game_btn != null){
                try{
                    //make a new ai instance
                    //this.AI = (AI) Class.forName("com.group4.AI." + selected_game_btn.getText().replace("-", "").toUpperCase() + "AI").newInstance();
                    this.AI = this.createAI(selected_game_btn.getText());
                    this.AIBtn.setText("Disable AI");
                }catch (Exception e){
                    System.out.println("AI could not be Created: " + e);
                    e.printStackTrace();
                    this.AIBtn.setSelected(false);
                }
            }
        }else{
            this.AIBtn.setText("Enable AI");
            this.AIBtn.setSelected(false);
            this.AIDepth.setDisable(false);
            this.AI = null; //remove any previous set ai instances
        }
    }

    /**
     * Create new Ai from String name
     *
     * @param type AI type
     * @return new AI
     * @throws Exception
     */
    public AI createAI(String type) throws Exception {
        return (AI) Class.forName("com.group4.AI." + type.replace("-", "").toUpperCase() + "AI").newInstance();
    }

    public void giveUp(){
        this.networkPlayer.forfeit(); //give up on current game this wil end the game
        this.multiplayerGameController.endGame();
    }

    //The methods underneath this section are ALL executed by the server with a observer --------------------------

    /**
     * receive a challenge from a player online
     *
     * @param object client object
     */
    public void receiveChallenge(Object object){
        Client client = (Client) object;
        String message = client.getMessage();

        if (message.contains("GAME CHALLENGE")){
            HashMap<String, String> message_to_map = client.messageToMap();

            //challenge received and not canceled
            if (!message.contains("CANCELLED")){
                //make a new current challenge
                this.current_challenge = new Challenge(message_to_map.get("CHALLENGER"), message_to_map.get("CHALLENGENUMBER"), message_to_map.get("GAMETYPE"));

                this.notificationController.revealNotification("You have been challenged by " + this.current_challenge.getUsername() + " | " + this.current_challenge.getGame_type(), (object1 -> {
                    this.networkPlayer.acceptChallenge(this.current_challenge.getCode());
                }), (object1 -> {
                    System.out.println("Challenge declined");
                }));
            }else{
                this.notificationController.revealNotification("Challenge has been cancelled, number" + message_to_map.get("CHALLENGENUMBER"));
                System.out.println("Challenge has been cancelled by player");
            }
        }
    }

    /**
     * Add players to view with observer
     *
     * @author Anne Pier / Gemar Koning
     *
     * @param object client object
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
                    }catch (Exception e){
                        System.out.println("Player could not be added to view." + e);
                    }
                }
            }
        }
    }

    /**
     * Determin the players turn and make a move for the server incase the server has made a turn
     *
     * @param object Client object
     */
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
                this.multiplayerGameController.game.setPlayerTurn("p2");

                //let the server make a move on the board
                //IMPORTANT! get the tile from the board not new otherwise tile wil not be recognised
                PlayerList.getPlayer("p2").makeMove(this.multiplayerGameController.game.getBoard().getTile(Integer.parseInt(hashmap_msg.get("MOVE"))));
                this.setTurnImage("p2");
            }else {
                this.setTurnImage("p1");
            }

            //set current scores of players
            this.setScorePlayer1(String.valueOf(this.multiplayerGameController.game.getBoard().getScore(PlayerList.getPlayer("p1"))));
            this.setScorePlayer2(String.valueOf(this.multiplayerGameController.game.getBoard().getScore(PlayerList.getPlayer("p2"))));

            //set the available moves
            this.setAvailableMovesPlayer1("Available moves: \n" + PlayerList.getPlayer("p1").getAvailableOptions().size());
            this.setAvailableMovesPlayer2("Available moves: \n" + PlayerList.getPlayer("p2").getAvailableOptions().size());
        }

        //its network players turn to make a move set state
        if (message.contains("YOURTURN")){
            this.networkPlayer.setState(new InMatchPlayerTurnState());
            this.multiplayerGameController.game.setPlayerTurn("p1");
            
            //when true let AI play instead of player
            if (this.AI != null){
                //Networkplayer makes a move with the best tile chosen by the AI.
            	Tile aiMove = this.AI.makeMove(this.networkPlayer.getAvailableOptions());
                if(aiMove != null) {
                	this.networkPlayer.makeMove(aiMove);//TODO depth meegeven
                }else {
                	//TODO - What to do with no more moves?
                	System.out.println("No more moves :)");
                }
            }
        }
    }

    /**
     * Prepare and create a new Game
     *
     * Called when server has started a match
     *
     * @param object Client object
     */
    public void startMatch(Object object){
        Client client = (Client) object; //upper cast object to client

        String message = client.getMessage(); //get the most recent message

        //a match has been started by the server
        if (message.contains("MATCH")){
            HashMap<String, String> messageToMap = client.messageToMap();

            if (messageToMap.get("PLAYERTOMOVE").equals(this.networkPlayer.getName())){
                this.multiplayerGameController.setStartingPlayer(this.networkPlayer);
            }else{
                this.multiplayerGameController.setStartingPlayer(PlayerList.getPlayer("p2"));
            }

            if (messageToMap.get("GAMETYPE").equals("Tic-tac-toe")){
                this.multiplayerGameController.createGame(GameController.GameType.TICTACTOE);
            }else {
                this.multiplayerGameController.createGame(GameController.GameType.REVERSI);
            }

            if (this.AI != null){
                //if AI differs for some reason from challenge make the right type AI
                if (!((MyToggleButton)this.GameGroup.getSelectedToggle()).getText().equals(messageToMap.get("GAMETYPE"))){
                    try{
                        this.createAI(messageToMap.get("GAMETYPE"));
                    }catch (Exception e){
                        System.out.println("Ai could not be created: " + e);
                    }
                }
                //set ai type
                this.AI.setAIType(this.multiplayerGameController.game, this.multiplayerGameController.game.getGameType(), AIDepth.getText().length() > 0 ? Integer.parseInt(AIDepth.getText()) : 5);
            }

            thisScene = findPlayers.getScene();
            stage = (Stage) findPlayers.getScene().getWindow();

            System.out.println("players: " + PlayerList.players.values());
            Platform.runLater(() -> {
                Scene scene = new Scene(fillInBoard(this.multiplayerGameController.game.getGameType(), this.multiplayerGameController, true));
                stage.setScene(scene);
            });
        }
    }

    /**
     * Match has been ended by the server
     *
     * @param object client object
     */
    public void endMatch(Object object){
        Client client = (Client) object;
        String message = client.getMessage();
        HashMap<String, String> messageToMap = client.messageToMap();

        //match is over set networkplayer state and end the game
        if (message.contains("WIN") || message.contains("LOSS") || message.contains("DRAW")){
            Platform.runLater(() -> {
                //go to GameOverScreen
                this.swap(stage, "EndGame.fxml");
                GameOverController gameOverController = (GameOverController) this.getCurrentController();

                if (message.contains("WIN")){
                    gameOverController.getResultText().setText("Je Hebt Gewonnen!");
                }else if (message.contains("LOSS")){
                    gameOverController.getResultText().setText("Je Hebt verloren :(");
                }else {
                    gameOverController.getResultText().setText("Gelijk Spel!");
                }

                if (this.multiplayerGameController.game.getGameProperty() instanceof REVERSI) {
                    gameOverController.setScoreVisibility(true);
                    gameOverController.getScorePlayer1Text().setText("Score Speler 1: " + messageToMap.get("PLAYERONESCORE"));
                    gameOverController.getScorePlayer2Text().setText("Score Speler 2: " + messageToMap.get("PLAYERTWOSCORE"));
                }else {
                    gameOverController.setScoreVisibility(false);
                }

                gameOverController.getQuitBtn().setOnAction((event) -> {
                    stage.setScene(this.thisScene);
                });
            });

            System.out.println("SERVER COMMENT: " + client.messageToMap().get("COMMENT"));
            System.out.println("players: " + PlayerList.players.values());

            //change scene back
            this.networkPlayer.setState(new LoginState());
            this.multiplayerGameController.endGame(); //match has ended so end the game
        }
    }
}
