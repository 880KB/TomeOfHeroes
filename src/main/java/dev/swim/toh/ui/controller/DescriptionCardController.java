package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.race.Race;
import dev.swim.toh.translation.SizeTranslator;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class DescriptionCardController extends CharacterModelAware {

    @FXML public TextField ageTextField;
    @FXML public TextField sizeTextField;
    @FXML public TextField sizeCategoryTextField;
    @FXML public ComboBox<Race> raceCombo;

    public DescriptionCardController(CharacterModel characterModel) {
        super(characterModel);
    }

    @FXML
    public void initialize() {
        bindFields();
    }

    protected void bindFields() {
        System.out.println(">>> Binding fields in " + this);
        ageTextField.textProperty().bindBidirectional(characterModel.description.ageProperty());
        sizeTextField.textProperty().bindBidirectional(characterModel.description.sizeProperty());
        sizeCategoryTextField.textProperty().bind(Bindings.createStringBinding(
                () -> SizeTranslator.toGerman(characterModel.description.sizeCategoryProperty().get()),
                characterModel.description.sizeCategoryProperty()));
        raceCombo.getItems().addAll(Race.values());
        raceCombo.valueProperty().bindBidirectional(characterModel.description.raceProperty());
    }
}
