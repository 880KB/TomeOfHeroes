package dev.swim.toh.model.core.attributes;

import dev.swim.toh.model.calculation.AttributeCalculator;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.data.attribute.AttributeName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Map;

public class AttributesComputed extends ComputedBase {

    private final Map<AttributeName, IntegerProperty> attributeModPropertyMap;

    public AttributesComputed() {
        attributeModPropertyMap = Map.of(
                AttributeName.STRENGTH, new SimpleIntegerProperty(0),
                AttributeName.DEXTERITY, new SimpleIntegerProperty(0),
                AttributeName.CONSTITUTION, new SimpleIntegerProperty(0),
                AttributeName.INTELLIGENCE, new SimpleIntegerProperty(0),
                AttributeName.WISDOM, new SimpleIntegerProperty(0),
                AttributeName.CHARISMA, new SimpleIntegerProperty(0)
        );
    }

    public IntegerProperty getAttributeModProperty(AttributeName attributeName) {
        return attributeModPropertyMap.get(attributeName);
    }

    public void addListeners() {
        for (AttributeName attributeName : AttributeName.values()) {
            characterModel.attributes.getAttributeBaseProperty(attributeName).addListener((obs, oldAttribute, newAttribute) ->
                    attributeModPropertyMap.get(attributeName).set(AttributeCalculator.getAttributeMod(newAttribute.intValue()))
            );
        }
    }
}
