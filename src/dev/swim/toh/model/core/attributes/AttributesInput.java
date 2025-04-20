package dev.swim.toh.model.core.attributes;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.attribute.Attribute;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Map;

public class AttributesInput extends InputBase {

    private final Map<Attribute, IntegerProperty> attributeBasePropertyMap;

    public AttributesInput() {
        attributeBasePropertyMap = Map.of(
                Attribute.STRENGTH, new SimpleIntegerProperty(10),
                Attribute.DEXTERITY, new SimpleIntegerProperty(10),
                Attribute.CONSTITUTION, new SimpleIntegerProperty(10),
                Attribute.INTELLIGENCE, new SimpleIntegerProperty(10),
                Attribute.WISDOM, new SimpleIntegerProperty(10),
                Attribute.CHARISMA, new SimpleIntegerProperty(10)
        );
    }

    public IntegerProperty getAttributeBaseProperty(Attribute attribute) {
        return attributeBasePropertyMap.get(attribute);
    }
}
