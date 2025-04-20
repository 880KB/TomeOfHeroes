package dev.swim.toh.model.util.javafx;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;

public class Format {

    public static StringConverter<Number> getNumberStringConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return number != null ? number.toString() : "";
            }

            @Override
            public Number fromString(String s) {
                if (s == null || s.isEmpty() || s.equals("-"))
                    return 0;
                return Integer.parseInt(s);
            }
        };
    }

    public static UnaryOperator<TextFormatter.Change> getAttributeIntegerFilter() {
        return getIntegerFilter("\\d{0,2}");
    }

    public static UnaryOperator<TextFormatter.Change> getIntegerFilter() {
        return getIntegerFilter("-?\\d*");
    }

    private static UnaryOperator<TextFormatter.Change> getIntegerFilter(String regEx) {
        return change -> {
            String newText = change.getControlNewText();
            if (newText.matches(regEx)) {
                return change;
            }
            return null;
        };
    }

}
