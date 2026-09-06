package dev.swim.toh.ui.controller.dialog;

import dev.swim.toh.model.util.javafx.InfoPopup;
import dev.swim.toh.model.util.javafx.Layout;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reusable "pick one or more of these" dialog, used for both adding classes and adding feats -
 * the two flows only differ in what {@link SelectionItem}s they hand in, not in how the dialog
 * itself behaves.
 */
public class SelectionDialogController<T> {

    // horizontal indent per nesting level - e.g. a feat shown indented under the feat it
    // builds on, mirroring how the Player's Handbook lists them
    private static final double INDENT_PER_DEPTH = 16.0;
    // a deliberate floor so the dialog doesn't shrink to something cramped when only one or two
    // items are offered - not a guessed content height, that part is still measured (see below)
    private static final double MIN_TABLE_HEIGHT = 150.0;
    // long, open-ended lists (all feats, as opposed to a character's own short chosen-feats
    // table) must not be able to size the window past the screen - leaves headroom for the
    // dialog's own title bar, the button row below the table, and the OS menu bar/dock
    private static final double MAX_TABLE_HEIGHT_SCREEN_FRACTION = 0.7;
    public TableView<SelectionItem<T>> itemsTableView;
    public TableColumn<SelectionItem<T>, Boolean> isSelectedColumn;
    public TableColumn<SelectionItem<T>, String> nameColumn;
    public TableColumn<SelectionItem<T>, Void> infoColumn;
    public TableColumn<SelectionItem<T>, String> reasonColumn;

    private final ObservableList<SelectionItem<T>> items = FXCollections.observableArrayList();
    private Stage dialogStage;
    private List<T> selectedValues = new ArrayList<>();

    public void initialize() {
        isSelectedColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        isSelectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(isSelectedColumn));

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nameColumn.setCellFactory(col -> new TableCell<>() {
            private final Region indent = new Region();
            private final Label label = new Label();
            private final HBox content = new HBox(indent, label);

            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setGraphic(null);
                } else {
                    SelectionItem<T> row = getTableRow().getItem();
                    indent.setPrefWidth(row == null ? 0 : row.getDepth() * INDENT_PER_DEPTH);
                    label.setText(name);
                    label.setTextFill(row == null || row.isAvailable() ? Color.BLACK : Color.RED);
                    setGraphic(content);
                }
            }
        });

        // "more info" button - only shown for items that actually carry a description (e.g.
        // feats), not for values like classes that don't have one (yet)
        infoColumn.setCellFactory(col -> new TableCell<>() {
            private final Button infoButton = new Button();
            {
                FontIcon infoIcon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
                infoIcon.setIconSize(14);
                infoIcon.setIconColor(Paint.valueOf("gray"));
                infoIcon.setTranslateY(-1);
                infoButton.setGraphic(infoIcon);
                infoButton.getStyleClass().add("icon-button");
                infoButton.setOnAction(event -> {
                    SelectionItem<T> row = getTableRow().getItem();
                    InfoPopup.show(infoButton, row.getInfoTitle(), row.getInfoBody());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                SelectionItem<T> row = empty ? null : getTableRow().getItem();
                setGraphic(row != null && row.hasInfo() ? infoButton : null);
            }
        });

        reasonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnavailableReason()));

        itemsTableView.setItems(items);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Items are shown in exactly the order given - e.g. feats grouped under the feat they build
     * on. The caller is responsible for sorting/grouping before calling this.
     */
    public void setItems(Collection<SelectionItem<T>> newItems) {
        items.setAll(newItems);
        double maxTableHeight = Screen.getPrimary().getVisualBounds().getHeight() * MAX_TABLE_HEIGHT_SCREEN_FRACTION;
        Layout.updateTableHeight(itemsTableView, MIN_TABLE_HEIGHT, maxTableHeight);

        // shrink the window to fit the (now correctly sized) table instead of always opening at
        // a fixed, often mostly-empty size - deferred to the next pulse since the table's real
        // height may itself only become known once its skin is attached during the dialog's own
        // initial show (see Layout.updateTableHeight). The floor lives on the table itself (see
        // above), not here as e.g. dialogStage.setMinHeight - that would force the whole window
        // taller without the table growing to match, leaving a gap above the (bottom-pinned)
        // button row instead of a clean fit.
        Platform.runLater(() -> {
            dialogStage.sizeToScene();

            // sizeToScene() above sized the window from the columns' own preferred widths, which
            // know nothing about a vertical scrollbar (only relevant once the table was actually
            // capped below its content height, see maxTableHeight) - that scrollbar then eats into
            // the columns' available width, which can in turn trigger an unwanted *horizontal*
            // scrollbar. Force a layout pass and measure the real scrollbar width (not a guessed
            // constant) rather than always reserving space for one that's usually not there.
            itemsTableView.applyCss();
            itemsTableView.layout();
            Node verticalScrollBar = itemsTableView.lookup(".scroll-bar:vertical");
            if (verticalScrollBar != null && verticalScrollBar.isVisible()) {
                dialogStage.setWidth(dialogStage.getWidth() + verticalScrollBar.prefWidth(-1));
            }
        });
    }

    public void onOk(ActionEvent actionEvent) {
        selectedValues = items.stream()
                .filter(SelectionItem::isSelected)
                .map(SelectionItem::getValue)
                .toList();
        dialogStage.close();
    }

    public void onCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public List<T> getSelectedValues() {
        return selectedValues;
    }
}
