package dev.swim.toh.model.core.spellresistance;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SpellResistance {

    private final IntegerProperty spellResistance = new SimpleIntegerProperty(0);

    public IntegerProperty spellResistanceProperty() {
        return spellResistance;
    }
}
