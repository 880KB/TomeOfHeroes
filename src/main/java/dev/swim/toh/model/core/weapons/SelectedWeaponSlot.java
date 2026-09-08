package dev.swim.toh.model.core.weapons;

import dev.swim.toh.model.data.weapon.Weapon;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SelectedWeaponSlot {

    // fixed at construction - unlike SelectedFeat's pool, a slot never changes which one it is
    private final WeaponSlot slot;
    private final ObjectProperty<Weapon> weaponProperty = new SimpleObjectProperty<>();
    private final StringProperty noteProperty = new SimpleStringProperty("");

    public SelectedWeaponSlot(WeaponSlot slot) {
        this.slot = slot;
    }

    public WeaponSlot getSlot() {
        return slot;
    }

    public ObjectProperty<Weapon> weaponProperty() {
        return weaponProperty;
    }

    public StringProperty noteProperty() {
        return noteProperty;
    }
}
