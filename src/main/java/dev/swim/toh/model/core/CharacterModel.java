package dev.swim.toh.model.core;

import dev.swim.toh.model.core.attributes.Attributes;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.core.classes.Classes;
import dev.swim.toh.model.core.description.Description;
import dev.swim.toh.model.core.feats.Feats;
import dev.swim.toh.model.core.savingthrows.SavingThrows;
import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.feat.FeatName;
import dev.swim.toh.model.data.savingthrow.SavingThrow;
import org.springframework.stereotype.Component;

@Component
public class CharacterModel {

    public final Description description = new Description();
    public final Attributes attributes = new Attributes();
    public final SavingThrows savingThrows = new SavingThrows();
    public final Classes classes  = new Classes();
    public final Feats feats;

    public CharacterModel(Feats feats) {
        description.init(this);
        classes.init(this);
        attributes.init(this);
        savingThrows.init(this);
        this.feats = feats;
        feats.init(this);

        initTestData();
    }

    public void initTestData() {
        classes.addClass(Clazz.FIGHTER, 3, true);
        classes.addClass(Clazz.WIZARD, 2, false);
        feats.addFeat(FeatName.ALERTNESS);
    }

    public void print() {
        System.out.println("-----");
        System.out.println("Character");
        for (AttributeName attributeName : AttributeName.values())
            System.out.println(attributeName + ": " + attributes.getAttributeBaseProperty(attributeName).getValue() + " (" + attributes.getAttributeModProperty(attributeName).getValue() + ")");
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
