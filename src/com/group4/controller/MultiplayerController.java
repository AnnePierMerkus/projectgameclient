package com.group4.controller;

import com.group4.AI.AI;
import com.group4.games.REVERSI;
import com.group4.model.Challenge;
import com.group4.util.Player;
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

/**
 * Multi-player Controller for controlling multi-player view and network player
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

    // Variable to store received challenges
    private Challenge current_challenge;

    // The controller that controls the multi-player game
    private MultiplayerGameController multiplayerGameController;

    /**
     * Start the multi-player controller and set the necessary variables
     *
     * @param client Client connected to server
     * @author Gemar Koning
     */
    public void start(Client client){
        this.multiplayerGameController = new MultiplayerGameController();

        try{
            // Create client for communication with server
            this.client = client;

            // Start client on new thread for responsiveness
            this.client_thread = new Thread(client);
            this.client_thread.start();

            // Create networkplayer
            this.networkPlayer = new NetworkPlayer("p1", client);

            // Add networkplayer to players list
            PlayerList.addPlayer(this.networkPlayer);

            // Create and add second player that will be used by the server
            PlayerList.addPlayer(new Player("p2"));

            // Register new observer to client for starting a match when server starts a match
            this.client.registerObserver(this::startMatch);

            // Register turn method to determine players turn
            this.client.registerObserver((this::setTurn));

            // Register end match method
            this.client.registerObserver(this::endMatch);

            //--------------Observers-for-changing-view-----------------------------

            // Register observer for adding players
            this.client.registerObserver((object -> {
                Platform.runLater(() -> {
                    // Remove any previous added players
                    this.clearPlayerView();

                    // Add new players
                    this.addPlayers(object);
                });
            }));

            // Register observer for receiving challenges from online players
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

    /**
     * Challenge online player selected from player list
     *
     * @param event Action event
     * @author Gemar Koning
     */
    @FXML
    protected void challenge(ActionEvent event)
    {
        ToggleButton challenge_btn = (ToggleButton) event.getSource();

        ToggleButton selected_player_btn = (ToggleButton) this.PlayersGroup.getSelectedToggle();
        ToggleButton selected_game_btn = (ToggleButton) this.GameGroup.getSelectedToggle();

        if (selected_game_btn != null){
            if (selected_player_btn != null){
                // Challenge player with selected player and selected game
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

    /**
     * Get all online connected players
     *
     * @param event Action event
     * @throws Exception
     * @author Gemar Koning
     */
    @FXML
    protected void queue(ActionEvent event) throws Exception
    {
        showPlayers(true);

        // Send online players command to server
        this.networkPlayer.getOnlinePlayers();
    }

    /**
     * Show active players
     *
     * @param show
     * @author Gemar Koning
     */
    private void showPlayers(boolean show)
    {
        findPlayers.setSelected(false);
        playersGrid.setVisible(show);
        challenge.setVisible(show);
        onlinePlayers.setVisible(show);
    }

    /**
     * Show AI button and depth field
     *
     * @param show boolean
     * @author Gemar Koning
     */
    private void showAI(boolean show){
        this.AIBtn.setVisible(show);
        this.AIDepth.setVisible(show);
    }

    /**
     * Reset the AI button and depth textfield
     * 
     * @author Gemar Koning
     */
    private void resetAI(){
        this.AIDepth.clear();
        this.AIDepth.setDisable(false);
        this.AIBtn.setSelected(false);
        this.AIBtn.setText("Enable AI");
        this.AI = null;
    }

    /**
     * Clear all players from screen
     * 
     * @author Gemar Koning
     */
    public void clearPlayerView(){
        this.playersGrid.getChildren().clear();
    }

    /**
     * Call login on enter press
     * @param event
     * 
     * @author Gemar Koning
     */
    @FXML
    protected void loginEnter(KeyEvent event)
    {
        if(event.getCode() == KeyCode.ENTER)
            login(null);
    }

    /**
     * Log the player into the multiplayer server and set username
     *
     * @param event
     * @author Gemar Koning
     */
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

    /**
     * Logout from server
     * 
     * @author Gemar Koning
     */
    public void logout(){
        this.networkPlayer.logout();
    }

    /**
     * Subscribe to game type
     *
     * @param event
     * @author Gemar Koning
     */
    public void subscribe(ActionEvent event){

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
     * @author Gemar Koning
     */
    public void setAI(Event event){
        // When true create AI instance to be used by networkplayer
        if (this.AIBtn.isSelected() && this.AIDepth.getText().replaceAll("\\D", "").length() > 0){
            this.AIDepth.setDisable(true);
            ToggleButton selected_game_btn =  (ToggleButton) this.GameGroup.getSelectedToggle();

            if (selected_game_btn != null){
                try{
                    // Make a new ai instance
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
            this.AI = null; // Remove any previous set AI instances
        }
    }

    /**
     * Create new Ai from String name
     *
     * @param type AI type
     * @return new AI
     * @throws Exception
     * @author Gemar Koning
     */
    @SuppressWarnings("deprecation")
	public AI createAI(String type) throws Exception {
        return (AI) Class.forName("com.group4.AI." + type.replace("-", "").toUpperCase() + "AI").newInstance();
    }

    /**
     * Logout player and close connection to server and stop the client Thread
     *
     * @param event Action event
     * @throws Exception
     * @author Gemar Koning
     */
    public void close(Event event) throws Exception{
        this.networkPlayer.logout();
        // Wait for 500 miliseconds for server to respond
        Thread.sleep(500);
        // Close the client socket and there by stop the thread
        this.client.close();
        // Go back to main menu
        this.swap((Stage) this.findPlayers.getScene().getWindow(), "../MainMenu.fxml");
    }

    /**
     * Give Up during the Match
     * 
     * @author Gemar Koning
     */
    public void giveUp(){
        this.networkPlayer.forfeit(); //give up on current game this wil end the game
        this.multiplayerGameController.endGame();
    }

    // The methods underneath this section are ALL executed by the server with a observer --------------------------

    /**
     * receive a challenge from a player online
     *
     * @param object client object
     * @author Gemar Koning
     */
    public void receiveChallenge(Object object){
        Client client = (Client) object;
        String message = client.getMessage();

        if (message.contains("GAME CHALLENGE")){
            HashMap<String, String> message_to_map = client.messageToMap();

            // Challenge received and not canceled
            if (!message.contains("CANCELLED")){
                // Make a new current challenge
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
     * @param object client object
     * @author Anne Pier & Gemar Koning
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

                        // Add player name to button
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
     * Determine the players turn and make a move for the server increase the server has made a turn
     *
     * @param object Client object
     * @author Gemar Koning
     */
    public void setTurn(Object object){
        Client client = (Client) object; //upper cast object to client

        String message = client.getMessage(); //get the most recent message

        // Check if the server has received a move
        if (message.contains("GAME MOVE")){
            HashMap<String, String> hashmap_msg = client.messageToMap(); // Parse message from server to map

            // Check if player move is the opponent of client user
            if (!hashmap_msg.get("PLAYER").equals(this.networkPlayer.getName())){
                // It's not the players turn so set the player to no turn state
                this.networkPlayer.setState(new InMatchNoTurnState());
                this.multiplayerGameController.game.setPlayerTurn("p2");

                // Let the server make a move on the board
                // IMPORTANT! get the tile from the board not new otherwise tile will not be recognized
                PlayerList.getPlayer("p2").makeMove(this.multiplayerGameController.game.getBoard().getTile(0, Integer.parseInt(hashmap_msg.get("MOVE"))), 0);
                this.setTurnImage("p2");
            }else {
                this.setTurnImage("p1");
            }

            // Set current scores of players
            this.setScorePlayer1(String.valueOf(this.multiplayerGameController.game.getBoard().getScore(0, PlayerList.getPlayer("p1"))));
            this.setScorePlayer2(String.valueOf(this.multiplayerGameController.game.getBoard().getScore(0, PlayerList.getPlayer("p2"))));

            // Set the available moves
            this.setAvailableMovesPlayer1("Available moves: \n" + PlayerList.getPlayer("p1").getAvailableOptions(0).size());
            this.setAvailableMovesPlayer2("Available moves: \n" + PlayerList.getPlayer("p2").getAvailableOptions(0).size());
        }

        // It's the network players' turn to make a move set state
        if (message.contains("YOURTURN")){
            this.networkPlayer.setState(new InMatchPlayerTurnState());
            this.multiplayerGameController.game.setPlayerTurn("p1");
            
            // When true let AI play instead of player
            if (this.AI != null){
                // Networkplayer makes a move with the best tile chosen by the AI.
            	Tile aiMove = this.AI.makeMove(this.networkPlayer.getAvailableOptions(0));
                if(aiMove != null) {
                	this.networkPlayer.makeMove(aiMove, 0);
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
     * @author Gemar Koning
     */
    public void startMatch(Object object){
        Client client = (Client) object; // Upper cast object to client

        String message = client.getMessage(); // Get the most recent message

        // A match has been started by the server
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
                // If AI differs for some reason from challenge make the right type AI
                if (!((MyToggleButton)this.GameGroup.getSelectedToggle()).getText().equals(messageToMap.get("GAMETYPE"))){
                    System.out.println("Challenge differs from enabled AI type Creating new AI");
                    try{
                        this.createAI(messageToMap.get("GAMETYPE"));
                    }catch (Exception e){
                        System.out.println("Ai could not be created: " + e);
                    }
                }

                // Set AI type
                this.AI.setAIType(this.multiplayerGameController.game, this.multiplayerGameController.game.getGameType(), AIDepth.getText().length() > 0 ? Integer.parseInt(AIDepth.getText()) : 5);
            }

            thisScene = findPlayers.getScene();
            stage = (Stage) findPlayers.getScene().getWindow();

            System.out.println("players: " + PlayerList.players.values());
            Platform.runLater(() -> {
                Scene scene = new Scene(fillInBoard(this.multiplayerGameController.game.getGameType(), this.multiplayerGameController, true));
                // Set quitbutton action
                this.getQuitButton().setOnAction((event) -> {
                    this.giveUp();
                });
                stage.setScene(scene);
            });
        }
    }

    /**
     * Match has been ended by the server
     *
     * @param object client object
     * @author Gemar Koning
     */
    public void endMatch(Object object){
        Client client = (Client) object;
        String message = client.getMessage();
        HashMap<String, String> messageToMap = client.messageToMap();

        // Match is over set networkplayer state and end the game
        if (message.contains("WIN") || message.contains("LOSS") || message.contains("DRAW")){
            Platform.runLater(() -> {
                // Go to GameOverScreen
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

            this.client.clearMessages();
            
            // Change scene back
            this.networkPlayer.setState(new LoginState());
            this.multiplayerGameController.endGame(); //match has ended so end the game
        }
    }
}
