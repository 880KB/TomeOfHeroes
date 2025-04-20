package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.Character;

public abstract class CharacterModelAware {

    Character characterModel;

    public void setCharacterModel(Character characterModel) {
        System.out.println(">>> setCharacterModel called in " + this);
        this.characterModel = characterModel;
        bindFields();
    }

    protected abstract void bindFields();
}
