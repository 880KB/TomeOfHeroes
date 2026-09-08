package dev.swim.toh.model.core.weapons;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.weapon.Weapon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class WeaponsInput extends InputBase {

    // exactly one row per WeaponSlot, for the lifetime of the character - slots are filled or
    // cleared, never added or removed, so unlike FeatsInput.featList this needs no extractor
    private final ObservableList<SelectedWeaponSlot> slots = FXCollections.observableArrayList(
            Arrays.stream(WeaponSlot.values()).map(SelectedWeaponSlot::new).toList());

    protected ObservableList<SelectedWeaponSlot> getSlots() {
        return slots;
    }

    protected SelectedWeaponSlot getSlot(WeaponSlot slot) {
        return slots.stream()
                .filter(selectedWeaponSlot -> selectedWeaponSlot.getSlot() == slot)
                .findFirst()
                .orElseThrow();
    }

    protected void setWeapon(WeaponSlot slot, Weapon weapon) {
        getSlot(slot).weaponProperty().set(weapon);
    }
}
