package dev.swim.toh.model.rules;

import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;

import java.util.List;

public class ClassRules {

    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 20;

    public static boolean canAddClass(List<ChosenClass> classList, Clazz clazz) {
        return classList.stream()
                .noneMatch(chosenClass -> chosenClass.clazzProperty().get() == clazz);
    }

    public static boolean isValidLevel(int level) {
        return level >= MIN_LEVEL && level <= MAX_LEVEL;
    }
}
