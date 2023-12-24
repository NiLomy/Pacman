package ru.kpfu.itis.lobanov.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.controller.MessageReceiverController;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;
import ru.kpfu.itis.lobanov.utils.db.ServerRepository;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

public class WaitingRoomController implements MessageReceiverController {
    public static final String PLAYERS_COUNT = "Players: %d/%d";
    @FXML
    private AnchorPane screen;
    @FXML
    private Label playersWaitingLabel;
    @FXML
    private Label playersCountLabel;
    private int maxPlayersCount;
    private int userCount;
    private AppScreenVisualizer visualizer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visualizer = new AppScreenVisualizer();

        if (AppConfig.lightMode) {
            setLightTheme();
        } else {
            setDarkTheme();
        }
        PacmanClient client = PacmanApplication.getClient();
        client.setController(this);
        maxPlayersCount = ServerRepository.get(AppConfig.host, AppConfig.port).getPlayersCount();
        client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_COUNT_INFO_REQUEST, new byte[0]));
        playersCountLabel.setText(String.format(PLAYERS_COUNT, userCount, maxPlayersCount));
    }

    @Override
    public void receiveMessage(Message message) {
        if (message != null && message.getType() == MessageType.USER_COUNT_INFO_RESPONSE) {
            ByteBuffer buffer = ByteBuffer.wrap(message.getData());
            userCount = buffer.getInt();
            Platform.runLater(() -> playersCountLabel.setText(String.format(PLAYERS_COUNT, userCount, maxPlayersCount)));

            if (userCount == maxPlayersCount) {
                ServerRepository.updateGameStatus(AppConfig.host, AppConfig.port, true);
                Platform.runLater(() -> visualizer.show(GameResources.GAME_SCREEN));
            }
        }
    }

    private void setLightTheme() {
        screen.setStyle("-fx-background-color: white");
        playersWaitingLabel.setTextFill(Color.BLACK);
        playersCountLabel.setTextFill(Color.BLACK);
    }

    private void setDarkTheme() {
        screen.setStyle("-fx-background-color: black");
        playersWaitingLabel.setTextFill(Color.BLUE);
        playersCountLabel.setTextFill(Color.BLUE);
    }
}
