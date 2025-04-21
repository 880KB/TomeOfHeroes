package dev.swim.toh.model.core.savingthrows;

import dev.swim.toh.model.calculation.SavingThrowCalculator;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.attribute.Attribute;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.savingthrow.SavingThrow;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;

import java.util.Map;

public class SavingThrowsComputed extends ComputedBase {

    private final Map<SavingThrow, IntegerProperty> savingThrowTotalPropertyMap;
    private final Map<SavingThrow, IntegerProperty> savingThrowBasePropertyMap;
    private final Map<SavingThrow, IntegerProperty> savingThrowAttributeModPropertyMap;
    private final Map<SavingThrow, IntegerProperty> savingThrowMagicModPropertyMap;

    public SavingThrowsComputed() {
        savingThrowTotalPropertyMap = Map.of(
                SavingThrow.FORTITUDE, new SimpleIntegerProperty(0),
                SavingThrow.REFLEX, new SimpleIntegerProperty(0),
                SavingThrow.WILL, new SimpleIntegerProperty(0)
        );
        savingThrowBasePropertyMap = Map.of(
                SavingThrow.FORTITUDE, new SimpleIntegerProperty(0),
                SavingThrow.REFLEX, new SimpleIntegerProperty(0),
                SavingThrow.WILL, new SimpleIntegerProperty(0)
        );
        savingThrowAttributeModPropertyMap = Map.of(
                SavingThrow.FORTITUDE, new SimpleIntegerProperty(0),
                SavingThrow.REFLEX, new SimpleIntegerProperty(0),
                SavingThrow.WILL, new SimpleIntegerProperty(0)
        );
        savingThrowMagicModPropertyMap = Map.of(
                SavingThrow.FORTITUDE, new SimpleIntegerProperty(0),
                SavingThrow.REFLEX, new SimpleIntegerProperty(0),
                SavingThrow.WILL, new SimpleIntegerProperty(0)
        );
    }

    public IntegerProperty getSavingThrowTotalProperty(SavingThrow savingThrow) {
        return savingThrowTotalPropertyMap.get(savingThrow);
    }

    public IntegerProperty getSavingThrowBaseProperty(SavingThrow savingThrow) {
        return savingThrowBasePropertyMap.get(savingThrow);
    }

    public IntegerProperty getSavingThrowAttributeModProperty(SavingThrow savingThrow) {
        return savingThrowAttributeModPropertyMap.get(savingThrow);
    }

    public IntegerProperty getSavingThrowMagicModProperty(SavingThrow savingThrow) {
        return savingThrowMagicModPropertyMap.get(savingThrow);
    }

    @Override
    public void addListeners() {
        for (SavingThrow savingThrow : SavingThrow.values()) {
            // Total Saving Throws
            character.savingThrows.getSavingThrowBaseProperty(savingThrow).addListener((obs, oldBase, newBase) ->
                    savingThrowTotalPropertyMap.get(savingThrow).set(SavingThrowCalculator.getSavingThrowTotal(character, savingThrow))
            );
            character.savingThrows.getSavingThrowAttributeModProperty(savingThrow).addListener((obs, oldAttributeMod, newAttributeMod) ->
                    savingThrowTotalPropertyMap.get(savingThrow).set(SavingThrowCalculator.getSavingThrowTotal(character, savingThrow))
            );
            character.savingThrows.getSavingThrowMagicModProperty(savingThrow).addListener((obs, oldMagicMod, newMagicMod) ->
                    savingThrowTotalPropertyMap.get(savingThrow).set(SavingThrowCalculator.getSavingThrowTotal(character, savingThrow))
            );
            character.savingThrows.getSavingThrowMiscModProperty(savingThrow).addListener((obs, oldMiscMod, newMiscMod) ->
                    savingThrowTotalPropertyMap.get(savingThrow).set(SavingThrowCalculator.getSavingThrowTotal(character, savingThrow))
            );
            // Attribute Mods
            Attribute attribute = SavingThrowCalculator.getAttribute(savingThrow);
            character.attributes.getAttributeModProperty(attribute).addListener((obs, oldAttributeMod, newAttributeMod) ->
                    savingThrowAttributeModPropertyMap.get(savingThrow).set(newAttributeMod.intValue())
            );
            // TODO: magic mods
        }
        // Base Saving Throws
        character.classes.getClassList().addListener((ListChangeListener<? super ChosenClass>) c -> {
                    while (c.next()) {
                        if (c.wasAdded()) {
                            for (ChosenClass chosenClass : c.getAddedSubList())
                                // add listener for level of added class
                                chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) -> updateSavingThrowBase());
                        }
                    }
                    // set base saving throws for all saving throws
                    updateSavingThrowBase();
                }
        );
    }

    private void updateSavingThrowBase() {
        for (SavingThrow savingThrow : SavingThrow.values())
            savingThrowBasePropertyMap.get(savingThrow).set(SavingThrowCalculator.getSavingThrowBase(character, savingThrow));
    }
}
