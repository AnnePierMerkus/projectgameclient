package com.group4.controller;

import com.group4.AI.AI;
import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.network.Client;
import com.group4.util.network.NetworkPlayer;
import com.group4.util.network.NetworkPlayerStates.InMatchNoTurnState;
import com.group4.util.network.NetworkPlayerStates.InMatchPlayerTurnState;
import com.group4.util.network.NetworkPlayerStates.LoginState;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * Multiplayer game controller for creating mutiplayer game
 *
 * @author Gemar Koning
 */
public class MultiplayerGameController extends GameController{

    private NetworkPlayer networkPlayer;

    private String startingPlayer;

    private AI AI;

    public MultiplayerGameController(AI ai){
        this.AI = ai;

        this.networkPlayer = (NetworkPlayer) PlayerList.getPlayer("p1");
    }

    @Override
    void createGame(GameType gameType) {
        this.game = new GameOptions(Difficulty.MEDIUM, gameType, this.startingPlayer);

        // Set PlayerState to has turn, turns will be monitored by server instead
        for(Player p : PlayerList.players.values()) {
            p.setPlayerState(Player.PlayerState.PLAYING_HAS_TURN);
            p.setGameProperty(this.game.getGameProperty());
        }

        this.game.setGameState(GameState.PLAYING);

        //set player state
        this.networkPlayer.setState(new InMatchNoTurnState());

        /**
        thisScene = findPlayers.getScene();
        stage = (Stage) findPlayers.getScene().getWindow();

        System.out.println("players: " + PlayerList.players.values());
        Platform.runLater(() -> {
            Scene scene = new Scene(fillInBoard());
            stage.setScene(scene);
        });**/

    }

    @Override
    void createGame(Difficulty difficulty, GameType gameType) {
        //online has no difficulty
        this.createGame(gameType);
    }

    @Override
    void endGame() {
        PlayerList.cleanUp();
    }

    //---------------------------------------Client-observers-------------------------------------------------
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

                //let the server make a move on the board
                //IMPORTANT! get the tile from the board not new otherwise tile wil not be recognised
                PlayerList.getPlayer("p2").makeMove(this.game.getBoard().getTile(Integer.parseInt(hashmap_msg.get("MOVE"))));
            }
        }

        //its network players turn to make a move set state
        if (message.contains("YOURTURN")){
            this.networkPlayer.setState(new InMatchPlayerTurnState());

            //when true let AI play instead of player
            if (this.AI != null){
                //Networkplayer makes a move with the best tile chosen by the AI.
                this.networkPlayer.makeMove(this.AI.makeMove(this.networkPlayer.getAvailableOptions()));
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
    }

    /**
     * Match has been ended by the server
     *
     * @param object client object
     */
    public void endMatch(Object object){
        Client client = (Client) object;
        String message = client.getMessage();

        //match is over set networkplayer state and end the game
        if (message.contains("WIN") || message.contains("LOSS") || message.contains("DRAW")){
            ButtonType continueButton = new ButtonType("Continue", ButtonBar.ButtonData.LEFT);

            /**
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You ! " + message.replace("SVR GAME", "").substring(0, message.lastIndexOf("{")) + this.networkPlayer.getName(), continueButton);
                alert.setTitle("Game Ended");
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.showAndWait();
                stage.setScene(this.thisScene);
            });**/

            System.out.println("players: " + PlayerList.players.values());

            //change scene back
            this.networkPlayer.setState(new LoginState());
            this.endGame(); //match has ended so end the game
        }
    }
}
