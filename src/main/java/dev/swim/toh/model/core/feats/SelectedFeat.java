package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.calculation.FeatSlotCalculator.FeatPool;
import dev.swim.toh.model.data.feat.Feat;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SelectedFeat {

    private final ObjectProperty<Feat> featProperty;
    // which feat-slot pool this selection consumed, decided once when it was added and frozen
    // from then on - null for a feat that hasn't been assigned yet (a brief transient state
    // between creation and assignment) or one that never competes for a slot (automatic grants)
    private final ObjectProperty<FeatPool> poolProperty = new SimpleObjectProperty<>();
    // options

    public SelectedFeat(Feat feat) {
        this.featProperty = new SimpleObjectProperty<>(feat);
    }

    public ObjectProperty<Feat> featProperty() {
        return featProperty;
    }

    public ObjectProperty<FeatPool> poolProperty() {
        return poolProperty;
    }

    public FeatPool getPool() {
        return poolProperty.get();
    }
}
