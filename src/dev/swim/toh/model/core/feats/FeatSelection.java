package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.data.feat.Feat;
import javafx.beans.property.SimpleBooleanProperty;

public class FeatSelection {
    private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    private final Feat feat;

    public FeatSelection(Feat feat) {
        this.feat = feat;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public Feat getFeat() {
        return feat;
    }
}
