package com.group4.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/***
 * Default Controller
 */
public class Controller {

	private Stage stage;

	private Controller CurrentController;

	public Controller() {
		// TODO - Initialize
	}
	
	// TODO - View stuff ~ swapScene maybe?
	public void swap(Stage stage, String resource){
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
			Parent root = loader.load();

			this.CurrentController = loader.getController();

			Scene scene = new Scene(root, 500, 500);
			scene.getStylesheets().add(getClass().getResource("../test.css").toExternalForm());

			stage.setScene(scene);
		}catch (Exception e){
			System.out.println("New scene could not be loaded: " + e);
			e.printStackTrace();
		}
	}

	public Controller getCurrentController(){
		return this.CurrentController;
	}
}
