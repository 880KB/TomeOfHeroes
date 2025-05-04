package dev.swim.toh.model.core.attributes;

import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.attribute.AttributeName;
import javafx.beans.property.IntegerProperty;

public class Attributes extends CoreBase<AttributesInput, AttributesComputed> {

    public Attributes() {
        input = new AttributesInput();
        computed = new AttributesComputed();
    }

    public IntegerProperty getAttributeBaseProperty(AttributeName attributeName) {
        return input.getAttributeBaseProperty(attributeName);
    }

    public IntegerProperty getAttributeModProperty(AttributeName attributeName) {
        return computed.getAttributeModProperty(attributeName);
    }
}
