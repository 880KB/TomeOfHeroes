package dev.swim.toh.ui.controller.dialog;

import dev.swim.toh.model.data.feat.Feat;
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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class FeatSelectionDialogController {
    public TableView<FeatSelection> featsTableView;
    public TableColumn<FeatSelection, Boolean> isSelectedColumn;
    public TableColumn<FeatSelection, Feat> nameColumn;
    public TableColumn<FeatSelection, Feat> prerequisiteColumn;
    public TableColumn<FeatSelection, String> descriptionColumn;

    private final ObservableList<FeatSelection> feats = FXCollections.observableArrayList();
    private Stage dialogStage;
    private List<Feat> selectedFeats = new ArrayList<>();

    public void initialize() {
        isSelectedColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        isSelectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(isSelectedColumn));

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().featProperty());
        nameColumn.setCellFactory(cellData -> new TableCell<>() {
            @Override
            protected void updateItem(Feat feat, boolean empty) {
                super.updateItem(feat, empty);
                if (empty || feat == null) {
                    setText(null);
                } else {
                    setText(feat.getName());
                }
            }
        });
//        prerequisiteColumn.setCellValueFactory(cell -> new SimpleStringProperty(
//                String.join(", ", cell.getValue().getFeat().getPrerequisites().stream().map(Object::toString).toList())));
//        descriptionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().featProperty().get().getShortDescription()));

        featsTableView.setItems(feats);
        featsTableView.getSortOrder().add(nameColumn);
        featsTableView.sort();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSelectableFeats(Collection<Feat> availableFeats) {
        feats.setAll(availableFeats.stream().map(FeatSelection::new).toList());
    }

    public void onOk(ActionEvent actionEvent) {
        selectedFeats = feats.stream()
                .filter(FeatSelection::isSelected)
                .map(featSelection -> featSelection.featProperty().get())
                .toList();
        dialogStage.close();
    }

    public void onCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public List<Feat> getSelectedFeats() {
        return selectedFeats;
    }
}
