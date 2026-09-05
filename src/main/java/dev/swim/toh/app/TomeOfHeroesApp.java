package dev.swim.toh.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class TomeOfHeroesApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TomeOfHeroesApp.class.getResource("/fxml/character-sheet.fxml"));
        fxmlLoader.setControllerFactory(TomeOfHeroesApplication.getContext()::getBean);

        Scene scene = new Scene(fxmlLoader.load(), 1400, 1000);
        stage.setTitle("Tome of Heroes");
        stage.setScene(scene);
        stage.show();
    }
}