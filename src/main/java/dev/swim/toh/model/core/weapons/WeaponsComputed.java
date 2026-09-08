package dev.swim.toh.model.core.weapons;

import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.data.weapon.Weapon;
import dev.swim.toh.model.data.weapon.WeaponHandedness;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class WeaponsComputed extends ComputedBase {

    private final BooleanProperty offHandLocked = new SimpleBooleanProperty(false);

    @Override
    public void addListeners() {
        characterModel.weapons.weaponProperty(WeaponSlot.MAIN_HAND)
                .addListener((obs, oldWeapon, newWeapon) -> updateOffHandLock(newWeapon));
        updateOffHandLock(characterModel.weapons.weaponProperty(WeaponSlot.MAIN_HAND).get());
    }

    /**
     * A two-handed main-hand weapon leaves no hand free for an off-hand weapon - clear it right
     * away rather than just flagging it, since a stale off-hand selection would otherwise keep
     * showing (and saving) a weapon the character can no longer actually be wielding.
     */
    private void updateOffHandLock(Weapon mainHandWeapon) {
        boolean locked = mainHandWeapon != null && mainHandWeapon.getHandedness() == WeaponHandedness.TWO_HANDED;
        offHandLocked.set(locked);
        if (locked)
            characterModel.weapons.setWeapon(WeaponSlot.OFF_HAND, (Weapon) null);
    }

    protected BooleanProperty offHandLockedProperty() {
        return offHandLocked;
    }
}
