package dev.swim.toh.model.data.weapon;

// Only meaningful for MELEE weapons - null for RANGED ones, which the table lists in their own
// "Fernkampfwaffen" section without a handedness column.
public enum WeaponHandedness {
    LIGHT,
    ONE_HANDED,
    TWO_HANDED
}
