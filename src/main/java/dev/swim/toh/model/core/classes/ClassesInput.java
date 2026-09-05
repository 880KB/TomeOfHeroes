package dev.swim.toh.model.core.classes;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.rules.ClassRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Stream;

class ClassesInput extends InputBase {

    private final ObservableList<ChosenClass> classList = FXCollections.observableArrayList();

    protected ObservableList<ChosenClass> getClassList() {
        return classList;
    }

    protected void addClass(Clazz clazz, int level) {
        addClass(clazz, level, !hasFirstClass());
    }

    protected void addClass(Clazz clazz, int level, boolean isFirstClass) {
        if (ClassRules.canAddClass(classList, clazz)) {
            ChosenClass chosenClass = new ChosenClass(clazz, level, isFirstClass);
            enforceExactlyOneFirstClass(chosenClass);
            classList.add(chosenClass);
        }
    }

    /**
     * Keeps "first class" acting like a radio button: marking one class as first unmarks every
     * other class, and unmarking the only first class immediately re-marks it (there is always
     * exactly one first class as long as the list isn't empty) - this also makes the checkbox in
     * the UI behave correctly without the view needing to know about this rule.
     */
    private void enforceExactlyOneFirstClass(ChosenClass chosenClass) {
        chosenClass.isFirstClassProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                for (ChosenClass other : classList)
                    if (other != chosenClass)
                        other.isFirstClassProperty().set(false);
            } else if (!classList.isEmpty() && classList.stream().noneMatch(c -> c.isFirstClassProperty().get())) {
                classList.getFirst().isFirstClassProperty().set(true);
            }
        });
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
        return Stream.of(Clazz.values())
                .filter(clazz -> !this.hasClass(clazz))
                .toList();
    }
    protected boolean hasFirstClass() {
        return classList.stream()
                .anyMatch(chosenClass -> chosenClass.isFirstClassProperty().get());
    }
}
