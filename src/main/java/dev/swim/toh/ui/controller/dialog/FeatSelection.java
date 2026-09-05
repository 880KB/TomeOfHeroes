package dev.swim.toh.ui.controller.dialog;

import dev.swim.toh.model.data.feat.Feat;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FeatSelection {
    private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    private final ObjectProperty<Feat> featProperty;

    public FeatSelection(Feat feat) {
        this.featProperty = new SimpleObjectProperty<>(feat);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public ObjectProperty<Feat> featProperty() {
        return featProperty;
    }
}
