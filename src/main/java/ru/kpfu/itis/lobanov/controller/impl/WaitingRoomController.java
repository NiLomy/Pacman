package ru.kpfu.itis.lobanov.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.controller.MessageReceiverController;
import ru.kpfu.itis.lobanov.model.dao.ServerDao;
import ru.kpfu.itis.lobanov.model.dao.impl.ServerDaoImpl;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

public class WaitingRoomController implements MessageReceiverController {
    public static final String PLAYERS_COUNT = "Players: %d/%d";
    @FXML
    private Label playersCountLabel;
    private int userCount;
    private PacmanClient client;
    private ServerDao serverDao;
    private int playersCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = PacmanApplication.getClient();
        client.setController(this);
        serverDao = new ServerDaoImpl();
        playersCount = serverDao.get(AppConfig.host, AppConfig.port).getPlayersCount();
        client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_COUNT_INFO_REQUEST, new byte[0]));
        playersCountLabel.setText(String.format(PLAYERS_COUNT, userCount, playersCount));
    }

    @Override
    public void receiveMessage(Message message) {
        if (message != null && message.getType() == MessageType.USER_COUNT_INFO_RESPONSE) {
            ByteBuffer buffer = ByteBuffer.wrap(message.getData());
            userCount = buffer.getInt();
            Platform.runLater(() -> playersCountLabel.setText(String.format(PLAYERS_COUNT, userCount, playersCount)));
            if (userCount == playersCount) {
                serverDao.updateGameStatus(AppConfig.host, AppConfig.port, true);
                Stage stage = PacmanApplication.getStage();
                FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource(GameResources.GAME_SCREEN));
                loader.setResources(ResourceBundle.getBundle(GameResources.LOCALIZED_TEXTS_RESOURCE_BUNDLE, GameSettings.LOCALE));
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
