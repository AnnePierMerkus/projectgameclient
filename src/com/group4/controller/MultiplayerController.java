package com.group4.controller;

import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.Tile;
import com.group4.util.network.Client;
import com.group4.util.network.NetworkPlayer;
import com.group4.util.network.NetworkPlayerStates.InMatchNoTurnState;
import com.group4.util.network.NetworkPlayerStates.InMatchPlayerTurnState;
import com.group4.util.network.NetworkPlayerStates.LoginState;
import com.group4.view.MyToggleButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.util.HashMap;

/**
 * Multiplayer Controller for controlling mutiplayer view and network player
 *
 * @author Gemar Koning
 */
public class MultiplayerController extends GameController {
	
    protected Client client;

    protected Thread client_thread;

    protected HashMap<String, Player> players;

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
    ToggleGroup PlayersGroup;

    //--------------login-screen---------------

    @FXML
    TextField username;

    @FXML
    MyToggleButton connect;

    //--------------end-login-screen-----------

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
            this.players = new HashMap<>();
            this.players.put("p1", this.networkPlayer);

            //create and add second player that will be used by the server
            this.players.put("p2", new Player("p2"));

            //register new observer to client for
            this.client.registerObserver((Object object) -> {
                Client client2 = (Client) object; //upper cast object to client

                String message = client2.getMessage(); //get the most recent message

                //check if the server has received a move
                if (message.contains("GAME MOVE")){
                    HashMap<String, String> hashmap_msg = client2.messageToMap(); //parse message from server to map

                    //check if player move is the opponent of client user
                    if (!hashmap_msg.get("PLAYER").equals(this.networkPlayer.getName())){
                        this.game.getPlayer("p2").makeMove(new Tile(Integer.parseInt(hashmap_msg.get("MOVE"))));
                    }
                }

                //match is over set networkplayer state and end the game
                if (message.contains("WIN") || message.contains("LOSS") || message.contains("DRAW")){
                    this.networkPlayer.setState(new LoginState());
                    this.endGame();
                }

                //its network players turn to make a move set state
                if (message.contains("YOURTURN")){
                    this.networkPlayer.setState(new InMatchPlayerTurnState());
                }

                //a match has been started by the server
                if (message.contains("MATCH")){
                    if (client2.messageToMap().get("GAMETYPE").equals("Tic-tac-toe")){
                        this.createGame(GameType.TICTACTOE);
                    }else {
                        this.createGame(GameType.REVERSI);
                    }
                }
            });
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
        // Code to challenge someone else
    }

    @FXML
    protected void queue(ActionEvent event) throws Exception
    {
        showPlayers(true);

        addPlayers();
    }

    private void showPlayers(boolean show)
    {
        findPlayers.setSelected(false);
        playersGrid.setVisible(show);
        challenge.setVisible(show);
        onlinePlayers.setVisible(show);
    }

    /**
     * Add parameters where needed
     * @throws Exception
     */
    public void addPlayers() throws Exception
    {
        HBox playerView = FXMLLoader.load(getClass().getResource("playerView.fxml"));
        ToggleButton playerButton = (ToggleButton) playerView.getChildren().get(0);
        playerButton.setText("Playername");
        playerButton.setToggleGroup(PlayersGroup);
        playerButton.setOnAction(event -> {
            // Code when player button clicked.
        });

        playersGrid.add(playerView, 0, playersGrid.getChildren().size());
        playersGrid.getRowConstraints().add(new RowConstraints(40, 40, 40));

        RowConstraints row = matchmaking.getRowConstraints().get(4);
        row.setMinHeight(row.getMinHeight() + 41);
        row.setMaxHeight(row.getMaxHeight() + 41);
        matchmaking.getRowConstraints().set(4, row);
        System.out.println(matchmaking.getRowConstraints());
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
    public void subscribe(String type){
        this.networkPlayer.subscribe(type);// game name from view form field here
    }

    //get available games from server
    public void getAvailableGames(){
        this.networkPlayer.getAvailableGames();
    }

    @Override
    public void createGame(GameType gameType) {
    	// TODO - Add players here
        this.game = new GameOptions(Difficulty.MEDIUM, gameType, this.players);
        this.game.setGameState(GameState.PLAYING);

        //set player state
        this.networkPlayer.setState(new InMatchNoTurnState());
    }

    @Override
    public void createGame(Difficulty difficulty, GameType gameType) {
    	// TODO - Add players here
        this.game = new GameOptions(difficulty, gameType, this.players);
        this.game.setGameState(GameState.PLAYING);

        //set player state
        this.networkPlayer.setState(new InMatchNoTurnState());
    }

    @Override
    public void endGame() {
        this.networkPlayer.forfeit(); //give up on current game this wil end the game
        this.game.cleanUp(); //set players back to no game state
    }
}
