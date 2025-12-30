package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;

public abstract class CharacterModelAware {

    protected final CharacterModel characterModel;

    protected CharacterModelAware(CharacterModel characterModel) {
        this.characterModel = characterModel;
    }

    protected abstract void bindFields();
}
