package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.feats.ChosenFeat;
import dev.swim.toh.model.data.feat.FeatName;
import dev.swim.toh.model.data.feat.FeatRegistry;
import dev.swim.toh.model.util.javafx.Layout;
import dev.swim.toh.ui.controller.dialog.FeatSelectionDialogController;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class FeatsViewController extends CharacterModelAware {
    public TableView<ChosenFeat> featsTableView;
    public TableColumn<ChosenFeat, FeatName> nameColumn;
    public TableColumn isRepeatableTableColumn;
    public TableColumn isGrantedByClassTableColumn;
    public TableColumn<ChosenFeat, Void> removeFeatTableColumn;
    public Button addFeatButton;
    public TextField availableFeatsCountTextField;

    public void bindFields() {
        // auto sizing of table
        featsTableView.itemsProperty()
                .addListener((observable, oldList, newList) ->
                        Layout.updateTableHeight(featsTableView));
        characterModel.feats.getFeatList().addListener((ListChangeListener<ChosenFeat>) c -> Layout.updateTableHeight(featsTableView));

        // bind items
        featsTableView.setItems(characterModel.feats.getFeatList());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

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

        // "add feat" button
        addFeatButton.setOnAction(event -> showFeatSelectionDialog());
    }

    private void showFeatSelectionDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/swim/toh/ui/view/dialog/feat-selection-dialog.fxml"));
        try {
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Talente auswählen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.featsTableView.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            FeatSelectionDialogController dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setAvailableFeats(FeatRegistry.getFeats(characterModel.feats.getNotSelectedFeats().stream().toList()));

            dialogStage.showAndWait();

            dialogController.getSelectedFeats().forEach(feat -> characterModel.feats.addFeatNoPrerequisitesCheck(feat.getName()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

