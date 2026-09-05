package dev.swim.toh.model.core.hitpoints;

import dev.swim.toh.model.core.InputBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HitPointsInput extends InputBase {

    private final IntegerProperty baseMaxHitPoints = new SimpleIntegerProperty(0);
    private final IntegerProperty currentHitPoints = new SimpleIntegerProperty(0);

    public IntegerProperty getBaseMaxHitPointsProperty() {
        return baseMaxHitPoints;
    }

    public IntegerProperty getCurrentHitPointsProperty() {
        return currentHitPoints;
    }
}
