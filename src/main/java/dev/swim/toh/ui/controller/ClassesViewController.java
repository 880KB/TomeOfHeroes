package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.rules.ClassRules;
import dev.swim.toh.model.util.javafx.Bind;
import dev.swim.toh.model.util.javafx.Format;
import dev.swim.toh.model.util.javafx.Layout;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClassesViewController extends CharacterModelAware {

    public TableView<ChosenClass> classesTableView;
    public TableColumn<ChosenClass, Clazz> classTableColumn;
    public TableColumn<ChosenClass, Integer> levelTableColumn;
    public TableColumn<ChosenClass, Boolean> isFirstClassTableColumn;
    public TableColumn<ChosenClass, Void> actionTableColumn;
    public Button addClassButton;
    public TextField characterLevelTextField;

    public ClassesViewController(CharacterModel characterModel) {
        super(characterModel);
    }

    @FXML
    public void initialize() {
        bindFields();
    }

    public void bindFields() {
        // auto sizing of table
        classesTableView.itemsProperty()
                .addListener((observable, oldList, newList) ->
                        Layout.updateTableHeight(classesTableView));
        characterModel.classes.getClassList().addListener((ListChangeListener<ChosenClass>) c -> Layout.updateTableHeight(classesTableView));

        // bind items
        classesTableView.setItems(characterModel.classes.getClassList());
        classTableColumn.setCellValueFactory(cellData -> cellData.getValue().clazzProperty());
        levelTableColumn.setCellValueFactory(cellData -> cellData.getValue().levelProperty().asObject());
        isFirstClassTableColumn.setCellValueFactory(cellData -> cellData.getValue().isFirstClassProperty());
        Bind.bindIntegerPropertyToTextField(characterModel.classes.getCharacterLevelProperty(), characterLevelTextField, Format.getIntegerFilter());

        // make level editable
        levelTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        levelTableColumn.setOnEditCommit(event -> {
            ChosenClass chosenClass = event.getRowValue();
            Integer newLevel = event.getNewValue();
            if (ClassRules.isValidLevel(newLevel))
                chosenClass.levelProperty().set(newLevel);
            else
                classesTableView.refresh();
        });

        // delete button in each row
        actionTableColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("🗑️");
            {
                deleteButton.setOnAction(event -> characterModel.classes.removeClass(getTableRow().getItem()));
                deleteButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // "add class" button
        addClassButton.setOnAction(event -> addClassDialog());
        characterModel.classes.getClassList().addListener((ListChangeListener<ChosenClass>) c ->
                addClassButton.setDisable(characterModel.classes.getAvailableClasses().isEmpty()));
    }

    private void addClassDialog() {
        List<Clazz> availableClasses = characterModel.classes.getAvailableClasses();
        ChoiceDialog<Clazz> dialog = new ChoiceDialog<>(null, availableClasses);
        dialog.setTitle("Neue Klasse");
        dialog.setHeaderText("Füge eine neue Klasse hinzu.");
        dialog.setContentText("Klasse:");
        dialog.setGraphic(null);

        Optional<Clazz> result = dialog.showAndWait();

        result.ifPresent(clazz -> characterModel.classes.addClass(clazz, 1));
    }
}
