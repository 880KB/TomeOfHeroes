package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.weapons.SelectedWeaponSlot;
import dev.swim.toh.model.core.weapons.WeaponSlot;
import dev.swim.toh.model.data.weapon.Weapon;
import dev.swim.toh.model.data.weapon.WeaponHandedness;
import dev.swim.toh.model.data.weapon.WeaponProficiency;
import dev.swim.toh.model.data.weapon.WeaponRangeType;
import dev.swim.toh.model.util.javafx.Layout;
import dev.swim.toh.model.util.javafx.Styles;
import dev.swim.toh.translation.DamageTypeTranslator;
import dev.swim.toh.translation.WeaponHandednessTranslator;
import dev.swim.toh.translation.WeaponProficiencyTranslator;
import dev.swim.toh.translation.WeaponSlotTranslator;
import dev.swim.toh.ui.controller.dialog.SelectionDialogController;
import dev.swim.toh.ui.controller.dialog.SelectionItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WeaponsViewController extends CharacterModelAware {

    public TableView<SelectedWeaponSlot> weaponsTableView;
    public TableColumn<SelectedWeaponSlot, WeaponSlot> slotColumn;
    public TableColumn<SelectedWeaponSlot, Weapon> weaponColumn;
    public TableColumn<SelectedWeaponSlot, Weapon> damageColumn;
    public TableColumn<SelectedWeaponSlot, Weapon> criticalColumn;
    public TableColumn<SelectedWeaponSlot, Weapon> rangeColumn;
    public TableColumn<SelectedWeaponSlot, Weapon> damageTypeColumn;
    public TableColumn<SelectedWeaponSlot, Void> attackBonusColumn;
    public TableColumn<SelectedWeaponSlot, String> noteColumn;
    public TableColumn<SelectedWeaponSlot, Void> actionColumn;

    public WeaponsViewController(CharacterModel characterModel) {
        super(characterModel);
    }

    @FXML
    public void initialize() {
        bindFields();
    }

    public void bindFields() {
        weaponsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        weaponsTableView.setItems(characterModel.weapons.getSlots());
        Layout.updateTableHeight(weaponsTableView);

        slotColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSlot()));
        slotColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(WeaponSlot slot, boolean empty) {
                super.updateItem(slot, empty);
                setText(empty || slot == null ? null : WeaponSlotTranslator.toGerman(slot));
            }
        });

        weaponColumn.setCellValueFactory(cellData -> cellData.getValue().weaponProperty());
        weaponColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Weapon weapon, boolean empty) {
                super.updateItem(weapon, empty);
                setText(empty ? null : (weapon == null ? "–" : weapon.getName()));
            }
        });

        damageColumn.setCellValueFactory(cellData -> cellData.getValue().weaponProperty());
        damageColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Weapon weapon, boolean empty) {
                super.updateItem(weapon, empty);
                setText(empty || weapon == null ? null : weapon.getDamageMedium());
            }
        });

        criticalColumn.setCellValueFactory(cellData -> cellData.getValue().weaponProperty());
        criticalColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Weapon weapon, boolean empty) {
                super.updateItem(weapon, empty);
                setText(empty || weapon == null ? null : formatCritical(weapon));
            }
        });

        rangeColumn.setCellValueFactory(cellData -> cellData.getValue().weaponProperty());
        rangeColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Weapon weapon, boolean empty) {
                super.updateItem(weapon, empty);
                setText(empty || weapon == null ? null :
                        (weapon.getRangeIncrement() == null ? "–" : weapon.getRangeIncrement() + "m"));
            }
        });

        damageTypeColumn.setCellValueFactory(cellData -> cellData.getValue().weaponProperty());
        damageTypeColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Weapon weapon, boolean empty) {
                super.updateItem(weapon, empty);
                setText(empty || weapon == null ? null : formatDamageTypes(weapon));
            }
        });

        // no attack-bonus formula exists yet (no base-attack-bonus-by-class/level system) - shown
        // as a visible placeholder rather than silently omitted, same idea as the "Magie-Mod"
        // placeholder on saving throws
        attackBonusColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : "–");
                setTooltip(empty ? null : new Tooltip("Noch nicht berechnet (kein Grund-Angriffsbonus-System vorhanden)"));
            }
        });

        noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
        noteColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        noteColumn.setOnEditCommit(event -> event.getRowValue().noteProperty().set(event.getNewValue()));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button chooseButton = new Button();
            private final Button clearButton = new Button();
            private final HBox content = new HBox(2, chooseButton, clearButton);
            {
                FontIcon chooseIcon = new FontIcon(FontAwesomeSolid.EXCHANGE_ALT);
                chooseIcon.setIconSize(14);
                chooseIcon.setIconColor(Paint.valueOf("gray"));
                chooseIcon.setTranslateY(-1);
                chooseButton.setGraphic(chooseIcon);
                chooseButton.getStyleClass().add("icon-button");

                FontIcon clearIcon = new FontIcon(FontAwesomeSolid.TRASH_ALT);
                clearIcon.setIconSize(14);
                clearIcon.setIconColor(Paint.valueOf("gray"));
                clearIcon.setTranslateY(-1);
                clearButton.setGraphic(clearIcon);
                clearButton.getStyleClass().add("icon-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                chooseButton.disableProperty().unbind();
                clearButton.disableProperty().unbind();
                if (empty) {
                    setGraphic(null);
                    return;
                }
                SelectedWeaponSlot selectedWeaponSlot = getTableRow().getItem();
                if (selectedWeaponSlot.getSlot() == WeaponSlot.OFF_HAND) {
                    Tooltip lockedTooltip = new Tooltip("Nicht verfügbar: Haupthand-Waffe ist zweihändig");
                    chooseButton.disableProperty().bind(characterModel.weapons.offHandLockedProperty());
                    clearButton.disableProperty().bind(characterModel.weapons.offHandLockedProperty()
                            .or(selectedWeaponSlot.weaponProperty().isNull()));
                    chooseButton.setTooltip(lockedTooltip);
                } else {
                    chooseButton.setDisable(false);
                    chooseButton.setTooltip(null);
                    clearButton.disableProperty().bind(selectedWeaponSlot.weaponProperty().isNull());
                }
                chooseButton.setOnAction(event -> showWeaponSelectionDialog(selectedWeaponSlot));
                clearButton.setOnAction(event -> characterModel.weapons.setWeapon(selectedWeaponSlot.getSlot(), (Weapon) null));
                setGraphic(content);
            }
        });
    }

    private String formatCritical(Weapon weapon) {
        String range = weapon.getCriticalThreatRange() == 20 ? "20" : weapon.getCriticalThreatRange() + "-20";
        return range + "/x" + weapon.getCriticalMultiplier();
    }

    private String formatDamageTypes(Weapon weapon) {
        String joiner = weapon.isDamageTypeChoice() ? " oder " : " und ";
        return weapon.getDamageTypes().stream()
                .map(DamageTypeTranslator::toGerman)
                .sorted()
                .collect(Collectors.joining(joiner));
    }

    private void showWeaponSelectionDialog(SelectedWeaponSlot selectedWeaponSlot) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog/selection-dialog.fxml"));
        try {
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Waffe auswählen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.weaponsTableView.getScene().getWindow());
            Scene scene = new Scene(page);
            Styles.applyAppStylesheet(scene);
            dialogStage.setScene(scene);

            WeaponRangeType rangeType = selectedWeaponSlot.getSlot() == WeaponSlot.RANGED
                    ? WeaponRangeType.RANGED : WeaponRangeType.MELEE;
            Weapon currentWeapon = selectedWeaponSlot.weaponProperty().get();
            List<SelectionItem<Weapon>> items = buildGroupedWeaponItems(characterModel.weapons.getWeaponsByRangeType(rangeType));

            SelectionDialogController<Weapon> dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setSingleSelect(true);
            dialogController.setItems(items);
            if (currentWeapon != null)
                dialogController.preselect(currentWeapon);

            dialogStage.showAndWait();

            List<Weapon> selected = dialogController.getSelectedValues();
            if (!selected.isEmpty()) {
                Weapon weapon = selected.get(0);
                // a two-handed weapon can't actually be "in the off hand" - if one was picked
                // from the off-hand slot's own dialog, it belongs in the main hand instead (which
                // then clears the off hand itself, see WeaponsComputed)
                WeaponSlot targetSlot = selectedWeaponSlot.getSlot() == WeaponSlot.OFF_HAND
                        && weapon.getHandedness() == WeaponHandedness.TWO_HANDED
                        ? WeaponSlot.MAIN_HAND : selectedWeaponSlot.getSlot();
                characterModel.weapons.setWeapon(targetSlot, weapon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Groups weapons into non-selectable section headers matching the PHB table (proficiency,
     * then - for melee - handedness; {@code weapons} must already be sorted that way, see
     * {@link dev.swim.toh.model.core.weapons.Weapons#getWeaponsByRangeType}) - a header is
     * inserted whenever the group changes, not once up front, so an empty group (e.g. no exotic
     * ranged weapons owned) simply produces no header for it.
     */
    private List<SelectionItem<Weapon>> buildGroupedWeaponItems(List<Weapon> weapons) {
        List<SelectionItem<Weapon>> items = new ArrayList<>();
        WeaponProficiency currentProficiency = null;
        WeaponHandedness currentHandedness = null;
        for (Weapon weapon : weapons) {
            if (weapon.getProficiency() != currentProficiency) {
                currentProficiency = weapon.getProficiency();
                currentHandedness = null;
                items.add(SelectionItem.header(WeaponProficiencyTranslator.toGerman(currentProficiency)));
            }
            if (weapon.getHandedness() != null && weapon.getHandedness() != currentHandedness) {
                currentHandedness = weapon.getHandedness();
                SelectionItem<Weapon> handednessHeader = SelectionItem.header(WeaponHandednessTranslator.toGerman(currentHandedness));
                handednessHeader.setDepth(1);
                items.add(handednessHeader);
            }
            SelectionItem<Weapon> item = new SelectionItem<>(weapon, weapon.getName(), true, null,
                    weapon.getShortDescription(), weapon.getDescription());
            item.setDepth(weapon.getHandedness() != null ? 2 : 1);
            items.add(item);
        }
        return items;
    }
}
