package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.CharacterModelFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class MainWindowController {

    @FXML
    public TabPane characterTabPane;

    private final CharacterModelFactory characterModelFactory;
    private int characterCounter = 0;

    public MainWindowController(CharacterModelFactory characterModelFactory) {
        this.characterModelFactory = characterModelFactory;
    }

    @FXML
    public void initialize() {
        addCharacterTab();
    }

    @FXML
    public void onNewCharacterButtonClicked() {
        addCharacterTab();
    }

    private void addCharacterTab() {
        CharacterModel characterModel = characterModelFactory.createCharacter();
        characterCounter++;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/character-sheet.fxml"));
            loader.setControllerFactory(controllerClass -> new CharacterSheetController(characterModel));
            Parent content = loader.load();

            Tab tab = new Tab("Charakter " + characterCounter, content);
            characterTabPane.getTabs().add(tab);
            characterTabPane.getSelectionModel().select(tab);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open character tab", e);
        }
    }
}
