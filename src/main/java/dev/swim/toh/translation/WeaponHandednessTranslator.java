package dev.swim.toh.translation;

import dev.swim.toh.model.data.weapon.WeaponHandedness;

public class WeaponHandednessTranslator {
    public static String toGerman(WeaponHandedness handedness) {
        return switch (handedness) {
            case LIGHT -> "Leicht";
            case ONE_HANDED -> "Einhand";
            case TWO_HANDED -> "Zweihand";
        };
    }
}
