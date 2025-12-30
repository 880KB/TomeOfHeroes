package dev.swim.toh.model.rules.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.attribute.AttributeName;

public record AttributePrerequisite(AttributeName requiredAttributeName, int requiredValue) implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        // TODO: use total attribute value (not base value)
        return characterModel.attributes.getAttributeBaseProperty(requiredAttributeName).intValue() >= requiredValue;
    }

    @Override
    public String toString() {
        return requiredAttributeName + " " + requiredValue;
    }
}
