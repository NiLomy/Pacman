package ru.kpfu.itis.lobanov.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.model.dao.ServerDao;
import ru.kpfu.itis.lobanov.model.dao.impl.ServerDaoImpl;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

public class WaitingRoomController implements MessageReceiverController {
    private int userCount;
    private PacmanClient client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = PacmanApplication.getClient();
        client.setController(this);
        client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_COUNT_INFO_REQUEST, new byte[0]));
    }

    @Override
    public void receiveMessage(Message message) {
        if (message != null && message.getType() == MessageType.USER_COUNT_INFO_RESPONSE) {
            ByteBuffer buffer = ByteBuffer.wrap(message.getData());
            userCount = buffer.getInt();
            if (userCount == GameSettings.PLAYERS_COUNT) {
                ServerDao serverDao = new ServerDaoImpl();
                serverDao.updateGameStatus(AppConfig.host, AppConfig.port, true);
                Stage stage = PacmanApplication.getStage();
                FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/view/game_screen.fxml"));
                loader.setResources(ResourceBundle.getBundle("property/game_strings", GameSettings.LOCALE));
                Platform.runLater(() -> {
                    try {
                        AnchorPane pane = loader.load();
                        Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                        stage.setScene(scene);
                        stage.setMaximized(true);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
