package dev.swim.toh.ui.controller;

import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.util.javafx.Bind;
import dev.swim.toh.model.util.javafx.Format;
import javafx.scene.control.TextField;

import java.util.Map;

public class AttributesViewController extends CharacterModelAware {

    public TextField strengthTextField;
    public TextField dexterityTextField;
    public TextField constitutionTextField;
    public TextField intelligenceTextField;
    public TextField wisdomTextField;
    public TextField charismaTextField;

    public TextField strengthModTextField;
    public TextField dexterityModTextField;
    public TextField constitutionModTextField;
    public TextField intelligenceModTextField;
    public TextField wisdomModTextField;
    public TextField charismaModTextField;

    private Map<AttributeName, TextField> attributeTextFieldMap;
    private Map<AttributeName, TextField> attributeModTextFieldMap;

    protected void bindFields() {
        attributeTextFieldMap = Map.of(
                AttributeName.STRENGTH, strengthTextField,
                AttributeName.DEXTERITY, dexterityTextField,
                AttributeName.CONSTITUTION, constitutionTextField,
                AttributeName.INTELLIGENCE, intelligenceTextField,
                AttributeName.WISDOM, wisdomTextField,
                AttributeName.CHARISMA, charismaTextField
        );
        attributeModTextFieldMap = Map.of(
                AttributeName.STRENGTH, strengthModTextField,
                AttributeName.DEXTERITY, dexterityModTextField,
                AttributeName.CONSTITUTION, constitutionModTextField,
                AttributeName.INTELLIGENCE, intelligenceModTextField,
                AttributeName.WISDOM, wisdomModTextField,
                AttributeName.CHARISMA, charismaModTextField
        );

        for (AttributeName attributeName : AttributeName.values()) {
            Bind.bindIntegerPropertyToTextField(characterModel.attributes.getAttributeBaseProperty(attributeName),
                    attributeTextFieldMap.get(attributeName),
                    Format.getAttributeIntegerFilter());
            Bind.bindIntegerPropertyToTextField(characterModel.attributes.getAttributeModProperty(attributeName),
                    attributeModTextFieldMap.get(attributeName),
                    Format.getIntegerFilter());
        }
    }
}
