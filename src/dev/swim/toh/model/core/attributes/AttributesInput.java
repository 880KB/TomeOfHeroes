package dev.swim.toh.model.core.attributes;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.attribute.AttributeName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Map;

public class AttributesInput extends InputBase {

    private final Map<AttributeName, IntegerProperty> attributeBasePropertyMap;

    public AttributesInput() {
        attributeBasePropertyMap = Map.of(
                AttributeName.STRENGTH, new SimpleIntegerProperty(10),
                AttributeName.DEXTERITY, new SimpleIntegerProperty(10),
                AttributeName.CONSTITUTION, new SimpleIntegerProperty(10),
                AttributeName.INTELLIGENCE, new SimpleIntegerProperty(10),
                AttributeName.WISDOM, new SimpleIntegerProperty(10),
                AttributeName.CHARISMA, new SimpleIntegerProperty(10)
        );
    }

    public IntegerProperty getAttributeBaseProperty(AttributeName attributeName) {
        return attributeBasePropertyMap.get(attributeName);
    }
}
