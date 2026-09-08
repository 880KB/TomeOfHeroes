package dev.swim.toh.model.core.weapons;

import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.weapon.Weapon;
import dev.swim.toh.model.data.weapon.WeaponRangeType;
import dev.swim.toh.model.data.weapon.WeaponRepository;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.List;

public class Weapons extends CoreBase<WeaponsInput, WeaponsComputed> {

    private final WeaponRepository weaponRepository;

    public Weapons(WeaponRepository weaponRepository) {
        this.weaponRepository = weaponRepository;
        this.input = new WeaponsInput();
        this.computed = new WeaponsComputed();
    }

    public ObservableList<SelectedWeaponSlot> getSlots() {
        return input.getSlots();
    }

    public ObjectProperty<Weapon> weaponProperty(WeaponSlot slot) {
        return input.getSlot(slot).weaponProperty();
    }

    public StringProperty noteProperty(WeaponSlot slot) {
        return input.getSlot(slot).noteProperty();
    }

    public void setWeapon(WeaponSlot slot, Weapon weapon) {
        input.setWeapon(slot, weapon);
    }

    /**
     * For restoring a saved character - id is null for a slot that was empty when saved.
     */
    public void setWeapon(WeaponSlot slot, String weaponId) {
        input.setWeapon(slot, weaponId == null ? null : weaponRepository.getWeapon(weaponId));
    }

    public String getWeaponId(WeaponSlot slot) {
        Weapon weapon = weaponProperty(slot).get();
        return weapon == null ? null : weapon.getId();
    }

    public BooleanProperty offHandLockedProperty() {
        return computed.offHandLockedProperty();
    }

    /**
     * The catalog offered when filling a given slot - main-/off-hand only make sense with a
     * melee weapon, the ranged slot only with a ranged one. Sorted the same way the PHB table
     * groups weapons (proficiency, then - for melee - handedness, both enums declared in that
     * same order so their natural ordinal ordering already matches the book) before falling back
     * to alphabetical, so the groups a player expects from the table stay visually clustered.
     * Hides WeaponRepository from callers, same as Feats hides FeatRepository.
     */
    public List<Weapon> getWeaponsByRangeType(WeaponRangeType rangeType) {
        return weaponRepository.getAll().stream()
                .filter(weapon -> weapon.getRangeType() == rangeType)
                .sorted(Comparator.comparing(Weapon::getProficiency)
                        .thenComparing(Weapon::getHandedness, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Weapon::getName))
                .toList();
    }
}
