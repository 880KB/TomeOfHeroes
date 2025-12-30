package dev.swim.toh.model.calculation;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.savingthrow.SavingThrow;

public class SavingThrowCalculator {

    public static AttributeName getAttribute(SavingThrow savingThrow) {
        return switch (savingThrow) {
            case REFLEX -> AttributeName.DEXTERITY;
            case WILL -> AttributeName.WISDOM;
            case FORTITUDE -> AttributeName.CONSTITUTION;
        };
    }

    public static int getSavingThrowTotal(CharacterModel characterModel, SavingThrow savingThrow) {
        return characterModel.savingThrows.getSavingThrowBaseProperty(savingThrow).intValue() +
                characterModel.savingThrows.getSavingThrowAttributeModProperty(savingThrow).intValue() +
                characterModel.savingThrows.getSavingThrowMagicModProperty(savingThrow).intValue() +
                characterModel.savingThrows.getSavingThrowMiscModProperty(savingThrow).intValue();
    }

    public static int getSavingThrowBase(CharacterModel characterModel, SavingThrow savingThrow) {
        int result = 0;
        for (ChosenClass chosenClass : characterModel.classes.getClassList()) {
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
