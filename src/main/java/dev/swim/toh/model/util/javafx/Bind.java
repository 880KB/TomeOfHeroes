package dev.swim.toh.model.util.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class Bind {

    public static void bindIntegerPropertyToTextField(IntegerProperty property,
                                                      TextField textField) {
        bindIntegerPropertyToTextField(property, textField, Format.getIntegerFilter());
    }

    public static void bindIntegerPropertyToTextField(IntegerProperty property,
                                                      TextField textField,
                                                      UnaryOperator<TextFormatter.Change> filter) {
        textField.setTextFormatter(new TextFormatter<>(filter));
        Bindings.bindBidirectional(textField.textProperty(), property, Format.getNumberStringConverter());
        selectAllOnFocus(textField);
        loseFocusOnEnter(textField);
    }

    public static void bindUsedMaxToTextField(IntegerProperty used, IntegerProperty max, TextField textField) {
        textField.textProperty().bind(Bindings.createStringBinding(
                () -> used.get() + "/" + max.get(), used, max));
    }

    /**
     * Selects the whole value when a field gains focus (click or tab-in), so typing immediately
     * replaces it instead of inserting at the click position - deferred a frame because the click
     * that grants focus also positions the caret right after, which would otherwise collapse the
     * selection again.
     */
    private static void selectAllOnFocus(TextField textField) {
        textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused)
                Platform.runLater(textField::selectAll);
        });
    }

    /**
     * Enter fires the field's ActionEvent - hand focus to the window root instead of leaving it
     * in the field, so the edit reads as "done" instead of just sitting there committed but still
     * focused. The root must be focusTraversable (see main-window.fxml) or requestFocus() on it
     * is a no-op.
     */
    private static void loseFocusOnEnter(TextField textField) {
        textField.setOnAction(event -> textField.getScene().getRoot().requestFocus());
    }
}
