package dev.swim.toh.translation;

import dev.swim.toh.model.data.attribute.AttributeName;

public class AttributeNameTranslator {
    public static String toGerman(AttributeName attributeName) {
        return switch (attributeName) {
            case STRENGTH -> "Stärke";
            case DEXTERITY -> "Geschicklichkeit";
            case CONSTITUTION -> "Konstitution";
            case INTELLIGENCE -> "Intelligenz";
            case WISDOM -> "Weisheit";
            case CHARISMA -> "Charisma";
        };
    }
}
