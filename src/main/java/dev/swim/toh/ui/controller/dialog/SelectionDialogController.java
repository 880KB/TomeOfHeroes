package dev.swim.toh.ui.controller.dialog;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reusable "pick one or more of these" dialog, used for both adding classes and adding feats -
 * the two flows only differ in what {@link SelectionItem}s they hand in, not in how the dialog
 * itself behaves.
 */
public class SelectionDialogController<T> {
    public TableView<SelectionItem<T>> itemsTableView;
    public TableColumn<SelectionItem<T>, Boolean> isSelectedColumn;
    public TableColumn<SelectionItem<T>, String> nameColumn;
    public TableColumn<SelectionItem<T>, String> reasonColumn;

    private final ObservableList<SelectionItem<T>> items = FXCollections.observableArrayList();
    private Stage dialogStage;
    private List<T> selectedValues = new ArrayList<>();

    public void initialize() {
        isSelectedColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        isSelectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(isSelectedColumn));

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nameColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setText(null);
                } else {
                    setText(name);
                    SelectionItem<T> row = getTableRow().getItem();
                    setTextFill(row == null || row.isAvailable() ? Color.BLACK : Color.RED);
                }
            }
        });

        reasonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnavailableReason()));

        itemsTableView.setItems(items);
        itemsTableView.getSortOrder().add(nameColumn);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setItems(Collection<SelectionItem<T>> newItems) {
        items.setAll(newItems);
        // sortOrder is set up in initialize(), but that runs before real data exists here -
        // re-apply the sort now that the list actually has content to sort.
        itemsTableView.sort();
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
