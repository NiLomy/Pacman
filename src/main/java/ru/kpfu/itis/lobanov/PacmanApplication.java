package ru.kpfu.itis.lobanov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PacmanApplication extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/start_screen.fxml"));

        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);

        primaryStage.setTitle("Pacman");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Stage getStage() {
        return stage;
    }
}
