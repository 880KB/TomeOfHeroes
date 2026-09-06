package dev.swim.toh.model.util.javafx;

import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;

public class Layout {

    /**
     * Sizes the table to exactly fit all its rows - no internal scrollbar, ever - and stops a
     * parent layout (e.g. an HBox pairing this table with a taller one) from stretching it
     * beyond that, which would otherwise leave a large empty area below the actual rows.
     * <p>
     * The header's real rendered height is measured rather than guessed - a guessed height that's
     * even a pixel too small makes a scrollbar appear, which then eats into the table's width too
     * and can trigger a second, horizontal scrollbar as a knock-on effect.
     */
    public static void updateTableHeight(TableView<?> tableView) {
        updateTableHeight(tableView, 0, Double.MAX_VALUE);
    }

    /**
     * Same exact-fit sizing as {@link #updateTableHeight(TableView)}, but never shrinks below
     * {@code minHeight} - e.g. so a table offering only one or two rows doesn't end up looking
     * cramped. The floor is applied to the table itself (not, say, a containing dialog's window)
     * so that whatever sits below the table in the layout stays directly attached to it, with no
     * gap opening up between them.
     */
    public static void updateTableHeight(TableView<?> tableView, double minHeight) {
        updateTableHeight(tableView, minHeight, Double.MAX_VALUE);
    }

    /**
     * Same as {@link #updateTableHeight(TableView, double)}, but also never grows past
     * {@code maxHeight} - e.g. so a long, open-ended list (every selectable feat, as opposed to
     * a character's own short, already-chosen list) can't size its window taller than the screen.
     * Past that cap the table falls back to its normal internal scrollbar for the overflow rows -
     * that's the one case in this app where a table scrolling is actually the right behavior,
     * since there's no outer scroll container around a dialog the way there is around the main
     * character sheet.
     */
    public static void updateTableHeight(TableView<?> tableView, double minHeight, double maxHeight) {
        double measuredHeaderHeight = measuredHeaderHeight(tableView);
        applyHeight(tableView, measuredHeaderHeight, minHeight, maxHeight);

        // Before the table has ever been shown, its header isn't laid out yet and can't be
        // measured (this happens on the very first call, while the FXML is still being built) -
        // re-measure for real the moment its skin is attached, instead of relying on a guess.
        if (measuredHeaderHeight < 0) {
            tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
                if (newSkin != null)
                    applyHeight(tableView, measuredHeaderHeight(tableView), minHeight, maxHeight);
            });
        }
    }

    private static void applyHeight(TableView<?> tableView, double measuredHeaderHeight, double minHeight, double maxHeight) {
        int rows = tableView.getItems().size();
        double rowHeight = tableView.getFixedCellSize();
        // one frame before the skin exists there's nothing real to measure yet; a row's own
        // height is a reasonable stand-in until the listener above corrects it for real
        double headerHeight = measuredHeaderHeight >= 0 ? measuredHeaderHeight : rowHeight;
        // Sub-pixel rounding epsilon, not a fudge factor: on HiDPI displays JavaFX snaps
        // fractional CSS units to physical pixels, so an exact-to-the-pixel height can still fall
        // a rounding error short of what's actually rendered. This covers that rounding only -
        // it does not grow with row count, unlike the earlier (wrong) fixed padding attempt.
        double subpixelEpsilon = 2;
        double exactFitHeight = headerHeight + rowHeight * rows + subpixelEpsilon;
        double totalHeight = Math.min(Math.max(exactFitHeight, minHeight), maxHeight);
        tableView.setPrefHeight(totalHeight);
        tableView.setMinHeight(Region.USE_PREF_SIZE);
        tableView.setMaxHeight(Region.USE_PREF_SIZE);
    }

    private static double measuredHeaderHeight(TableView<?> tableView) {
        Node header = tableView.lookup(".column-header-background");
        return header != null ? header.getBoundsInLocal().getHeight() : -1;
    }
}
