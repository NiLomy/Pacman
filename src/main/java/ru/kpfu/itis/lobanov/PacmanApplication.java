package ru.kpfu.itis.lobanov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.db.ServerRepository;

import java.util.Objects;
import java.util.ResourceBundle;

public class PacmanApplication extends Application {
    public static final String GAME_NAME = "Pacman";
    private static Stage stage;
    private static PacmanClient client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // establish connection with serverless db
        Class.forName(ServerRepository.class.getName());
        // build gui
        stage = primaryStage;
        primaryStage.setMaximized(true);
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource(GameResources.START_SCREEN));
        loader.setResources(ResourceBundle.getBundle(GameResources.LOCALIZED_TEXTS_RESOURCE_BUNDLE, AppConfig.LOCALE));

        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm());

        primaryStage.setTitle(GAME_NAME);
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
