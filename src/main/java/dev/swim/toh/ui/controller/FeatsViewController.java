package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.feats.SelectedFeat;
import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.util.javafx.Bind;
import dev.swim.toh.model.util.javafx.Layout;
import dev.swim.toh.ui.controller.dialog.FeatSelectionDialogController;
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
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FeatsViewController extends CharacterModelAware {
    public TableView<SelectedFeat> featsTableView;
    public TableColumn<SelectedFeat, Feat> nameColumn;
    public TableColumn isRepeatableTableColumn;
    public TableColumn isGrantedByClassTableColumn;
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

        // bind items
        featsTableView.setItems(characterModel.feats.getFeatList());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().featProperty());
        nameColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(Feat feat, boolean empty) {
                super.updateItem(feat, empty);
                if (empty || feat == null) {
                    setText(null);
                } else {
                    setText(feat.getName());
                    boolean prerequisitesSatisfied = characterModel.feats.prerequisitesSatisfied(feat);
                    setTextFill(prerequisitesSatisfied ? Color.BLACK : Color.RED);
                }
            }
        });
        // TODO: not only for INT
        characterModel.attributes.getAttributeBaseProperty(AttributeName.INTELLIGENCE)
                .addListener((obs, oldBase, newBase) -> featsTableView.refresh());

        // delete button in each row
        removeFeatTableColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();
            {
                FontIcon deleteIcon = new FontIcon(FontAwesomeSolid.TRASH_ALT);
                deleteIcon.setIconSize(14);
                deleteIcon.setIconColor(Paint.valueOf("gray"));
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> characterModel.feats.removeFeat(getTableRow().getItem()));
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

        featsTableView.sort();

        // int values
        Bind.bindIntegerPropertyToTextField(characterModel.feats.maxClassFeatsProperty(), maxClassFeatsTextField);
        Bind.bindIntegerPropertyToTextField(characterModel.feats.maxFighterBonusFeatsProperty(), maxFighterFeatsTextField);

        // "add feat" button
        addFeatButton.setOnAction(event -> showFeatSelectionDialog());
    }

    private void showFeatSelectionDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog/feat-selection-dialog.fxml"));
        try {
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Talente auswählen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.featsTableView.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            FeatSelectionDialogController dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setSelectableFeats(characterModel.feats.getNotSelectedFeats());

            dialogStage.showAndWait();

            dialogController.getSelectedFeats().forEach(characterModel.feats::addFeatNoPrerequisitesCheck);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

