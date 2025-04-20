package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.Character;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CharacterSheetController {

    @FXML
    public VBox cardsContainer;

    private final Character character;

    public CharacterSheetController() {
        character = new Character();
    }

    @FXML
    public void initialize() {
        loadCard("attributes-view");
        loadCard("classes-view");
        loadCard("description-card");
        loadCard("saving-throws-view");
    }

    private void loadCard(String cardName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/swim/toh/ui/view/" + cardName + ".fxml"));
            TitledPane pane = loader.load();
            Object controller = loader.getController();
            System.out.println(">>> Loaded FXML for " + cardName + ", controller = " + controller);
            if (controller instanceof CharacterModelAware cma) {
                cma.setCharacterModel(character);
            }
            cardsContainer.getChildren().add(pane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
