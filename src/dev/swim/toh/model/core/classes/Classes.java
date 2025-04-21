package dev.swim.toh.model.core.classes;

import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.clazz.Clazz;
import javafx.collections.ObservableList;

import java.util.List;

public class Classes extends CoreBase<ClassesInput, ClassesComputed> {
    public Classes() {
        input = new ClassesInput();
        computed = new ClassesComputed();
    }

    public ObservableList<ChosenClass> getClassList() {
        return input.getClassList();
    }

    public void addClass(Clazz clazz, int level) {
        input.addClass(clazz, level);
    }

    public void addClass(Clazz clazz, int level, boolean isFirstClass) {
        input.addClass(clazz, level, isFirstClass);
    }

    public void removeClass(ChosenClass chosenClass) {
        input.removeClass(chosenClass);
    }

    public boolean hasClass(Clazz clazz) {
        return input.hasClass(clazz);
    }

    public List<Clazz> getAvailableClasses() {
        return input.getAvailableClasses();
    }
}
