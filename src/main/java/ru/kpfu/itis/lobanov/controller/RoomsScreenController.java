package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.exceptions.ClientException;
import ru.kpfu.itis.lobanov.model.dao.ServerDao;
import ru.kpfu.itis.lobanov.model.dao.impl.ServerDaoImpl;
import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RoomsScreenController implements Controller {
    @FXML
    private VBox panel;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button backBtn;
    private static PacmanClient client;
    private ServerDao serverDao;
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        refreshBtn.setOnAction(event -> refreshPage());
        backBtn.setOnAction(event -> goBack());
        serverDao = new ServerDaoImpl();
        List<ServerModel> servers = serverDao.getAll();
        for (int i = 0; i < servers.size(); i++) {
            ServerModel server = servers.get(i);
            if (!server.isGameHeld()) createRoom(server.getHost(), server.getPort(), i + 1);
        }
    }

    private void createRoom(String host, int port, int position) {
        Button room = new Button(resources.getString("room.enter") + position);
        room.setOnAction(event -> {
            if (serverDao.get(host, port).isGameHeld()) {
                showFullRoomScreen();
            } else {
                Stage stage = PacmanApplication.getStage();
                FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/waiting_room.fxml"));
                loader.setResources(ResourceBundle.getBundle("game_strings", GameSettings.LOCALE));
                try {
                    AppConfig.host = host;
                    AppConfig.port = port;
                    client = new PacmanClient(host, port);
                    PacmanApplication.setClient(client);
                    client.connect();
                    AnchorPane pane = loader.load();
                    Scene scene = new Scene(pane);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClientException e) {
                    refreshPage();
                }
            }
        });
        panel.getChildren().addAll(room);
    }

    private void refreshPage() {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/rooms_screen.fxml"));
        loader.setResources(ResourceBundle.getBundle("game_strings", GameSettings.LOCALE));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void goBack() {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/start_screen.fxml"));
        loader.setResources(ResourceBundle.getBundle("game_strings", GameSettings.LOCALE));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showFullRoomScreen() {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/full_room_screen.fxml"));
        loader.setResources(ResourceBundle.getBundle("game_strings", GameSettings.LOCALE));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
