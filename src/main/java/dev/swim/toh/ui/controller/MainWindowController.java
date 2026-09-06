package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.CharacterModelFactory;
import dev.swim.toh.persistence.CharacterFileService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class MainWindowController {

    @FXML
    public TabPane characterTabPane;

    private final CharacterModelFactory characterModelFactory;
    private final CharacterFileService characterFileService;
    private int characterCounter = 0;

    public MainWindowController(CharacterModelFactory characterModelFactory, CharacterFileService characterFileService) {
        this.characterModelFactory = characterModelFactory;
        this.characterFileService = characterFileService;
    }

    @FXML
    public void initialize() {
        addCharacterTab(characterModelFactory.createCharacter(), null);
    }

    @FXML
    public void onNewCharacterButtonClicked() {
        addCharacterTab(characterModelFactory.createCharacter(), null);
    }

    @FXML
    public void onOpenButtonClicked() {
        File file = characterFileChooser("Charakter öffnen").showOpenDialog(getWindow());
        if (file == null)
            return;

        try {
            addCharacterTab(characterFileService.load(file), file);
        } catch (IOException e) {
            showError("Charakter konnte nicht geladen werden", e);
        }
    }

    @FXML
    public void onSaveButtonClicked() {
        CharacterTabState state = getSelectedTabState();
        if (state == null)
            return;

        if (state.file == null)
            saveAs(state);
        else
            save(state, state.file);
    }

    @FXML
    public void onSaveAsButtonClicked() {
        CharacterTabState state = getSelectedTabState();
        if (state != null)
            saveAs(state);
    }

    private void saveAs(CharacterTabState state) {
        FileChooser fileChooser = characterFileChooser("Charakter speichern unter");
        fileChooser.setInitialFileName(sanitizeFileName(state.characterModel.description.nameProperty().get()) + ".json");
        File file = fileChooser.showSaveDialog(getWindow());
        if (file != null)
            save(state, file);
    }

    private void save(CharacterTabState state, File file) {
        try {
            characterFileService.save(state.characterModel, file);
            state.file = file;
        } catch (IOException e) {
            showError("Charakter konnte nicht gespeichert werden", e);
        }
    }

    private CharacterTabState getSelectedTabState() {
        Tab tab = characterTabPane.getSelectionModel().getSelectedItem();
        return tab == null ? null : (CharacterTabState) tab.getUserData();
    }

    private FileChooser characterFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Charakterdatei (*.json)", "*.json"));
        return fileChooser;
    }

    private Window getWindow() {
        return characterTabPane.getScene().getWindow();
    }

    private void showError(String header, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(header);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private String sanitizeFileName(String name) {
        return (name == null || name.isBlank()) ? "charakter" : name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    private void addCharacterTab(CharacterModel characterModel, File file) {
        characterCounter++;
        String fallbackName = "Charakter " + characterCounter;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/character-sheet.fxml"));
            loader.setControllerFactory(controllerClass -> new CharacterSheetController(characterModel));
            Parent content = loader.load();

            Tab tab = new Tab();
            tab.setContent(content);
            tab.setUserData(new CharacterTabState(characterModel, file));
            // keep the tab usable even if the name is cleared, instead of shrinking to an empty sliver
            tab.textProperty().bind(Bindings.createStringBinding(
                    () -> {
                        String name = characterModel.description.nameProperty().get();
                        return (name == null || name.isBlank()) ? fallbackName : name;
                    },
                    characterModel.description.nameProperty()));

            characterTabPane.getTabs().add(tab);
            characterTabPane.getSelectionModel().select(tab);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open character tab", e);
        }
    }

    /** Tracks the file a tab's character was last loaded from/saved to, so "Speichern" can skip the file dialog. */
    private static class CharacterTabState {
        final CharacterModel characterModel;
        File file;

        CharacterTabState(CharacterModel characterModel, File file) {
            this.characterModel = characterModel;
            this.file = file;
        }
    }
}
