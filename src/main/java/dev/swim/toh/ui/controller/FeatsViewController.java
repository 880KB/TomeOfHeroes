package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.feats.SelectedFeat;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.util.javafx.Bind;
import dev.swim.toh.model.util.javafx.Layout;
import dev.swim.toh.model.validation.Violation;
import dev.swim.toh.translation.PrerequisiteFormatter;
import dev.swim.toh.ui.controller.dialog.SelectionDialogController;
import dev.swim.toh.ui.controller.dialog.SelectionItem;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.stream.Collectors;

public class FeatsViewController extends CharacterModelAware {
    public TableView<SelectedFeat> featsTableView;
    public TableColumn<SelectedFeat, Feat> nameColumn;
    public TableColumn<SelectedFeat, Feat> isRepeatableTableColumn;
    public TableColumn<SelectedFeat, Void> removeFeatTableColumn;
    public Button addFeatButton;
    public TextField maxClassFeatsTextField;
    public TextField maxFighterFeatsTextField;
    public TextField availableFeatsCountTextField;

    public FeatsViewController(CharacterModel characterModel) {
        super(characterModel);
    }

    @FXML
    public void initialize() {
        bindFields();
    }

    public void bindFields() {
        // auto sizing of table
        featsTableView.itemsProperty()
                .addListener((observable, oldList, newList) ->
                        Layout.updateTableHeight(featsTableView));
        characterModel.feats.getFeatList().addListener((ListChangeListener<SelectedFeat>) c -> Layout.updateTableHeight(featsTableView));

        // the other two columns are fixed-width (resizable="false" in FXML), so JavaFX's own
        // constrained resize policy gives 100% of whatever width is left to the only resizable
        // column (feat name) - correctly, since it accounts for the table's real available
        // width itself, unlike a manually computed subtraction
        featsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // bind items
        featsTableView.setItems(characterModel.feats.getFeatList());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().featProperty());
        nameColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(Feat feat, boolean empty) {
                super.updateItem(feat, empty);
                if (empty || feat == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(feat.getName());
                    Violation violation = findViolation(getTableRow().getItem());
                    setTextFill(violation == null ? Color.BLACK : Color.RED);
                    setTooltip(violation == null ? null : new Tooltip(violation.message()));
                }
            }
        });
        isRepeatableTableColumn.setCellValueFactory(cellData -> cellData.getValue().featProperty());
        isRepeatableTableColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(Feat feat, boolean empty) {
                super.updateItem(feat, empty);
                setText(empty || feat == null ? null : (feat.getRepeatType().isRepeatable() ? "Ja" : "Nein"));
            }
        });

        // re-check the red/black prerequisite highlighting whenever the validation result changes
        characterModel.featsValidation.violationsProperty().addListener((ListChangeListener<Violation>) c -> featsTableView.refresh());

        // delete button in each row
        removeFeatTableColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();
            {
                FontIcon deleteIcon = new FontIcon(FontAwesomeSolid.TRASH_ALT);
                deleteIcon.setIconSize(14);
                deleteIcon.setIconColor(Paint.valueOf("gray"));
                deleteIcon.setTranslateY(-1);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> characterModel.feats.removeFeat(getTableRow().getItem()));
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

        featsTableView.sort();

        // int values
        Bind.bindIntegerPropertyToTextField(characterModel.feats.maxClassFeatsProperty(), maxClassFeatsTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.feats.maxFighterBonusFeatsProperty(), maxFighterFeatsTextField);

        // "add feat" button
        FontIcon addIcon = new FontIcon(FontAwesomeSolid.PLUS);
        addIcon.setIconSize(14);
        addIcon.setIconColor(Paint.valueOf("gray"));
        addIcon.setTranslateY(-1);
        addFeatButton.setGraphic(addIcon);
        addFeatButton.setOnAction(event -> showFeatSelectionDialog());
    }

    private void showFeatSelectionDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog/selection-dialog.fxml"));
        try {
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Talente auswählen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.featsTableView.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            SelectionDialogController<Feat> dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setItems(characterModel.feats.getNotSelectedFeats().stream()
                    .map(this::toSelectionItem)
                    .toList());

            dialogStage.showAndWait();

            dialogController.getSelectedValues().forEach(characterModel.feats::addFeatNoPrerequisitesCheck);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Violation findViolation(SelectedFeat selectedFeat) {
        return characterModel.featsValidation.violationsProperty().stream()
                .filter(violation -> violation.source() == selectedFeat)
                .findFirst()
                .orElse(null);
    }

    private SelectionItem<Feat> toSelectionItem(Feat feat) {
        boolean satisfied = characterModel.feats.prerequisitesSatisfied(feat);
        String reason = satisfied ? null : feat.getPrerequisites().stream()
                .filter(prerequisite -> !prerequisite.isSatisfiedBy(characterModel))
                .map(prerequisite -> PrerequisiteFormatter.toGerman(prerequisite, characterModel.feats))
                .collect(Collectors.joining(", "));
        return new SelectionItem<>(feat, feat.getName(), satisfied, reason);
    }
}

