package dev.swim.toh.app;

import dev.swim.toh.model.core.CharacterModelFactory;
import dev.swim.toh.persistence.CharacterFileService;
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
        CharacterFileService characterFileService = TomeOfHeroesApplication.getContext().getBean(CharacterFileService.class);

        FXMLLoader fxmlLoader = new FXMLLoader(TomeOfHeroesApp.class.getResource("/fxml/main-window.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new MainWindowController(characterModelFactory, characterFileService));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1400, 1000);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());

        stage.setTitle("Tome of Heroes");
        stage.setScene(scene);
        stage.show();
    }
}
