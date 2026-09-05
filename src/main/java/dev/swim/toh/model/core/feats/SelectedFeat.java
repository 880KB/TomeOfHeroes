package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.data.feat.Feat;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SelectedFeat {

    private final ObjectProperty<Feat> featProperty;
    // options

    public SelectedFeat(Feat feat) {
        this.featProperty = new SimpleObjectProperty<>(feat);
    }

    public ObjectProperty<Feat> featProperty() {
        return featProperty;
    }
}
