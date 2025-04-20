package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.Character;

public abstract class CharacterAware {

    Character characterModel;

    public void setCharacterModel(Character characterModel) {
        this.characterModel = characterModel;
    }
}
