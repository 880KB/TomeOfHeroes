package dev.swim.toh.translation;

import dev.swim.toh.model.core.weapons.WeaponSlot;

public class WeaponSlotTranslator {
    public static String toGerman(WeaponSlot slot) {
        return switch (slot) {
            case MAIN_HAND -> "Haupthand";
            case OFF_HAND -> "Nebenhand";
            case RANGED -> "Fernkampf";
        };
    }
}
