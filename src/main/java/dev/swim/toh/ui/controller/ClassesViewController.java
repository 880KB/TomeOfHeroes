package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.rules.ClassRules;
import dev.swim.toh.model.util.javafx.Bind;
import dev.swim.toh.model.util.javafx.Format;
import dev.swim.toh.model.util.javafx.Layout;
import dev.swim.toh.translation.ClazzTranslator;
import dev.swim.toh.ui.controller.dialog.SelectionDialogController;
import dev.swim.toh.ui.controller.dialog.SelectionItem;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

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

        // the other three columns are fixed-width (resizable="false" in FXML), so JavaFX's own
        // constrained resize policy gives 100% of whatever width is left to the only resizable
        // column (class name) - correctly, since it accounts for the table's real available
        // width itself, unlike a manually computed subtraction
        classesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // bind items
        classesTableView.setItems(characterModel.classes.getClassList());
        classTableColumn.setCellValueFactory(cellData -> cellData.getValue().clazzProperty());
        classTableColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Clazz clazz, boolean empty) {
                super.updateItem(clazz, empty);
                setText(empty || clazz == null ? null : ClazzTranslator.toGerman(clazz));
            }
        });
        levelTableColumn.setCellValueFactory(cellData -> cellData.getValue().levelProperty().asObject());
        isFirstClassTableColumn.setCellValueFactory(cellData -> cellData.getValue().isFirstClassProperty());
        isFirstClassTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(isFirstClassTableColumn));
        Bind.bindIntegerPropertyToTextField(characterModel.classes.getCharacterLevelProperty(), characterLevelTextField, Format.getIntegerFilter());

        // level as a spinner: visible +/- buttons make it obvious at a glance that this is
        // editable, unlike the previous plain number that only responded to an undiscoverable
        // double-click
        levelTableColumn.setCellFactory(col -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>(ClassRules.MIN_LEVEL, ClassRules.MAX_LEVEL, ClassRules.MIN_LEVEL);
            private ChosenClass boundClass;
            {
                spinner.setEditable(true);
                spinner.setPrefWidth(70);
                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    if (boundClass != null && newValue != null)
                        boundClass.levelProperty().set(newValue);
                });
                spinner.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                    if (!isFocused)
                        commitTypedLevel();
                });
            }

            private void commitTypedLevel() {
                try {
                    int typed = Integer.parseInt(spinner.getEditor().getText().trim());
                    int clamped = Math.max(ClassRules.MIN_LEVEL, Math.min(ClassRules.MAX_LEVEL, typed));
                    spinner.getValueFactory().setValue(clamped);
                } catch (NumberFormatException e) {
                    spinner.getEditor().setText(String.valueOf(spinner.getValue()));
                }
            }

            @Override
            protected void updateItem(Integer level, boolean empty) {
                super.updateItem(level, empty);
                if (empty || level == null) {
                    boundClass = null;
                    setGraphic(null);
                } else {
                    boundClass = getTableRow().getItem();
                    spinner.getValueFactory().setValue(level);
                    setGraphic(spinner);
                }
            }
        });

        // delete button in each row
        actionTableColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();
            {
                FontIcon deleteIcon = new FontIcon(FontAwesomeSolid.TRASH_ALT);
                deleteIcon.setIconSize(14);
                deleteIcon.setIconColor(Paint.valueOf("gray"));
                deleteIcon.setTranslateY(-1);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> characterModel.classes.removeClass(getTableRow().getItem()));
                deleteButton.getStyleClass().add("icon-button");
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
        FontIcon addIcon = new FontIcon(FontAwesomeSolid.PLUS);
        addIcon.setIconSize(14);
        addIcon.setIconColor(Paint.valueOf("gray"));
        addIcon.setTranslateY(-1);
        addClassButton.setGraphic(addIcon);
        addClassButton.setOnAction(event -> addClassDialog());
        characterModel.classes.getClassList().addListener((ListChangeListener<ChosenClass>) c ->
                addClassButton.setDisable(characterModel.classes.getAvailableClasses().isEmpty()));
    }

    private void addClassDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog/selection-dialog.fxml"));
        try {
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Klassen auswählen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.classesTableView.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            SelectionDialogController<Clazz> dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setItems(characterModel.classes.getAvailableClasses().stream()
                    .map(clazz -> new SelectionItem<>(clazz, ClazzTranslator.toGerman(clazz), true, null))
                    .toList());

            dialogStage.showAndWait();

            dialogController.getSelectedValues().forEach(clazz -> characterModel.classes.addClass(clazz, 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
