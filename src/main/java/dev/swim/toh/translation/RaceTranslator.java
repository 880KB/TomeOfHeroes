package dev.swim.toh.translation;

import dev.swim.toh.model.data.race.Race;

public class RaceTranslator {
    public static String toGerman(Race race) {
        return switch (race) {
            case HUMAN -> "Mensch";
            case ELF -> "Elf";
            case DWARF -> "Zwerg";
            case HALFLING -> "Halbling";
            case GNOME -> "Gnom";
            case HALF_ORC -> "Halbork";
            case HALF_ELF -> "Halbelf";
        };
    }
}
