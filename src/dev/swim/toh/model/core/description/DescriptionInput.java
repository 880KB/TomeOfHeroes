package dev.swim.toh.model.core.description;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.race.Race;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DescriptionInput extends InputBase {

    private final ObjectProperty<Race> race = new SimpleObjectProperty<>(Race.HUMAN);
    private final StringProperty age = new SimpleStringProperty("20");
    private final StringProperty size = new SimpleStringProperty("170");

    public ObjectProperty<Race> raceProperty() {
        return race;
    }

    public StringProperty ageProperty() {
        return age;
    }

    public StringProperty sizeProperty() {
        return size;
    }
}
