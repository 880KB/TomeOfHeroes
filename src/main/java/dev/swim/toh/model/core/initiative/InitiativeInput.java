package dev.swim.toh.model.core.initiative;

import dev.swim.toh.model.core.InputBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InitiativeInput extends InputBase {

    private final IntegerProperty miscMod = new SimpleIntegerProperty(0);

    public IntegerProperty getMiscModProperty() {
        return miscMod;
    }
}
