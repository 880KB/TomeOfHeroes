package dev.swim.toh.translation;

import dev.swim.toh.model.data.weapon.DamageType;

public class DamageTypeTranslator {
    public static String toGerman(DamageType damageType) {
        return switch (damageType) {
            case BLUDGEONING -> "Wucht";
            case PIERCING -> "Stich";
            case SLASHING -> "Hieb";
        };
    }
}
