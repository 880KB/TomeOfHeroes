package dev.swim.toh.model.rules;

import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;

import java.util.List;

public class ClassRules {

    public static boolean canAddClass(List<ChosenClass> classList, Clazz clazz) {
        return classList.stream()
                .noneMatch(chosenClass -> chosenClass.clazzProperty().get() == clazz);
    }

    public static boolean isValidLevel(int level) {
        return level >= 1 && level <= 20;
    }
}
