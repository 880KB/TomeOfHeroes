package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.data.feat.FeatName;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ChosenFeat {
    private final ObjectProperty<FeatName> name;
    // options

    public ChosenFeat(FeatName name) {
        this.name = new SimpleObjectProperty<>(name);
    }

    public ObjectProperty<FeatName> nameProperty() {
        return name;
    }
}
