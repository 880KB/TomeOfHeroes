package dev.swim.toh.model.core.armorclass;

import dev.swim.toh.model.core.CoreBase;
import javafx.beans.property.IntegerProperty;

public class ArmorClass extends CoreBase<ArmorClassInput, ArmorClassComputed> {

    public ArmorClass() {
        input = new ArmorClassInput();
        computed = new ArmorClassComputed();
    }

    public IntegerProperty getArmorBonusProperty() {
        return input.getArmorBonusProperty();
    }

    public IntegerProperty getShieldBonusProperty() {
        return input.getShieldBonusProperty();
    }

    public IntegerProperty getNaturalArmorProperty() {
        return input.getNaturalArmorProperty();
    }

    public IntegerProperty getDeflectionBonusProperty() {
        return input.getDeflectionBonusProperty();
    }

    public IntegerProperty getMiscModProperty() {
        return input.getMiscModProperty();
    }

    public IntegerProperty getSizeModProperty() {
        return computed.getSizeModProperty();
    }

    public IntegerProperty getTotalAcProperty() {
        return computed.getTotalAcProperty();
    }

    public IntegerProperty getTouchAcProperty() {
        return computed.getTouchAcProperty();
    }

    public IntegerProperty getFlatFootedAcProperty() {
        return computed.getFlatFootedAcProperty();
    }
}
