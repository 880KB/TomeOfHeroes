package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.ui.controller.dialog.FeatSelectionDialogController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;

public class CharacterSheetController {

    @FXML
    public VBox cardsContainer;

    private final CharacterModel characterModel;

    public CharacterSheetController(CharacterModel characterModel) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + cardName + ".fxml"));
            loader.setControllerFactory(controllerFactory());
            Node node = loader.load();
            cardsContainer.getChildren().add(node);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load " + cardName, e);
        }
    }

    private Node getNode(String cardName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + cardName + ".fxml"));
            loader.setControllerFactory(controllerFactory());
            return loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load " + cardName, e);
        }
    }

    /**
     * Every card controller only needs this tab's CharacterModel, so they are constructed
     * directly here instead of being resolved as Spring-managed singletons - each open character
     * tab needs its own independent set of controller instances.
     */
    private Callback<Class<?>, Object> controllerFactory() {
        return controllerClass -> {
            if (controllerClass == DescriptionCardController.class)
                return new DescriptionCardController(characterModel);
            if (controllerClass == AttributesViewController.class)
                return new AttributesViewController(characterModel);
            if (controllerClass == SavingThrowsViewController.class)
                return new SavingThrowsViewController(characterModel);
            if (controllerClass == ClassesViewController.class)
                return new ClassesViewController(characterModel);
            if (controllerClass == FeatsViewController.class)
                return new FeatsViewController(characterModel);
            if (controllerClass == FeatSelectionDialogController.class)
                return new FeatSelectionDialogController();
            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("No factory registered for controller " + controllerClass, e);
            }
        };
    }

    public CharacterModel getCharacterModel() {
        return characterModel;
    }
}
