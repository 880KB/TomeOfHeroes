package dev.swim.toh.model.calculation;

import dev.swim.toh.model.data.race.Race;
import dev.swim.toh.model.data.size.Size;

public class SizeCalculator {

    public static Size getSize(Race race) {
        return switch (race) {
            case HUMAN, ELF, HALF_ORC, HALF_ELF -> Size.MEDIUM;
            case DWARF, HALFLING, GNOME -> Size.SMALL;
        };
    }

    public static int getAcModifier(Size size) {
        return switch (size) {
            case FINE -> 8;
            case DIMINUTIVE -> 4;
            case TINY -> 2;
            case SMALL -> 1;
            case MEDIUM -> 0;
            case LARGE -> -1;
            case HUGE -> -2;
            case GARGANTUAN -> -4;
            case COLOSSAL -> -8;
        };
    }

}
