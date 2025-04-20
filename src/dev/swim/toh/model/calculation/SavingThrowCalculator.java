package dev.swim.toh.model.calculation;

import dev.swim.toh.model.core.Character;
import dev.swim.toh.model.data.attribute.Attribute;
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
        int total = character.savingThrows.getSavingThrowBaseProperty(savingThrow).intValue() +
                character.savingThrows.getSavingThrowAttributeModProperty(savingThrow).intValue() +
                character.savingThrows.getSavingThrowMagicModProperty(savingThrow).intValue() +
                character.savingThrows.getSavingThrowMiscModProperty(savingThrow).intValue();
        return Math.max(1, total);
    }
}
