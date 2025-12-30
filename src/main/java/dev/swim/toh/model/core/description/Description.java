package dev.swim.toh.model.core.description;

import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.race.Race;
import dev.swim.toh.model.data.size.Size;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public class Description extends CoreBase<DescriptionInput, DescriptionComputed> {

    public Description() {
        input = new DescriptionInput();
        computed = new DescriptionComputed();
    }

    public ObjectProperty<Race> raceProperty() {
        return input.raceProperty();
    }

    public StringProperty ageProperty() {
        return input.ageProperty();
    }

    public StringProperty sizeProperty() {
        return input.sizeProperty();
    }

    public ObjectProperty<Size> sizeCategoryProperty() {
        return computed.sizeCategoryProperty();
    }
}
