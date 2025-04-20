package dev.swim.toh.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SavingThrowsCardController extends CharacterModelAware {

    public Button testButton;

    @FXML
    public void initialize() {
    }

    protected void bindFields() {
        System.out.println(">>> Binding fields in " + this);
    }

    public void handleTestButtonClicked(ActionEvent actionEvent) {
        System.out.println(">>> Button clicked. Controller instance: " + this);
        System.out.println(">>> characterModel = " + characterModel);
        characterModel.print();
    }
}
