package dev.swim.toh.model.core.attributes;

import dev.swim.toh.model.calculation.AttributeCalculator;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.data.attribute.Attribute;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Map;

public class AttributesComputed extends ComputedBase {

    private final Map<Attribute, IntegerProperty> attributeModPropertyMap;

    public AttributesComputed() {
        attributeModPropertyMap = Map.of(
                Attribute.STRENGTH, new SimpleIntegerProperty(0),
                Attribute.DEXTERITY, new SimpleIntegerProperty(0),
                Attribute.CONSTITUTION, new SimpleIntegerProperty(0),
                Attribute.INTELLIGENCE, new SimpleIntegerProperty(0),
                Attribute.WISDOM, new SimpleIntegerProperty(0),
                Attribute.CHARISMA, new SimpleIntegerProperty(0)
        );
    }

    public IntegerProperty getAttributeModProperty(Attribute attribute) {
        return attributeModPropertyMap.get(attribute);
    }

    public void addListeners() {
        for (Attribute attribute : Attribute.values()) {
            character.attributes.getAttributeBaseProperty(attribute).addListener((obs, oldAttribute, newAttribute) ->
                    attributeModPropertyMap.get(attribute).set(AttributeCalculator.getAttributeMod(newAttribute.intValue()))
            );
        }
    }
}
