package dev.swim.toh.ui.controller;

import dev.swim.toh.model.core.CharacterModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;

public class CharacterSheetController {

    @FXML
    public VBox headerContainer;
    @FXML
    public VBox sectionsContainer;

    private final CharacterModel characterModel;

    public CharacterSheetController(CharacterModel characterModel) {
        this.characterModel = characterModel;
    }

    @FXML
    public void initialize() {
        headerContainer.getChildren().addAll(getNode("character-header"), getNode("description-card"));

        VBox combatValues = new VBox(10, getNode("hit-points-view"), getNode("armor-class-view"), getNode("initiative-view"));
        HBox kampf = new HBox(15, getNode("attributes-view"), combatValues);
        Node savingThrows = getNode("saving-throws-view");
        HBox talente = new HBox(15, getNode("classes-view"), getNode("feats-view"));
        talente.setFillHeight(false);
        talente.setAlignment(Pos.TOP_LEFT);
        Node weapons = getNode("weapons-view");

        sectionsContainer.getChildren().addAll(kampf, savingThrows, talente, weapons);
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
     * Every card controller only needs this character's CharacterModel, so they are constructed
     * directly here instead of being resolved as Spring-managed singletons - each open character
     * needs its own independent set of controller instances.
     */
    private Callback<Class<?>, Object> controllerFactory() {
        return controllerClass -> {
            if (controllerClass == CharacterHeaderController.class)
                return new CharacterHeaderController(characterModel);
            if (controllerClass == DescriptionCardController.class)
                return new DescriptionCardController(characterModel);
            if (controllerClass == AttributesViewController.class)
                return new AttributesViewController(characterModel);
            if (controllerClass == HitPointsViewController.class)
                return new HitPointsViewController(characterModel);
            if (controllerClass == ArmorClassViewController.class)
                return new ArmorClassViewController(characterModel);
            if (controllerClass == InitiativeViewController.class)
                return new InitiativeViewController(characterModel);
            if (controllerClass == SavingThrowsViewController.class)
                return new SavingThrowsViewController(characterModel);
            if (controllerClass == ClassesViewController.class)
                return new ClassesViewController(characterModel);
            if (controllerClass == FeatsViewController.class)
                return new FeatsViewController(characterModel);
            if (controllerClass == WeaponsViewController.class)
                return new WeaponsViewController(characterModel);
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
