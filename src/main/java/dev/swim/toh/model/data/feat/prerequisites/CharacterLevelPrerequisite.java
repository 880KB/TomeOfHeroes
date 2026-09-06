package dev.swim.toh.model.data.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

public record CharacterLevelPrerequisite(int requiredLevel) implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return characterModel.classes.getCharacterLevelProperty().get() >= requiredLevel;
    }

    @Override
    public String toString() {
        return "Charakterstufe " + requiredLevel;
    }
}
