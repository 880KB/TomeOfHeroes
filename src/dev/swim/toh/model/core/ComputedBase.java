package dev.swim.toh.model.core;

public abstract class ComputedBase {

    protected Character character;

    public void setCharacter(Character character) {
        this.character = character;
        addListeners();
    }

    public abstract void addListeners();
}
