package dev.swim.toh.model.core.classes;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.rules.clazz.ClassRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClassesInput extends InputBase {

    private final ObservableList<ChosenClass> classList = FXCollections.observableArrayList();

    protected ObservableList<ChosenClass> getClassList() {
        return classList;
    }

    protected void addClass(Clazz clazz, int level) {
        if (ClassRules.canAddClass(classList, clazz))
            classList.add(new ChosenClass(clazz, level));
    }
}
