package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.util.javafx.Bind;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class HitPointsViewController extends CharacterModelAware {

    public TextField currentHitPointsTextField;
    public TextField totalMaxHitPointsTextField;
    public TextField baseMaxHitPointsTextField;
    public TextField bonusMaxHitPointsTextField;

    public HitPointsViewController(CharacterModel characterModel) {
        super(characterModel);
    }

    @FXML
    public void initialize() {
        bindFields();
    }

    @Override
    protected void bindFields() {
        Bind.bindIntegerPropertyToTextField(characterModel.hitPoints.getCurrentHitPointsProperty(), currentHitPointsTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.hitPoints.getTotalMaxHitPointsProperty(), totalMaxHitPointsTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.hitPoints.getBaseMaxHitPointsProperty(), baseMaxHitPointsTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.hitPoints.getBonusMaxHitPointsProperty(), bonusMaxHitPointsTextField);
    }
}
