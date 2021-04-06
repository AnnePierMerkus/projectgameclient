package com.group4.view;

import java.util.ArrayList;

import com.group4.util.observers.Observable;
import com.group4.util.observers.Observer;

public class View implements Observable {
	
	// List of all the observers watching this view.
	private ArrayList<Observer> viewObservers;
	
	public View() {
		
		// Initializing viewObservers with an empty ArrayList to contain Observer objects.
		viewObservers = new ArrayList<Observer>();
	}
	
	/**
	 * Method that adds observers to the viewObserver list.
	 */
	@Override
	public void registerObserver(Observer observer) {
		viewObservers.add(observer);
	}

	/**
	 * Method that removes observers from the viewObserver list.
	 */
	@Override
	public void removeObserver(Observer observer) {
		viewObservers.remove(observer);
	}
	
	/**
	 * Method that notifies all the observers in the viewObserver list that there is an update.
	 */
	@Override
	public void notifyObservers() {
		for(Observer observer : viewObservers) {
			observer.update(this);
		}
	}

}
