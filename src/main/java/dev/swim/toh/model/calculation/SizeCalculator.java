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

}
