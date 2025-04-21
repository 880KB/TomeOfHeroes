package dev.swim.toh.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TomeOfHeroesApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TomeOfHeroesApp.class.getResource("/dev/swim/toh/ui/view/character-sheet.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1400, 1000);
        stage.setTitle("Tome of Heroes");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}