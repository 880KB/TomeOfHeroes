package dev.swim.toh.translation;

import dev.swim.toh.model.data.clazz.Clazz;

public class ClazzTranslator {
    public static String toGerman(Clazz clazz) {
        return switch (clazz) {
            case BARBARIAN -> "Barbar";
            case BARD -> "Barde";
            case CLERIC -> "Kleriker";
            case DRUID -> "Druide";
            case FIGHTER -> "Kämpfer";
            case MONK -> "Mönch";
            case PALADIN -> "Paladin";
            case RANGER -> "Waldläufer";
            case ROGUE -> "Schurke";
            case SORCERER -> "Hexenmeister";
            case WIZARD -> "Magier";
        };
    }
}
