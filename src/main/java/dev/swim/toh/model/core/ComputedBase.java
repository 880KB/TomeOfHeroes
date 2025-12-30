package dev.swim.toh.model.core;

public abstract class ComputedBase {

    protected CharacterModel characterModel;

    public void setCharacter(CharacterModel characterModel) {
        this.characterModel = characterModel;
        addListeners();
    }

    public abstract void addListeners();
}
