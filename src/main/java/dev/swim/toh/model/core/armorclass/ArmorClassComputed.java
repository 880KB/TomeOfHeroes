package dev.swim.toh.model.core.armorclass;

import dev.swim.toh.model.calculation.ArmorClassCalculator;
import dev.swim.toh.model.calculation.SizeCalculator;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.data.attribute.AttributeName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ArmorClassComputed extends ComputedBase {

    private final IntegerProperty sizeMod = new SimpleIntegerProperty(0);
    private final IntegerProperty totalAc = new SimpleIntegerProperty(0);
    private final IntegerProperty touchAc = new SimpleIntegerProperty(0);
    private final IntegerProperty flatFootedAc = new SimpleIntegerProperty(0);

    public IntegerProperty getSizeModProperty() {
        return sizeMod;
    }

    public IntegerProperty getTotalAcProperty() {
        return totalAc;
    }

    public IntegerProperty getTouchAcProperty() {
        return touchAc;
    }

    public IntegerProperty getFlatFootedAcProperty() {
        return flatFootedAc;
    }

    @Override
    public void addListeners() {
        characterModel.description.sizeCategoryProperty().addListener((obs, oldSize, newSize) -> {
            sizeMod.set(SizeCalculator.getAcModifier(newSize));
            updateAc();
        });
        sizeMod.set(SizeCalculator.getAcModifier(characterModel.description.sizeCategoryProperty().get()));

        characterModel.attributes.getAttributeModProperty(AttributeName.DEXTERITY).addListener((obs, oldMod, newMod) -> updateAc());
        characterModel.armorClass.getArmorBonusProperty().addListener((obs, oldValue, newValue) -> updateAc());
        characterModel.armorClass.getShieldBonusProperty().addListener((obs, oldValue, newValue) -> updateAc());
        characterModel.armorClass.getNaturalArmorProperty().addListener((obs, oldValue, newValue) -> updateAc());
        characterModel.armorClass.getDeflectionBonusProperty().addListener((obs, oldValue, newValue) -> updateAc());
        characterModel.armorClass.getMiscModProperty().addListener((obs, oldValue, newValue) -> updateAc());

        updateAc();
    }

    private void updateAc() {
        totalAc.set(ArmorClassCalculator.getTotalAc(characterModel));
        touchAc.set(ArmorClassCalculator.getTouchAc(characterModel));
        flatFootedAc.set(ArmorClassCalculator.getFlatFootedAc(characterModel));
    }
}
