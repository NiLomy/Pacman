package ru.kpfu.itis.lobanov.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.utils.AppConfig;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.GameSettings;
import ru.kpfu.itis.lobanov.utils.MessageType;

import java.io.IOException;
import java.nio.ByteBuffer;

public class WaitingRoomController implements Controller {
    @FXML
    private Button start;
    private int userCount;
    private PacmanClient client;

    @FXML
    private void initialize() {
        client = PacmanApplication.getClient();
        client.setController(this);
        client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_COUNT_INFO_REQUEST, new byte[0]));

        start.setOnAction(event -> {
            AppConfig.setHost(AppConfig.CURRENT_HOST);
            AppConfig.setPort(AppConfig.CURRENT_PORT_1);
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/game_screen.fxml"));
            try {
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void receiveMessage(Message message) {
        if (message != null && message.getType() == MessageType.USER_COUNT_INFO_RESPONSE) {
            ByteBuffer buffer = ByteBuffer.wrap(message.getData());
            userCount = buffer.getInt();
            if (userCount >= GameSettings.PLAYERS_COUNT) {
//                start.setDisable(false);
                Stage stage = PacmanApplication.getStage();
                FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/game_screen.fxml"));
                Platform.runLater(() -> {
                    try {
                        AnchorPane pane = loader.load();
                        Scene scene = new Scene(pane);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
