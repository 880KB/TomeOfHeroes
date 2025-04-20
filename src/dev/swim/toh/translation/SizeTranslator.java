package dev.swim.toh.translation;

import dev.swim.toh.model.data.size.Size;

public class SizeTranslator {
    public static String toGerman(Size size) {
        return switch (size) {
            case FINE -> "mini";
            case DIMINUTIVE -> "winzig";
            case TINY -> "sehr klein";
            case SMALL -> "klein";
            case MEDIUM -> "mittelgroß";
            case LARGE -> "groß";
            case HUGE -> "riesig";
            case GARGANTUAN -> "gigantisch";
            case COLOSSAL -> "kolossal";
        };
    }
}
