package dev.swim.toh.model.core.description;

import dev.swim.toh.model.calculation.SizeCalculator;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.data.size.Size;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DescriptionComputed extends ComputedBase {

    private final ObjectProperty<Size> sizeCategory = new SimpleObjectProperty<>(Size.MEDIUM);

    public void addListeners() {
        character.description.raceProperty().addListener((obs, oldRace, newRace) ->
                sizeCategory.set(SizeCalculator.getSize(character.description.raceProperty().get()))
        );
        sizeCategory.set(character.description.sizeCategoryProperty().get());
    }

    public ObjectProperty<Size> sizeCategoryProperty() {
        return sizeCategory;
    }
}
