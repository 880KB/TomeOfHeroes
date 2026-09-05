package dev.swim.toh.model.core.hitpoints;

import dev.swim.toh.model.core.CoreBase;
import javafx.beans.property.IntegerProperty;

public class HitPoints extends CoreBase<HitPointsInput, HitPointsComputed> {

    public HitPoints() {
        input = new HitPointsInput();
        computed = new HitPointsComputed();
    }

    public IntegerProperty getBaseMaxHitPointsProperty() {
        return input.getBaseMaxHitPointsProperty();
    }

    public IntegerProperty getCurrentHitPointsProperty() {
        return input.getCurrentHitPointsProperty();
    }

    public IntegerProperty getTotalMaxHitPointsProperty() {
        return computed.getTotalMaxHitPointsProperty();
    }
}
