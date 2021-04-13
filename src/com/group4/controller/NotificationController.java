package com.group4.controller;

import com.group4.util.observers.Observer;
import com.group4.view.MyToggleButton;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Controller for notification view
 *
 * @author Gemar Koning
 */
public class NotificationController {

    @FXML
    GridPane Notification;

    @FXML
    Text notification_text;

    @FXML
    MyToggleButton notificationAcceptBtn;

    @FXML
    MyToggleButton notificationDeclineBtn;

    Observer acceptObserver;

    Observer declineObserver;

    /**
     * Toon een nieuwe notificatie op het scherm
     *
     * @param msg
     * @param acceptObserver
     * @param declineObserver
     */
    public void revealNotification(String msg, Observer acceptObserver, Observer declineObserver){
        this.Notification.setVisible(true);
        this.notification_text.setText(msg);

        this.acceptObserver = acceptObserver;
        this.declineObserver = declineObserver;

        this.notificationAcceptBtn.setVisible(true);
        this.notificationDeclineBtn.setVisible(true);
    }

    /**
     * Toon een nieuwe notificatie op het scherm
     *
     * @param msg
     * @param acceptObserver
     */
    public void revealNotification(String msg, Observer acceptObserver){
        this.Notification.setVisible(true);
        this.notification_text.setText(msg);

        this.acceptObserver = acceptObserver;

        this.notificationAcceptBtn.setVisible(true);
        this.notificationDeclineBtn.setVisible(false);
    }


    /**
     * Toon een nieuwe notificatie op het scherm
     *
     * @param msg
     */
    public void revealNotification(String msg){
        this.Notification.setVisible(true);
        this.notification_text.setText(msg);

        this.notificationAcceptBtn.setVisible(false);
        this.notificationDeclineBtn.setVisible(false);
    }

    /**
     * hide revealed notification
     *
     */
    public void hideNotification(){
        this.Notification.setVisible(false);
    }

    /**
     * call observable on accept button press
     */
    public void notificationAccept(){
        System.out.println("Notification accepted");

        this.hideNotification();
        if (this.acceptObserver != null) {
            this.acceptObserver.update(this);
        }

        this.notificationAcceptBtn.setSelected(false);
    }

    /**
     * call observable on decline button press
     */
    public void notificationDecline(){
        System.out.println("Notification declined");

        this.hideNotification();
        if (this.declineObserver != null){
            this.declineObserver.update(this);
        }

        this.notificationDeclineBtn.setSelected(false);
    }
}
