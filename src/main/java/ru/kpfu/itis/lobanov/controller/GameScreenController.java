package ru.kpfu.itis.lobanov.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.commons.lang.SerializationUtils;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.model.entity.player.Ghost;
import ru.kpfu.itis.lobanov.model.entity.player.Pacman;
import ru.kpfu.itis.lobanov.utils.constants.Direction;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class GameScreenController implements MessageReceiverController {
    @FXML
    private Pane gameWindow;
    @FXML
    private AnchorPane gameScreen;
    @FXML
    private HBox gameInfo;
    @FXML
    private Label scoreInfo;
    @FXML
    private Label timeLabel;
    private int scores;
    private Pacman pacman;
    private List<Ghost> ghosts;
    private Direction currentDirection;
    private List<Pellet> pellets;
    private List<Bonus> bonuses;
    private PacmanClient client;
    private int userId;
    private boolean isRushMode;
    private String passedTime;
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        gameScreen.setMaxWidth((GameSettings.MAZE_SIZE) * GameSettings.CELL_SIZE);
        gameScreen.setMaxHeight((GameSettings.MAZE_SIZE + 1) * GameSettings.CELL_SIZE);
        gameScreen.setPrefWidth((GameSettings.MAZE_SIZE) * GameSettings.CELL_SIZE);
        gameScreen.setPrefHeight((GameSettings.MAZE_SIZE + 1) * GameSettings.CELL_SIZE);
        gameWindow.setStyle("-fx-background-color: lightgrey");
        ghosts = new ArrayList<>();
        isRushMode = false;

        client = PacmanApplication.getClient();
        client.setController(this);
        client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_ID_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_WALLS_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_PACMAN_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_GHOST_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_BONUSES_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_PELLETS_REQUEST, new byte[0]));

        setUpGameInfo();

        gameWindow.setOnKeyPressed(event1 -> {
            switch (event1.getCode()) {
                case UP:
                case W:
                    currentDirection = Direction.UP;
                    break;
                case DOWN:
                case S:
                    currentDirection = Direction.DOWN;
                    break;
                case LEFT:
                case A:
                    currentDirection = Direction.LEFT;
                    break;
                case RIGHT:
                case D:
                    currentDirection = Direction.RIGHT;
                    break;
            }
            if (currentDirection != null) {
                Message message = GameMessageProvider.createMessage(MessageType.MOVEMENT_REQUEST, new byte[]{currentDirection.getPositionByte()});
                client.sendMessage(message);
            }
        });
        Platform.runLater(() -> gameWindow.requestFocus());
    }

    private void setUpGameInfo() {
        Platform.runLater(() -> scoreInfo.setText(resources.getString("game.scores") + " " + scores));
    }

    private void blinkBonuses() {
        for (Bonus bonus : bonuses) {
            Platform.runLater(() -> {
                Circle bonusView = bonus.getView();
                if (bonusView.getFill() == Color.BLUE) {
                    bonusView.setFill(Color.CYAN);
                } else {
                    bonusView.setFill(Color.BLUE);
                }
            });
        }
    }

    @Override
    public void receiveMessage(Message message) {
        if (message != null) {
            ByteBuffer buffer;
            switch (message.getType()) {
                case MessageType.MOVEMENT_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    int playerId = buffer.getInt();
                    if (playerId == 0) {
                        switch (buffer.get()) {
                            case 1:
                                pacman.setCurrentDirection(Direction.UP);
                                break;
                            case 2:
                                pacman.setCurrentDirection(Direction.DOWN);
                                break;
                            case 3:
                                pacman.setCurrentDirection(Direction.LEFT);
                                break;
                            case 4:
                                pacman.setCurrentDirection(Direction.RIGHT);
                                break;
                        }
                    } else {
                        switch (buffer.get()) {
                            case 1:
                                ghosts.get(playerId - 1).setCurrentDirection(Direction.UP);
                                break;
                            case 2:
                                ghosts.get(playerId - 1).setCurrentDirection(Direction.DOWN);
                                break;
                            case 3:
                                ghosts.get(playerId - 1).setCurrentDirection(Direction.LEFT);
                                break;
                            case 4:
                                ghosts.get(playerId - 1).setCurrentDirection(Direction.RIGHT);
                                break;
                        }
                    }
                    break;
                case MessageType.USER_ID_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    userId = buffer.getInt();
                    break;
                case MessageType.CREATE_WALLS_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    List<Rectangle> rectangles = new ArrayList<>();
                    while (buffer.hasRemaining()) {
                        Rectangle rectangle = new Rectangle(buffer.getInt() * GameSettings.CELL_SIZE, buffer.getInt() * GameSettings.CELL_SIZE, GameSettings.CELL_SIZE, GameSettings.CELL_SIZE);
                        rectangles.add(rectangle);
                    }
                    Platform.runLater(() -> gameWindow.getChildren().addAll(rectangles));
                    break;
                case MessageType.CREATE_PACMAN_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    double x = buffer.getDouble();
                    double y = buffer.getDouble();
                    Maze m = (Maze) SerializationUtils.deserialize(Arrays.copyOfRange(buffer.array(), GameSettings.DOUBLE_BYTES * 2, buffer.array().length));
                    pacman = new Pacman(m);
                    pacman.setX(x);
                    pacman.setSpawnX(x);
                    pacman.setY(y);
                    pacman.setSpawnY(y);
                    pacman.show();
                    Platform.runLater(() -> gameWindow.getChildren().addAll(pacman.getView()));
                    break;
                case MessageType.CREATE_GHOST_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    int id = buffer.getInt();
                    double x2 = buffer.getDouble();
                    double y2 = buffer.getDouble();
                    Maze m2 = (Maze) SerializationUtils.deserialize(Arrays.copyOfRange(buffer.array(), GameSettings.DOUBLE_BYTES * 2 + GameSettings.INTEGER_BYTES, buffer.array().length));

                    Ghost g = new Ghost(m2);
                    g.setX(x2);
                    g.setSpawnX(x2);
                    g.setY(y2);
                    g.setSpawnY(y2);
                    switch (id) {
                        case 0:
                            g.setGhostPackageSprite("/red-ghost");
                            break;
                        case 1:
                            g.setGhostPackageSprite("/blue-ghost");
                            break;
                        case 2:
                            g.setGhostPackageSprite("/green-ghost");
                            break;
                    }
                    g.show();
                    ghosts.add(g);
                    Platform.runLater(() -> gameWindow.getChildren().addAll(g.getView()));
                    break;
                case MessageType.CREATE_BONUSES_RESPONSE:
                    bonuses = (List<Bonus>) SerializationUtils.deserialize(message.getData());
                    bonuses.forEach(Bonus::show);
                    Platform.runLater(() -> gameWindow.getChildren().addAll(bonuses.stream().map(Bonus::getView).collect(Collectors.toList())));
                    break;
                case MessageType.CREATE_PELLETS_RESPONSE:
                    pellets = (List<Pellet>) SerializationUtils.deserialize(message.getData());
                    for (Pellet p : pellets) {
                        p.show();
                    }
                    Platform.runLater(() -> gameWindow.getChildren().addAll(pellets.stream().map(Pellet::getView).collect(Collectors.toList())));
                    break;
                case MessageType.EAT_PLAYER_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    int eaterId = buffer.getInt();
                    int victimId = buffer.getInt();
                    if (eaterId == 0) {
                        Ghost gho = ghosts.get(victimId - 1);
                        Platform.runLater(() -> {
                            gho.getView().setVisible(false);
                            gho.setX(0);
                            gho.setY(0);
                            gho.setCurrentDirection(null);
                        });
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    gho.setX(gho.getSpawnX());
                                    gho.setY(gho.getSpawnY());
                                    gho.getView().setVisible(true);
                                });
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 3 * 1_000);
                        if (userId == 0) scores += GameSettings.PACMAN_EAT_GHOST_BONUS;
                    } else {
                        Platform.runLater(() -> {
                            for (Ghost gh : ghosts) {
                                gh.getView().setVisible(false);
                                gh.setX(gh.getSpawnX());
                                gh.setY(gh.getSpawnY());
                                gh.setCurrentDirection(null);
                            }
                            pacman.setCurrentDirection(null);
                            pacman.setX(pacman.getSpawnX());
                            pacman.setY(pacman.getSpawnY());
                            pacman.show();
                            for (Ghost gh : ghosts) {
                                gh.getView().setVisible(true);
                            }
                        });
                        if (userId == eaterId) scores += GameSettings.GHOST_EAT_PACMAN_BONUS;
                        pacman.setHp(pacman.getHp() - 1);
                        if (pacman.getHp() == 0) {
                            client.sendMessage(GameMessageProvider.createMessage(MessageType.GAME_END_REQUEST, new byte[]{1}));
                        }
                    }
                    break;
                case MessageType.BLINK_BONUSES_RESPONSE:
                    blinkBonuses();
                    break;
                case MessageType.PACMAN_EAT_PELLET_RESPONSE:
                    Pellet pellet = pacman.eatPellet(pellets);
                    if (pellet != null) {
                        if (userId == 0) {
                            scores += pellet.getScore();
                        }
                        Platform.runLater(() -> gameWindow.getChildren().remove(pellet.getView()));
                    }
                    break;
                case MessageType.PACMAN_EAT_BONUS_RESPONSE:
                    Bonus bonus = pacman.eatBonus(bonuses);
                    if (bonus != null) {
                        client.sendMessage(GameMessageProvider.createMessage(MessageType.RUSH_MODE_REQUEST, new byte[]{1}));
                        if (userId == 0) scores += bonus.getScore();
                        Platform.runLater(() -> gameWindow.getChildren().remove(bonus.getView()));
                    }
                    break;
                case MessageType.CHANGE_SCORES_RESPONSE:
                    setUpGameInfo();
                    break;
                case MessageType.GAME_WIN_RESPONSE:
                    if (pellets.isEmpty() && bonuses.isEmpty()) {
                        client.sendMessage(GameMessageProvider.createMessage(MessageType.GAME_END_REQUEST, new byte[]{0}));
                    }
                    break;
                case MessageType.PLAYERS_MOVE_RESPONSE:
                    Platform.runLater(pacman::go);
                    for (int i = 0; i < ghosts.size(); i++) {
                        Ghost ghos = ghosts.get(i);
                        Platform.runLater(ghos::go);
                        if (pacman.getView().getBoundsInParent().intersects(ghos.getView().getBoundsInParent())) {
                            buffer = ByteBuffer.allocate(GameSettings.DOUBLE_BYTES * 2 + GameSettings.INTEGER_BYTES * 2);
                            if (userId != 0) {
                                if (isRushMode) {
                                    buffer.putInt(0);
                                    buffer.putInt(i + 1);
                                } else {
                                    buffer.putInt(i + 1);
                                    buffer.putInt(0);
                                }
                                buffer.putDouble(pacman.getX());
                                buffer.putDouble(pacman.getY());
                                client.sendMessage(GameMessageProvider.createMessage(MessageType.EAT_PLAYER_REQUEST, buffer.array()));
                            }
                        }
                    }
                    break;
                case MessageType.RUSH_MODE_RESPONSE:
                    byte[] data = message.getData();
                    isRushMode = data[0] == 1;
                    break;
                case MessageType.GHOST_SCORES_RESPONSE:
                    if (userId != 0) {
                        scores += GameSettings.GHOST_SCORES_DEFAULT;
                    }
                    break;
                case MessageType.GAME_END_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    if (buffer.get() == 0) {
                        if (userId == 0) gameOver(resources.getString("game.win"));
                        else gameOver(resources.getString("game.lose"));
                    } else {
                        if (userId == 0) gameOver(resources.getString("game.lose"));
                        else gameOver(resources.getString("game.win"));
                    }
                    break;
                case MessageType.TIME_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    int time = buffer.getInt();
                    Platform.runLater(() -> {
                        String seconds = String.valueOf(time % GameSettings.SECONDS_IN_MINUTE);
                        if (seconds.length() == 1) seconds = '0' + seconds;
                        String minutes = String.valueOf((time / GameSettings.SECONDS_IN_MINUTE) % GameSettings.MINUTES_IN_HOUR);
                        if (minutes.length() == 1) minutes = '0' + minutes;
                        String hours = String.valueOf((time / GameSettings.SECONDS_IN_MINUTE) / GameSettings.MINUTES_IN_HOUR);
                        if (hours.length() == 1) hours = '0' + hours;
                        passedTime = String.format("%s:%s:%s", hours, minutes, seconds);
                        timeLabel.setText(passedTime);
                    });
                    break;
            }
        }
    }

    private void gameOver(String message) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Stage stage = PacmanApplication.getStage();
                    AnchorPane pane = new AnchorPane();
                    pane.setPrefWidth(600);
                    pane.setMaxHeight(400);
                    Label gameResultInfo = new Label(message);
                    gameResultInfo.setStyle("-fx-font-family: sans-serif; -fx-font-weight: bold; -fx-font-size: 26px");
                    Label stats = new Label(resources.getString("game.stats"));
                    stats.setStyle("-fx-font-size: 20 px");
                    Label playerScores = new Label(resources.getString("game.scores") + " " + scores);
                    playerScores.setStyle("-fx-font-family: sans-serif");
                    Label playerTime = new Label(resources.getString("game.played.time") + " " + passedTime);
                    playerTime.setStyle("-fx-font-family: sans-serif");
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER);
                    hBox.setSpacing(40);
                    hBox.getChildren().addAll(playerScores, playerTime);
                    Button button = new Button(resources.getString("go_back"));
                    button.setOnAction(event -> goToHomePage());
                    button.setStyle("-fx-border-radius: 5px; -fx-padding: 8px 16px; -fx-cursor: pointer; -fx-font-family: sans-serif");
                    VBox vBox = new VBox();
                    vBox.setPrefSize(600, 400);
                    vBox.setAlignment(Pos.CENTER);
                    vBox.setSpacing(20);
                    vBox.getChildren().addAll(gameResultInfo, stats, hBox, button);
                    pane.getChildren().addAll(vBox);
                    Scene scene = new Scene(pane);
                    stage.setScene(scene);
                    stage.show();
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 400);
    }

    private void goToHomePage() {
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
}
