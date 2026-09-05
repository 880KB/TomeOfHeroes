package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.util.javafx.Bind;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InitiativeViewController extends CharacterModelAware {

    public TextField totalTextField;
    public TextField dexModTextField;
    public TextField miscModTextField;

    public InitiativeViewController(CharacterModel characterModel) {
        super(characterModel);
    }

    @FXML
    public void initialize() {
        bindFields();
    }

    @Override
    protected void bindFields() {
        Bind.bindIntegerPropertyToTextField(characterModel.initiative.getTotalProperty(), totalTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.attributes.getAttributeModProperty(AttributeName.DEXTERITY), dexModTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.initiative.getMiscModProperty(), miscModTextField);
    }
}
