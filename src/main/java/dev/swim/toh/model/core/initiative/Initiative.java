package dev.swim.toh.model.core.initiative;

import dev.swim.toh.model.core.CoreBase;
import javafx.beans.property.IntegerProperty;

public class Initiative extends CoreBase<InitiativeInput, InitiativeComputed> {

    public Initiative() {
        input = new InitiativeInput();
        computed = new InitiativeComputed();
    }

    public IntegerProperty getMiscModProperty() {
        return input.getMiscModProperty();
    }

    public IntegerProperty getTotalProperty() {
        return computed.getTotalProperty();
    }
}
