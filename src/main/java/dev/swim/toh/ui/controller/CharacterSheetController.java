package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CharacterSheetController {

    @FXML
    public VBox cardsContainer;

    private final ApplicationContext context;
    private final CharacterModel characterModel;

    public CharacterSheetController(ApplicationContext context, CharacterModel characterModel) {
        this.context = context;
        this.characterModel = characterModel;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/" + cardName + ".fxml"));
            loader.setControllerFactory(context::getBean);

            Node node = loader.load();
            Object controller = loader.getController();
            System.out.println(">>> Loaded FXML for " + cardName + ", controller = " + controller);
            cardsContainer.getChildren().add(node);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load " + cardName, e);
        }
    }

    public CharacterModel getCharacterModel() {
        return characterModel;
    }
}
