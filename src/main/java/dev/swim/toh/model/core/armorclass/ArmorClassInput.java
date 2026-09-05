package dev.swim.toh.model.core.armorclass;

import dev.swim.toh.model.core.InputBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ArmorClassInput extends InputBase {

    private final IntegerProperty armorBonus = new SimpleIntegerProperty(0);
    private final IntegerProperty shieldBonus = new SimpleIntegerProperty(0);
    private final IntegerProperty naturalArmor = new SimpleIntegerProperty(0);
    private final IntegerProperty deflectionBonus = new SimpleIntegerProperty(0);
    private final IntegerProperty miscMod = new SimpleIntegerProperty(0);

    public IntegerProperty getArmorBonusProperty() {
        return armorBonus;
    }

    public IntegerProperty getShieldBonusProperty() {
        return shieldBonus;
    }

    public IntegerProperty getNaturalArmorProperty() {
        return naturalArmor;
    }

    public IntegerProperty getDeflectionBonusProperty() {
        return deflectionBonus;
    }

    public IntegerProperty getMiscModProperty() {
        return miscMod;
    }
}
