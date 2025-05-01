package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.Character;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
        loadCard("description-card");
        loadCard("attributes-view");
        loadCard("classes-view");
        loadCard("feats-view");
        loadCard("saving-throws-view");
    }

    private void loadCard(String cardName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/swim/toh/ui/view/" + cardName + ".fxml"));
            Node node = loader.load();
            Object controller = loader.getController();
            System.out.println(">>> Loaded FXML for " + cardName + ", controller = " + controller);
            if (controller instanceof CharacterModelAware cma) {
                cma.setCharacterModel(character);
            }
            cardsContainer.getChildren().add(node);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
