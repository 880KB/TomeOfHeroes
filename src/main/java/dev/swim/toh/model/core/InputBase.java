package dev.swim.toh.model.core;

public abstract class InputBase {

    protected CharacterModel characterModel;

    public void setCharacter(CharacterModel characterModel) {
        this.characterModel = characterModel;
    }
}
