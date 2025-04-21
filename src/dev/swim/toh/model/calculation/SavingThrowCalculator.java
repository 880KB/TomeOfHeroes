package dev.swim.toh.model.calculation;

import dev.swim.toh.model.core.Character;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.attribute.Attribute;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.savingthrow.SavingThrow;

public class SavingThrowCalculator {

    public static Attribute getAttribute(SavingThrow savingThrow) {
        return switch (savingThrow) {
            case REFLEX -> Attribute.DEXTERITY;
            case WILL -> Attribute.WISDOM;
            case FORTITUDE -> Attribute.CONSTITUTION;
        };
    }

    public static int getSavingThrowTotal(Character character, SavingThrow savingThrow) {
        return character.savingThrows.getSavingThrowBaseProperty(savingThrow).intValue() +
                character.savingThrows.getSavingThrowAttributeModProperty(savingThrow).intValue() +
                character.savingThrows.getSavingThrowMagicModProperty(savingThrow).intValue() +
                character.savingThrows.getSavingThrowMiscModProperty(savingThrow).intValue();
    }

    public static int getSavingThrowBase(Character character, SavingThrow savingThrow) {
        int result = 0;
        for (ChosenClass chosenClass : character.classes.getClassList()) {
            int level = chosenClass.levelProperty().get();
            Clazz clazz = chosenClass.clazzProperty().get();
            if (isGood(clazz, savingThrow))
                result += level / 2 + 2;
            else
                result += level / 3;
        }
        return result;
    }

    private static boolean isGood(Clazz clazz, SavingThrow savingThrow) {
        switch (savingThrow) {
            case FORTITUDE -> {
                switch (clazz) {
                    case BARBARIAN, CLERIC, DRUID, FIGHTER, MONK, PALADIN, RANGER -> {
                        return true;
                    }
                }
            }
            case REFLEX -> {
                switch (clazz) {
                    case BARD, MONK, RANGER, ROGUE -> {
                        return true;
                    }
                }
            }
            case WILL -> {
                switch (clazz) {
                    case BARD, CLERIC, DRUID, MONK, SORCERER, WIZARD -> {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
