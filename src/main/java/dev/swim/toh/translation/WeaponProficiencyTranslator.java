package dev.swim.toh.translation;

import dev.swim.toh.model.data.weapon.WeaponProficiency;

public class WeaponProficiencyTranslator {
    public static String toGerman(WeaponProficiency proficiency) {
        return switch (proficiency) {
            case SIMPLE -> "Einfache Waffen";
            case MARTIAL -> "Kriegswaffen";
            case EXOTIC -> "Exotische Waffen";
        };
    }
}
