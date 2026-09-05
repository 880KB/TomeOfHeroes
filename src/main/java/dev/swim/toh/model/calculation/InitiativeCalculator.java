package dev.swim.toh.model.calculation;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.attribute.AttributeName;

public class InitiativeCalculator {

    public static int getInitiative(CharacterModel characterModel) {
        return characterModel.attributes.getAttributeModProperty(AttributeName.DEXTERITY).get()
                + characterModel.initiative.getMiscModProperty().get();
    }
}
