package dev.swim.toh.model.util.javafx;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Applies app.css - every new {@link Scene}/{@link Parent} needs this explicitly, it is never
 * inherited automatically (not by a dialog's own Stage/Scene, and not by a {@link javafx.stage.Popup},
 * which always renders in its own private Scene regardless of which window it's anchored to).
 */
public class Styles {

    private static final String APP_STYLESHEET = Styles.class.getResource("/css/app.css").toExternalForm();

    public static void applyAppStylesheet(Scene scene) {
        scene.getStylesheets().add(APP_STYLESHEET);
    }

    public static void applyAppStylesheet(Parent node) {
        node.getStylesheets().add(APP_STYLESHEET);
    }
}
