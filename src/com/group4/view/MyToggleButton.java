package com.group4.view;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;

public class MyToggleButton extends ToggleButton {
    /**
     * Override click button so it doesn't become unselected and no checks are required.
     */
    @Override
    public void fire() {
        if (!this.isDisabled() && !this.isSelected())
        {
            this.setSelected(true);
            this.fireEvent(new ActionEvent());
        }
    }
}
