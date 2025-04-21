package dev.swim.toh.model.core.classes;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.rules.clazz.ClassRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

class ClassesInput extends InputBase {

    private final ObservableList<ChosenClass> classList = FXCollections.observableArrayList();

    protected ObservableList<ChosenClass> getClassList() {
        return classList;
    }

    protected void addClass(Clazz clazz, int level) {
        addClass(clazz, level, !hasFirstClass());
    }

    protected void addClass(Clazz clazz, int level, boolean isFirstClass) {
        if (ClassRules.canAddClass(classList, clazz))
            classList.add(new ChosenClass(clazz, level, isFirstClass));
    }

    protected void removeClass(ChosenClass chosenClass) {
        if (chosenClass != null) {
            classList.remove(chosenClass);
            // select another class as the first class
            if (chosenClass.isFirstClassProperty().get() && !classList.isEmpty())
                classList.getFirst().isFirstClassProperty().set(true);
        }
    }

    protected boolean hasClass(Clazz clazz) {
        return classList.stream()
                .anyMatch(chosenClass -> chosenClass.clazzProperty().get() == clazz);
    }

    protected List<Clazz> getAvailableClasses() {
        return Arrays.stream(Clazz.values())
                .filter(clazz -> !this.hasClass(clazz))
                .toList();
    }
    protected boolean hasFirstClass() {
        return classList.stream()
                .anyMatch(chosenClass -> chosenClass.isFirstClassProperty().get());
    }
}
