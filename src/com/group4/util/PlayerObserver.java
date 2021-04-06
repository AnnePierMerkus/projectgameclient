package com.group4.util;

import com.group4.controller.SingleplayerGameController;
import com.group4.util.observers.Observer;

public class PlayerObserver implements Observer {

	SingleplayerGameController spGameController;
	
	public PlayerObserver(SingleplayerGameController spGamecontroller) {
		this.spGameController = spGamecontroller;
	}

	@Override
	public void update(Object object) {
		// TODO Auto-generated method stub
		
	}
	
}
