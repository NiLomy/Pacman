package ru.kpfu.itis.lobanov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.utils.AppConfig;
import ru.kpfu.itis.lobanov.utils.GameSettings;
import ru.kpfu.itis.lobanov.utils.MessageType;

import java.util.Arrays;

public class PacmanApplication extends Application {
    private static Stage stage;
    private static PacmanClient client;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/start_screen.fxml"));

        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);

        client = new PacmanClient(AppConfig.CURRENT_HOST, AppConfig.CURRENT_PORT_1);
//        PacmanApplication.setClient(client);
        client.connect();

        primaryStage.setTitle("Pacman");
        primaryStage.setOnCloseRequest(e -> {
            if (client != null && client.getThread() != null) client.getThread().stop();
            System.exit(0);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static PacmanClient getClient() {
        return client;
    }

    public static void setClient(PacmanClient client) {
        PacmanApplication.client = client;
    }
}
