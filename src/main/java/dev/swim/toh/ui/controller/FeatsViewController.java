package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.feats.SelectedFeat;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.prerequisites.FeatPrerequisite;
import dev.swim.toh.model.util.javafx.Bind;
import dev.swim.toh.model.util.javafx.InfoPopup;
import dev.swim.toh.model.util.javafx.Layout;
import dev.swim.toh.model.util.javafx.Styles;
import dev.swim.toh.model.validation.Violation;
import dev.swim.toh.translation.PrerequisiteFormatter;
import dev.swim.toh.ui.controller.dialog.SelectionDialogController;
import dev.swim.toh.ui.controller.dialog.SelectionItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FeatsViewController extends CharacterModelAware {

    // horizontal indent per nesting level - mirrors SelectionDialogController's feat hierarchy
    private static final double INDENT_PER_DEPTH = 16.0;

    public TableView<SelectedFeat> featsTableView;
    public TableColumn<SelectedFeat, Feat> nameColumn;
    public TableColumn<SelectedFeat, Void> infoTableColumn;
    public TableColumn<SelectedFeat, Void> removeFeatTableColumn;
    public Button addFeatButton;
    public TextField maxClassFeatsTextField;
    public TextField maxFighterFeatsTextField;
    public TextField availableFeatsCountTextField;

    // featsTableView is bound to this instead of characterModel.feats.getFeatList() directly, so
    // the displayed order can group a feat under the feat it builds on (see refreshFeatOrder())
    // instead of just following selection order
    private final ObservableList<SelectedFeat> orderedFeats = FXCollections.observableArrayList();
    private final Map<SelectedFeat, Integer> depthBySelectedFeat = new HashMap<>();

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
        characterModel.feats.getFeatList().addListener((ListChangeListener<SelectedFeat>) c -> {
            refreshFeatOrder();
            Layout.updateTableHeight(featsTableView);
        });

        // the other two columns are fixed-width (resizable="false" in FXML), so JavaFX's own
        // constrained resize policy gives 100% of whatever width is left to the only resizable
        // column (feat name) - correctly, since it accounts for the table's real available
        // width itself, unlike a manually computed subtraction
        featsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // bind items
        refreshFeatOrder();
        featsTableView.setItems(orderedFeats);
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().featProperty());
        nameColumn.setCellFactory(cellData -> new TableCell<>() {
            private final Region indent = new Region();
            private final Label label = new Label();
            private final HBox content = new HBox(indent, label);

            @Override
            protected void updateItem(Feat feat, boolean empty) {
                super.updateItem(feat, empty);
                if (empty || feat == null) {
                    setGraphic(null);
                    setTooltip(null);
                } else {
                    SelectedFeat selectedFeat = getTableRow().getItem();
                    indent.setPrefWidth(depthBySelectedFeat.getOrDefault(selectedFeat, 0) * INDENT_PER_DEPTH);
                    label.setText(feat.getName());
                    Violation violation = findViolation(selectedFeat);
                    label.setTextFill(violation == null ? Color.BLACK : Color.RED);
                    setGraphic(content);
                    setTooltip(violation == null ? null : new Tooltip(violation.message()));
                }
            }
        });

        // re-check the red/black prerequisite highlighting whenever the validation result changes
        characterModel.featsValidation.violationsProperty().addListener((ListChangeListener<Violation>) c -> featsTableView.refresh());

        // "more info" button in each row - opens a popup with the feat's short and full
        // description, since that's too much text to read comfortably in a tooltip
        infoTableColumn.setCellFactory(col -> new TableCell<>() {
            private final Button infoButton = new Button();
            {
                FontIcon infoIcon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
                infoIcon.setIconSize(14);
                infoIcon.setIconColor(Paint.valueOf("gray"));
                infoIcon.setTranslateY(-1);
                infoButton.setGraphic(infoIcon);
                infoButton.setOnAction(event -> {
                    Feat feat = getTableRow().getItem().featProperty().get();
                    InfoPopup.show(infoButton, feat.getShortDescription(), feat.getDescription());
                });
                infoButton.getStyleClass().add("icon-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : infoButton);
            }
        });

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
            Scene scene = new Scene(page);
            Styles.applyAppStylesheet(scene);
            dialogStage.setScene(scene);

            SelectionDialogController<Feat> dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setItems(buildFeatSelectionItems());

            dialogStage.showAndWait();

            dialogController.getSelectedValues().forEach(characterModel.feats::addFeatNoPrerequisitesCheck);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rebuilds {@link #orderedFeats} (and {@link #depthBySelectedFeat}) from the character's
     * actual feat list, same grouping as {@link #buildFeatSelectionItems()}: a feat that builds
     * on another selected feat is listed directly under it, indented. Called whenever the feat
     * list changes, so newly added/removed feats are re-grouped immediately instead of just
     * being appended/removed in place.
     */
    private void refreshFeatOrder() {
        List<SelectedFeat> selectedFeats = characterModel.feats.getFeatList();
        List<Feat> selectedFeatValues = selectedFeats.stream().map(sf -> sf.featProperty().get()).toList();

        Map<Feat, List<SelectedFeat>> rowsByFeat = new LinkedHashMap<>();
        for (SelectedFeat selectedFeat : selectedFeats) {
            rowsByFeat.computeIfAbsent(selectedFeat.featProperty().get(), f -> new ArrayList<>()).add(selectedFeat);
        }

        Map<Feat, List<Feat>> childrenByParent = new LinkedHashMap<>();
        List<Feat> roots = new ArrayList<>();
        for (Feat feat : rowsByFeat.keySet()) {
            Feat parent = parentFeat(feat, selectedFeatValues);
            if (parent == null) {
                roots.add(feat);
            } else {
                childrenByParent.computeIfAbsent(parent, p -> new ArrayList<>()).add(feat);
            }
        }
        roots.sort(Comparator.comparing(Feat::getName));
        childrenByParent.values().forEach(children -> children.sort(Comparator.comparing(Feat::getName)));

        List<SelectedFeat> ordered = new ArrayList<>();
        depthBySelectedFeat.clear();
        roots.forEach(root -> appendFeatRows(root, 0, childrenByParent, rowsByFeat, ordered));
        orderedFeats.setAll(ordered);
    }

    private void appendFeatRows(Feat feat, int depth, Map<Feat, List<Feat>> childrenByParent,
                                 Map<Feat, List<SelectedFeat>> rowsByFeat, List<SelectedFeat> ordered) {
        for (SelectedFeat row : rowsByFeat.get(feat)) {
            depthBySelectedFeat.put(row, depth);
            ordered.add(row);
        }
        childrenByParent.getOrDefault(feat, List.of())
                .forEach(child -> appendFeatRows(child, depth + 1, childrenByParent, rowsByFeat, ordered));
    }

    /**
     * Orders the selectable feats so a feat that builds on another feat is listed directly under
     * it, indented - mirroring how the Player's Handbook lists prerequisite feat chains.
     */
    private List<SelectionItem<Feat>> buildFeatSelectionItems() {
        List<Feat> candidates = characterModel.feats.getNotSelectedFeats();

        Map<Feat, List<Feat>> childrenByParent = new LinkedHashMap<>();
        List<Feat> roots = new ArrayList<>();
        for (Feat feat : candidates) {
            Feat parent = parentFeat(feat, candidates);
            if (parent == null) {
                roots.add(feat);
            } else {
                childrenByParent.computeIfAbsent(parent, p -> new ArrayList<>()).add(feat);
            }
        }
        roots.sort(Comparator.comparing(Feat::getName));
        childrenByParent.values().forEach(children -> children.sort(Comparator.comparing(Feat::getName)));

        List<SelectionItem<Feat>> items = new ArrayList<>();
        roots.forEach(root -> appendWithChildren(root, 0, childrenByParent, items));
        return items;
    }

    private void appendWithChildren(Feat feat, int depth, Map<Feat, List<Feat>> childrenByParent,
                                     List<SelectionItem<Feat>> items) {
        SelectionItem<Feat> item = toSelectionItem(feat);
        item.setDepth(depth);
        items.add(item);
        childrenByParent.getOrDefault(feat, List.of())
                .forEach(child -> appendWithChildren(child, depth + 1, childrenByParent, items));
    }

    /**
     * The feat this one builds on, if any: the first {@link FeatPrerequisite} among its
     * prerequisites (in whatever order they're listed - not necessarily the first prerequisite
     * overall), as long as that feat is itself among the currently selectable candidates.
     */
    private Feat parentFeat(Feat feat, List<Feat> candidates) {
        return feat.getPrerequisites().stream()
                .filter(FeatPrerequisite.class::isInstance)
                .map(FeatPrerequisite.class::cast)
                .findFirst()
                .map(prerequisite -> characterModel.feats.getFeatById(prerequisite.requiredFeat()))
                .filter(candidates::contains)
                .orElse(null);
    }

    private Violation findViolation(SelectedFeat selectedFeat) {
        return characterModel.featsValidation.violationsProperty().stream()
                .filter(violation -> violation.source() == selectedFeat)
                .findFirst()
                .orElse(null);
    }

    private SelectionItem<Feat> toSelectionItem(Feat feat) {
        boolean satisfied = characterModel.feats.prerequisitesSatisfied(feat);
        // shows every prerequisite, not just unmet ones - the column is "Voraussetzungen", not
        // "warum nicht wählbar"; that distinction matters for placeholder prerequisites (Zauberstufe,
        // Fertigkeit, ...) which always report satisfied=true since they're not enforced yet, but
        // should still be visible to the player
        String reason = feat.getPrerequisites().stream()
                .map(prerequisite -> PrerequisiteFormatter.toGerman(prerequisite, characterModel.feats))
                .collect(Collectors.joining(", "));
        return new SelectionItem<>(feat, feat.getName(), satisfied, reason.isEmpty() ? null : reason,
                feat.getShortDescription(), feat.getDescription());
    }
}

