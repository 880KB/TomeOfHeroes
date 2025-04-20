package dev.swim.toh.ui.controller;

import dev.swim.toh.model.data.attribute.Attribute;
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

    private Map<Attribute, TextField> attributeTextFieldMap;
    private Map<Attribute, TextField> attributeModTextFieldMap;

    protected void bindFields() {
        attributeTextFieldMap = Map.of(
                Attribute.STRENGTH, strengthTextField,
                Attribute.DEXTERITY, dexterityTextField,
                Attribute.CONSTITUTION, constitutionTextField,
                Attribute.INTELLIGENCE, intelligenceTextField,
                Attribute.WISDOM, wisdomTextField,
                Attribute.CHARISMA, charismaTextField
        );
        attributeModTextFieldMap = Map.of(
                Attribute.STRENGTH, strengthModTextField,
                Attribute.DEXTERITY, dexterityModTextField,
                Attribute.CONSTITUTION, constitutionModTextField,
                Attribute.INTELLIGENCE, intelligenceModTextField,
                Attribute.WISDOM, wisdomModTextField,
                Attribute.CHARISMA, charismaModTextField
        );

        for (Attribute attribute : Attribute.values()) {
            Bind.bindIntegerPropertyToTextField(characterModel.attributes.getAttributeBaseProperty(attribute),
                    attributeTextFieldMap.get(attribute),
                    Format.getAttributeIntegerFilter());
            Bind.bindIntegerPropertyToTextField(characterModel.attributes.getAttributeModProperty(attribute),
                    attributeModTextFieldMap.get(attribute),
                    Format.getIntegerFilter());
        }
    }
}
