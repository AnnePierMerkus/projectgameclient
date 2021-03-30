package com.group4.controller;

import com.group4.model.GameOptions;

public class GameController extends Controller {
	
	public enum GameType {
		TICTACTOE, REVERSI
	};
	
	public enum Difficulty {
		EASY, MEDIUM, HARD
	};
	
	public enum GameState {
		PREPARING, PLAYING, ENDED
	}
	
	// The game that is currently going on
	protected GameOptions game = null;
	
	protected GameController() {
		super();
	}
	
}
