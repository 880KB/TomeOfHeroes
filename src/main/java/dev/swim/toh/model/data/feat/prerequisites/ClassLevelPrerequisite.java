package dev.swim.toh.model.data.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.clazz.Clazz;

import java.util.List;

/**
 * Satisfied if the character has at least {@code requiredLevel} levels in any of
 * {@code requiredClasses} - a list rather than a single class to also cover "either/or"
 * prerequisites like "Kleriker oder Paladin".
 */
public record ClassLevelPrerequisite(List<Clazz> requiredClasses, int requiredLevel) implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return characterModel.classes.getClassList().stream()
                .filter(chosenClass -> requiredClasses.contains(chosenClass.clazzProperty().get()))
                .anyMatch(chosenClass -> chosenClass.levelProperty().get() >= requiredLevel);
    }

    @Override
    public String toString() {
        return requiredClasses + " Stufe " + requiredLevel;
    }
}
