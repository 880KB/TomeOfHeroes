package dev.swim.toh.model.calculation;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.attribute.AttributeName;

public class ArmorClassCalculator {

    public static int getTotalAc(CharacterModel characterModel) {
        return 10
                + characterModel.armorClass.getArmorBonusProperty().get()
                + characterModel.armorClass.getShieldBonusProperty().get()
                + getDexMod(characterModel)
                + characterModel.armorClass.getSizeModProperty().get()
                + characterModel.armorClass.getNaturalArmorProperty().get()
                + characterModel.armorClass.getDeflectionBonusProperty().get()
                + characterModel.armorClass.getMiscModProperty().get();
    }

    public static int getTouchAc(CharacterModel characterModel) {
        return 10
                + getDexMod(characterModel)
                + characterModel.armorClass.getSizeModProperty().get()
                + characterModel.armorClass.getDeflectionBonusProperty().get()
                + characterModel.armorClass.getMiscModProperty().get();
    }

    public static int getFlatFootedAc(CharacterModel characterModel) {
        return 10
                + characterModel.armorClass.getArmorBonusProperty().get()
                + characterModel.armorClass.getShieldBonusProperty().get()
                + characterModel.armorClass.getSizeModProperty().get()
                + characterModel.armorClass.getNaturalArmorProperty().get()
                + characterModel.armorClass.getDeflectionBonusProperty().get()
                + characterModel.armorClass.getMiscModProperty().get();
    }

    private static int getDexMod(CharacterModel characterModel) {
        return characterModel.attributes.getAttributeModProperty(AttributeName.DEXTERITY).get();
    }
}
