package dev.swim.toh.model.core.initiative;

import dev.swim.toh.model.calculation.InitiativeCalculator;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.data.attribute.AttributeName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InitiativeComputed extends ComputedBase {

    private final IntegerProperty total = new SimpleIntegerProperty(0);

    public IntegerProperty getTotalProperty() {
        return total;
    }

    @Override
    public void addListeners() {
        characterModel.attributes.getAttributeModProperty(AttributeName.DEXTERITY).addListener((obs, oldMod, newMod) -> updateTotal());
        characterModel.initiative.getMiscModProperty().addListener((obs, oldValue, newValue) -> updateTotal());
        updateTotal();
    }

    private void updateTotal() {
        total.set(InitiativeCalculator.getInitiative(characterModel));
    }
}
