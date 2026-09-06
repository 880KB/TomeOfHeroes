package dev.swim.toh.model.util.javafx;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

/**
 * A small, non-modal popup anchored below a node - for text too long to read comfortably in a
 * {@link javafx.scene.control.Tooltip} (which uses a small font and hides itself on a timer
 * regardless of whether the user is still reading). Stays open until the user clicks elsewhere.
 */
public class InfoPopup {

    private static final double MAX_WIDTH = 280.0;

    public static void show(Node anchor, String title, String body) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("info-popup-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(MAX_WIDTH);

        Label bodyLabel = new Label(body);
        bodyLabel.setWrapText(true);
        bodyLabel.setMaxWidth(MAX_WIDTH);

        VBox content = new VBox(6, titleLabel, bodyLabel);
        content.getStyleClass().add("info-popup");
        // a Popup always gets its own private Scene, so it never inherits the owner window's
        // stylesheet - it has to be attached here, or the card framing above silently does nothing
        Styles.applyAppStylesheet(content);

        Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.getContent().add(content);

        Bounds anchorBounds = anchor.localToScreen(anchor.getBoundsInLocal());
        popup.show(anchor, anchorBounds.getMinX(), anchorBounds.getMaxY());
    }
}
