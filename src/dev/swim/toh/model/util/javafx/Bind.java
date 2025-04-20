package dev.swim.toh.model.util.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class Bind {

    public static void bindIntegerPropertyToTextField(IntegerProperty property,
                                                      TextField textField,
                                                      UnaryOperator<TextFormatter.Change> filter) {
        textField.setTextFormatter(new TextFormatter<>(filter));
        Bindings.bindBidirectional(textField.textProperty(), property, Format.getNumberStringConverter());
    }
}
