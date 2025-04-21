package dev.swim.toh.model.core;

import dev.swim.toh.model.core.attributes.Attributes;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.core.classes.Classes;
import dev.swim.toh.model.core.description.Description;
import dev.swim.toh.model.core.savingthrows.SavingThrows;
import dev.swim.toh.model.data.attribute.Attribute;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.savingthrow.SavingThrow;

public class Character {

    public final Description description;
    public final Attributes attributes;
    public final SavingThrows savingThrows;
    public final Classes classes;

    public Character() {
        description = new Description();
        classes = new Classes();
        attributes = new Attributes();
        savingThrows = new SavingThrows();
        init();
    }

    public void init() {
        description.init(this);
        classes.init(this);
        attributes.init(this);
        savingThrows.init(this);

        // For testing purposes
        classes.addClass(Clazz.FIGHTER, 3, true);
        classes.addClass(Clazz.WIZARD, 2, false);
    }

    public void print() {
        System.out.println("-----");
        System.out.println("Character");
        for (Attribute attribute : Attribute.values())
            System.out.println(attribute + ": " + attributes.getAttributeBaseProperty(attribute).getValue() + " (" + attributes.getAttributeModProperty(attribute).getValue() + ")");
        for (ChosenClass chosenClass : classes.getClassList())
            System.out.println(chosenClass.clazzProperty().get() + " (" + chosenClass.levelProperty().get() + ")" + (chosenClass.isFirstClassProperty().get() ? " (First Class)" : ""));
        System.out.println(description.raceProperty().get());
        System.out.println(description.ageProperty().get());
        System.out.println(description.sizeProperty().get() + " (" + description.sizeCategoryProperty().get() + ")");
        for (SavingThrow savingThrow : SavingThrow.values())
            System.out.println(savingThrow + ": " + savingThrows.getSavingThrowTotalProperty(savingThrow).getValue());
        System.out.println("-----");
    }
}
