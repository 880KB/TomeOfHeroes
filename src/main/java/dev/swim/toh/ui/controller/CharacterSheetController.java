package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
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
        loadCard("saving-throws-view");
        Node classNode = getNode("classes-view");
        Node featsView = getNode("feats-view");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(classNode, featsView);
        hBox.setSpacing(10);
        cardsContainer.getChildren().add(hBox);
    }

    private void loadCard(String cardName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/" + cardName + ".fxml"));
            loader.setControllerFactory(context::getBean);
            Node node = loader.load();
            cardsContainer.getChildren().add(node);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load " + cardName, e);
        }
    }

    private Node getNode(String cardName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/" + cardName + ".fxml"));
            loader.setControllerFactory(context::getBean);
            return loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load " + cardName, e);
        }
    }

    public CharacterModel getCharacterModel() {
        return characterModel;
    }
}
