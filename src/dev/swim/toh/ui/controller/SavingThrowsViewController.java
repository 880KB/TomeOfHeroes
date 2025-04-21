package dev.swim.toh.ui.controller;

import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.savingthrow.SavingThrow;
import dev.swim.toh.model.util.javafx.Bind;
import dev.swim.toh.model.util.javafx.Format;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Map;

public class SavingThrowsViewController extends CharacterModelAware {

    public Button testButton;

    public TextField totalReflex;
    public TextField totalWill;
    public TextField totalFortitude;
    public TextField baseSaveReflex;
    public TextField baseSaveWill;
    public TextField baseSaveFortitude;
    public TextField attrModReflex;
    public TextField attrModWill;
    public TextField attrModFortitude;
    public TextField magicModReflex;
    public TextField magicModWill;
    public TextField magicModFortitude;
    public TextField miscModReflex;
    public TextField miscModWill;
    public TextField miscModFortitude;

    private Map<SavingThrow, TextField> savingThrowTotalTextFieldMap;
    private Map<SavingThrow, TextField> savingThrowBaseTextFieldMap;
    private Map<SavingThrow, TextField> savingThrowAttributeModTextFieldMap;
    private Map<SavingThrow, TextField> savingThrowMagicModTextFieldMap;
    private Map<SavingThrow, TextField> savingThrowMiscModTextFieldMap;

    protected void bindFields() {
        savingThrowTotalTextFieldMap = Map.of(
                SavingThrow.REFLEX, totalReflex,
                SavingThrow.WILL, totalWill,
                SavingThrow.FORTITUDE, totalFortitude
        );
        savingThrowBaseTextFieldMap = Map.of(
                SavingThrow.REFLEX, baseSaveReflex,
                SavingThrow.WILL, baseSaveWill,
                SavingThrow.FORTITUDE, baseSaveFortitude
        );
        savingThrowAttributeModTextFieldMap = Map.of(
                SavingThrow.REFLEX, attrModReflex,
                SavingThrow.WILL, attrModWill,
                SavingThrow.FORTITUDE, attrModFortitude
        );
        savingThrowMagicModTextFieldMap = Map.of(
                SavingThrow.REFLEX, magicModReflex,
                SavingThrow.WILL, magicModWill,
                SavingThrow.FORTITUDE, magicModFortitude
        );
        savingThrowMiscModTextFieldMap = Map.of(
                SavingThrow.REFLEX, miscModReflex,
                SavingThrow.WILL, miscModWill,
                SavingThrow.FORTITUDE, miscModFortitude
        );

        for (SavingThrow savingThrow : SavingThrow.values()) {
            Bind.bindIntegerPropertyToTextField(characterModel.savingThrows.getSavingThrowTotalProperty(savingThrow),
                    savingThrowTotalTextFieldMap.get(savingThrow),
                    Format.getIntegerFilter());
            Bind.bindIntegerPropertyToTextField(characterModel.savingThrows.getSavingThrowBaseProperty(savingThrow),
                    savingThrowBaseTextFieldMap.get(savingThrow),
                    Format.getIntegerFilter());
            Bind.bindIntegerPropertyToTextField(characterModel.savingThrows.getSavingThrowAttributeModProperty(savingThrow),
                    savingThrowAttributeModTextFieldMap.get(savingThrow),
                    Format.getIntegerFilter());
            Bind.bindIntegerPropertyToTextField(characterModel.savingThrows.getSavingThrowMagicModProperty(savingThrow),
                    savingThrowMagicModTextFieldMap.get(savingThrow),
                    Format.getIntegerFilter());
            Bind.bindIntegerPropertyToTextField(characterModel.savingThrows.getSavingThrowMiscModProperty(savingThrow),
                    savingThrowMiscModTextFieldMap.get(savingThrow),
                    Format.getIntegerFilter());
        }
    }

    public void handleTestButtonClicked(ActionEvent actionEvent) {
        characterModel.print();
    }
}
