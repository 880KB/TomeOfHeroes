package dev.swim.toh.model.core.feats.prerequisites;

import dev.swim.toh.model.data.attribute.Attribute;

public class AttributePrerequisite implements Prerequisite {
    private final Attribute requiredAttribute;
    private final int requiredValue;

    public AttributePrerequisite(Attribute requiredAttribute, int requiredValue) {
        this.requiredAttribute = requiredAttribute;
        this.requiredValue = requiredValue;
    }

    public Attribute getRequiredAttribute() {
        return requiredAttribute;
    }

    public int getRequiredValue() {
        return requiredValue;
    }
}
