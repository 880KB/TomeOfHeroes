package dev.swim.toh.app;

import dev.swim.toh.model.core.CharacterModelFactory;
import dev.swim.toh.ui.controller.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TomeOfHeroesApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        CharacterModelFactory characterModelFactory = TomeOfHeroesApplication.getContext().getBean(CharacterModelFactory.class);

        FXMLLoader fxmlLoader = new FXMLLoader(TomeOfHeroesApp.class.getResource("/fxml/main-window.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new MainWindowController(characterModelFactory));

        Parent root = fxmlLoader.load();
        stage.setTitle("Tome of Heroes");
        stage.setScene(new Scene(root, 1400, 1000));
        stage.show();
    }
}
