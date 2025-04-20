package dev.swim.toh.model.core.classes;

import dev.swim.toh.model.data.clazz.Clazz;
import javafx.beans.property.*;

public class ChosenClass {
    private final ObjectProperty<Clazz> clazz;
    private IntegerProperty level;
    private BooleanProperty isFirstClass;

    public ChosenClass(Clazz clazz, int level) {
        this(clazz, level, false);
    }

    public ChosenClass(Clazz clazz, int level, boolean isFirstClass) {
        this.clazz = new SimpleObjectProperty<>(clazz);
        this.level = new SimpleIntegerProperty(level);
        this.isFirstClass = new SimpleBooleanProperty(isFirstClass);
    }

    public ObjectProperty<Clazz> clazzProperty() {
        return clazz;
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public BooleanProperty isFirstClassProperty() {
        return isFirstClass;
    }
}
