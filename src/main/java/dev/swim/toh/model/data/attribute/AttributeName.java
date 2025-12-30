package dev.swim.toh.model.data.attribute;

public enum AttributeName {
    STRENGTH,
    DEXTERITY,
    CONSTITUTION,
    INTELLIGENCE,
    WISDOM,
    CHARISMA;

    @Override
    public String toString() {
        return name().substring(0, 3);
    }
}
