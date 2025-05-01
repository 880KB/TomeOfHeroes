package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.feats.ChosenFeat;
import dev.swim.toh.model.data.feat.FeatName;
import dev.swim.toh.model.util.javafx.Layout;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class FeatsViewController extends CharacterModelAware {
    public TableView<ChosenFeat> featsTableView;
    public TableColumn<ChosenFeat, FeatName> featTableColumn;
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
        featTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // delete button in each row
        removeFeatTableColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("🗑️");
            {
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
        addFeatButton.setOnAction(event -> addFeatDialog());
        characterModel.feats.getFeatList().addListener((ListChangeListener<? super ChosenFeat>) c ->
                addFeatButton.setDisable(characterModel.feats.getAvailableFeats().isEmpty()));
    }

    private void addFeatDialog() {
        List<FeatName> availableFeats = characterModel.feats.getAvailableFeats();
        ChoiceDialog<FeatName> dialog = new ChoiceDialog<>(null, availableFeats);
        dialog.setTitle("Neues Talent");
        dialog.setHeaderText("Füge ein neues Talent hinzu.");
        dialog.setContentText("Talent:");
        dialog.setGraphic(null);

        Optional<FeatName> result = dialog.showAndWait();

        result.ifPresent(featName -> characterModel.feats.addFeat(featName));
    }
}

