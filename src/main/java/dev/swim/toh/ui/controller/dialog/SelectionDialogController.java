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
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
    // classes/feats let the player pick several at once (checkboxes, one dialog covers the whole
    // add-flow); a fixed weapon slot instead holds exactly one value, so for that caller clicking
    // a row is the selection - no checkbox needed
    private boolean singleSelect = false;
    // how many extra detail columns setDetailColumns() has already added, so each one's
    // cellValueFactory closes over its own, distinct index into SelectionItem.getDetail(int)
    private int detailColumnCount = 0;

    public void initialize() {
        // header rows (e.g. "Kriegswaffen") are display-only - mouse-transparent so clicking one
        // can't select it (there is nothing behind it to fall through to, so the click is simply
        // swallowed), tagged for the CSS that gives them a distinct background
        itemsTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(SelectionItem<T> item, boolean empty) {
                super.updateItem(item, empty);
                boolean header = !empty && item != null && item.isHeader();
                setMouseTransparent(header);
                getStyleClass().remove("selection-dialog-header-row");
                if (header)
                    getStyleClass().add("selection-dialog-header-row");
            }
        });

        isSelectedColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        isSelectedColumn.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            {
                checkBox.setOnAction(event -> {
                    SelectionItem<T> row = getTableRow().getItem();
                    if (row != null)
                        row.selectedProperty().set(checkBox.isSelected());
                });
            }

            @Override
            protected void updateItem(Boolean selected, boolean empty) {
                super.updateItem(selected, empty);
                SelectionItem<T> row = empty ? null : getTableRow().getItem();
                if (row == null || row.isHeader()) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(Boolean.TRUE.equals(selected));
                    setGraphic(checkBox);
                }
            }
        });

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
                    boolean header = row != null && row.isHeader();
                    indent.setPrefWidth(row == null ? 0 : row.getDepth() * INDENT_PER_DEPTH);
                    label.setText(name);
                    label.setTextFill(header || row == null || row.isAvailable() ? Color.BLACK : Color.RED);
                    label.setStyle(header ? "-fx-font-weight: bold;" : null);
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
     * Switches from the default "check any number of rows" behaviour to "exactly one row,
     * selected by clicking it" - hides the checkbox column and drives {@link #onOk} off the
     * table's own row selection instead of each item's {@code selectedProperty}.
     */
    public void setSingleSelect(boolean singleSelect) {
        this.singleSelect = singleSelect;
        isSelectedColumn.setVisible(!singleSelect);
        itemsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Highlights the row for the given value as the initial selection - e.g. the weapon
     * currently in a slot, when reopening the dialog to change it. Only meaningful together with
     * {@link #setSingleSelect}; must be called after {@link #setItems}.
     */
    public void preselect(T value) {
        items.stream()
                .filter(item -> item.getValue() == value)
                .findFirst()
                .ifPresent(item -> {
                    itemsTableView.getSelectionModel().select(item);
                    itemsTableView.scrollTo(item);
                });
    }

    /**
     * Adds extra read-only columns right after the name column, one per header, populated from
     * each row's {@link SelectionItem#getDetail(int)} at the matching position - e.g. a weapon's
     * damage/critical/range/type, so the player can compare candidates without opening every
     * "more info" popup. Classes/feats never call this, so they keep their current column set.
     * Must be called before {@link #setItems} so the dialog sizes itself around the full table.
     */
    public void setDetailColumns(List<String> headers) {
        int insertIndex = itemsTableView.getColumns().indexOf(nameColumn) + 1 + detailColumnCount;
        for (String header : headers) {
            int detailIndex = detailColumnCount++;
            TableColumn<SelectionItem<T>, String> column = new TableColumn<>(header);
            column.setSortable(false);
            column.setResizable(false);
            column.setPrefWidth(70);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDetail(detailIndex)));
            itemsTableView.getColumns().add(insertIndex++, column);
        }
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
        if (singleSelect) {
            SelectionItem<T> selected = itemsTableView.getSelectionModel().getSelectedItem();
            // a header can't be clicked (see the row factory in initialize()), but keyboard
            // navigation still walks over it as a row, so guard against it landing as "selected"
            selectedValues = (selected == null || selected.isHeader()) ? List.of() : List.of(selected.getValue());
        } else {
            selectedValues = items.stream()
                    .filter(item -> !item.isHeader() && item.isSelected())
                    .map(SelectionItem::getValue)
                    .toList();
        }
        dialogStage.close();
    }

    public void onCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public List<T> getSelectedValues() {
        return selectedValues;
    }
}
