package dev.swim.toh.model.core.classes;

import dev.swim.toh.model.core.ComputedBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;

class ClassesComputed extends ComputedBase {

    private final IntegerProperty characterLevelProperty;

    ClassesComputed() {
        this.characterLevelProperty = new SimpleIntegerProperty(1);
    }

    @Override
    public void addListeners() {
        // add listener for changed level of already-present classes
        for (ChosenClass chosenClass : characterModel.classes.getClassList())
            chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) -> updateCharacterLevel());

        characterModel.classes.getClassList().addListener((ListChangeListener<? super ChosenClass>) c -> {
                    while (c.next()) {
                        if (c.wasAdded()) {
                            for (ChosenClass chosenClass : c.getAddedSubList())
                                // add listener for changed level of added class
                                chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) -> updateCharacterLevel());
                        }
                    }
                    // set character level
                    updateCharacterLevel();
                }
        );
        // set initial character level
        updateCharacterLevel();
    }

    public IntegerProperty characterLevelProperty() {
        return characterLevelProperty;
    }

    private void updateCharacterLevel() {
        int newCharacterLevel = characterModel.classes.getClassList().stream()
                .mapToInt(chosenClass -> chosenClass.levelProperty().get())
                .sum();
        characterLevelProperty.set(newCharacterLevel);
    }
}
