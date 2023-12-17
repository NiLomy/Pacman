package ru.kpfu.itis.lobanov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.util.ResourceBundle;

public class PacmanApplication extends Application {
    private static Stage stage;
    private static PacmanClient client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setMaximized(true);
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource(GameResources.START_SCREEN));
        loader.setResources(ResourceBundle.getBundle(GameResources.LOCALIZED_TEXTS_RESOURCE_BUNDLE, GameSettings.LOCALE));

        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        primaryStage.setTitle("Pacman");
        primaryStage.setOnCloseRequest(e -> {
            if (client != null && client.getThread() != null) {
//                client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_COUNT_INFO_REQUEST, new byte[0]));
                client.getThread().stop();
            }
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
