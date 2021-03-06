package com.group4.util.observers;

public interface Observable {
	void registerObserver(Observer observer);

	void removeObserver(Observer observer);

	void notifyObservers();
}