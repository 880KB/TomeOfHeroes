package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.util.javafx.Bind;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ArmorClassViewController extends CharacterModelAware {

    public TextField totalAcTextField;
    public TextField armorBonusTextField;
    public TextField shieldBonusTextField;
    public TextField dexModTextField;
    public TextField naturalArmorTextField;
    public TextField sizeModTextField;
    public TextField deflectionBonusTextField;
    public TextField miscModTextField;
    public TextField touchAcTextField;
    public TextField flatFootedAcTextField;
    public TextField spellResistanceTextField;

    public ArmorClassViewController(CharacterModel characterModel) {
        super(characterModel);
    }

    @FXML
    public void initialize() {
        bindFields();
    }

    @Override
    protected void bindFields() {
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getTotalAcProperty(), totalAcTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getArmorBonusProperty(), armorBonusTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getShieldBonusProperty(), shieldBonusTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.attributes.getAttributeModProperty(AttributeName.DEXTERITY), dexModTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getNaturalArmorProperty(), naturalArmorTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getSizeModProperty(), sizeModTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getDeflectionBonusProperty(), deflectionBonusTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getMiscModProperty(), miscModTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getTouchAcProperty(), touchAcTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.armorClass.getFlatFootedAcProperty(), flatFootedAcTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.spellResistance.spellResistanceProperty(), spellResistanceTextField);
    }
}
