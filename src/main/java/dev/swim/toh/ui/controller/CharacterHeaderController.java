package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.translation.ClazzTranslator;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.stream.Collectors;

public class CharacterHeaderController extends CharacterModelAware {

    @FXML public TextField nameTextField;
    @FXML public Label classAndLevelLabel;

    public CharacterHeaderController(CharacterModel characterModel) {
        super(characterModel);
    }

    @FXML
    public void initialize() {
        bindFields();
    }

    @Override
    protected void bindFields() {
        nameTextField.textProperty().bindBidirectional(characterModel.description.nameProperty());

        // "Klasse & Stufe" must reflect every class of a multiclass character (e.g. "Kämpfer 3 / Magier 2"),
        // not just one - so it recomputes whenever the class list or any class's level changes.
        for (ChosenClass chosenClass : characterModel.classes.getClassList())
            chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) -> updateClassAndLevel());

        characterModel.classes.getClassList().addListener((ListChangeListener<ChosenClass>) c -> {
            while (c.next()) {
                if (c.wasAdded())
                    for (ChosenClass chosenClass : c.getAddedSubList())
                        chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) -> updateClassAndLevel());
            }
            updateClassAndLevel();
        });
        updateClassAndLevel();
    }

    private void updateClassAndLevel() {
        String text = characterModel.classes.getClassList().stream()
                .map(chosenClass -> ClazzTranslator.toGerman(chosenClass.clazzProperty().get()) + " " + chosenClass.levelProperty().get())
                .collect(Collectors.joining(" / "));
        classAndLevelLabel.setText(text.isEmpty() ? "–" : text);
    }
}
