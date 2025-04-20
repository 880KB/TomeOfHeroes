package dev.swim.toh.model.core.attributes;

import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.attribute.Attribute;
import javafx.beans.property.IntegerProperty;

public class Attributes extends CoreBase<AttributesInput, AttributesComputed> {

    public Attributes() {
        input = new AttributesInput();
        computed = new AttributesComputed();
    }

    public IntegerProperty getAttributeBaseProperty(Attribute attribute) {
        return input.getAttributeBaseProperty(attribute);
    }

    public IntegerProperty getAttributeModProperty(Attribute attribute) {
        return computed.getAttributeModProperty(attribute);
    }
}
