package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.rules.clazz.ClassRules;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class ClassesViewController extends CharacterModelAware {

    public TableView<ChosenClass> classesTableView;
    public TableColumn<ChosenClass, Clazz> classTableColumn;
    public TableColumn<ChosenClass, Integer> levelTableColumn;
    public TableColumn<ChosenClass, Boolean> isFirstClassTableColumn;

    public void bindFields() {
        classesTableView.setItems(characterModel.classes.getClassList());
        classTableColumn.setCellValueFactory(cellData -> cellData.getValue().clazzProperty());
        levelTableColumn.setCellValueFactory(cellData -> cellData.getValue().levelProperty().asObject());

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

        isFirstClassTableColumn.setCellValueFactory(cellData -> cellData.getValue().isFirstClassProperty());
    }
}
