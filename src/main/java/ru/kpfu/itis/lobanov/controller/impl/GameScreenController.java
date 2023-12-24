package ru.kpfu.itis.lobanov.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import org.apache.commons.lang.SerializationUtils;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.controller.MessageReceiverController;
import ru.kpfu.itis.lobanov.exceptions.ClientException;
import ru.kpfu.itis.lobanov.model.entity.environment.Cell;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Ghost;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Pacman;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.*;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class GameScreenController implements MessageReceiverController {
    private static final String PASSED_TIME = "%s:%s:%s";
    private static final String SPACE_DIVIDER = "%s %s";
    public static final int GAME_OVER_TIMER = 200;
    @FXML
    private Pane gameWindow;
    @FXML
    private AnchorPane gameScreen;
    @FXML
    private Label scoreInfo;
    @FXML
    private Label timeLabel;
    private PacmanClient client;
    private Pacman pacman;
    private List<Ghost> ghosts;
    private List<Pellet> pellets;
    private List<Bonus> bonuses;
    private Maze maze;

    private int scores;
    private String passedTime;
    private Direction currentDirection;
    private boolean isRushMode;

    private int userId;
    private ResourceBundle resources;
    private AppScreenVisualizer visualizer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.visualizer = new AppScreenVisualizer();

        if (AppConfig.lightMode) {
            setLightTheme();
        } else {
            setDarkTheme();
        }
        gameScreen.setMaxWidth((GameSettings.MAZE_SIZE) * AppConfig.CELL_SIZE);
        gameScreen.setMaxHeight((GameSettings.MAZE_SIZE + 1) * AppConfig.CELL_SIZE);
        gameScreen.setPrefWidth((GameSettings.MAZE_SIZE) * AppConfig.CELL_SIZE);
        gameScreen.setPrefHeight((GameSettings.MAZE_SIZE + 1) * AppConfig.CELL_SIZE);
        ghosts = new ArrayList<>();
        isRushMode = false;

        client = PacmanApplication.getClient();
        client.setController(this);
        try {
            client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_ID_REQUEST, new byte[0]));
            client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_WALLS_REQUEST, new byte[0]));
            client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_PACMAN_REQUEST, new byte[0]));
            client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_BONUSES_REQUEST, new byte[0]));
            client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_PELLETS_REQUEST, new byte[0]));
            client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_GHOST_REQUEST, new byte[0]));
        } catch (ClientException e) {
            visualizer.show(GameResources.NET_ERROR_SCREEN);
        }

        setUpGameInfo();

        gameWindow.setOnKeyPressed(event -> {
            switch (event.getCode()) {
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
                try {
                    client.sendMessage(message);
                } catch (ClientException e) {
                    visualizer.show(GameResources.NET_ERROR_SCREEN);
                }
            }
        });
        Platform.runLater(() -> gameWindow.requestFocus());
    }

    @Override
    public void receiveMessage(Message message) {
        if (message != null) {
            ByteBuffer buffer;
            double offsetX = Screen.getPrimary().getVisualBounds().getWidth() / GameSettings.SCREEN_WIDTH_DIVIDER;
            double offsetY = Screen.getPrimary().getVisualBounds().getHeight() / GameSettings.SCREEN_HEIGHT_DIVIDER;

            switch (message.getType()) {
                case MessageType.USER_ID_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    userId = buffer.getInt();
                    break;

                case MessageType.CREATE_WALLS_RESPONSE:
                    System.out.println(Arrays.toString(message.getData()));
                    maze = (Maze) SerializationUtils.deserialize(message.getData());
                    List<Rectangle> rectangles = new ArrayList<>();
                    Cell[][] cells = maze.getData();
                    for (int i = 0; i < cells.length; i++) {
                        for (int j = 0; j < cells.length; j++) {
                            if (cells[i][j].isWall()) {
                                Rectangle rectangle = new Rectangle(i * AppConfig.CELL_SIZE + offsetX, j * AppConfig.CELL_SIZE + offsetY, AppConfig.CELL_SIZE, AppConfig.CELL_SIZE);
                                if (!AppConfig.lightMode) {
                                    rectangle.setFill(Color.BLUE);
                                }
                                rectangles.add(rectangle);
                            }
                        }
                    }
                    Platform.runLater(() -> gameWindow.getChildren().addAll(rectangles));
                    break;

                case MessageType.CREATE_PACMAN_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    double x = buffer.getDouble();
                    double y = buffer.getDouble();

                    pacman = new Pacman(maze);
                    pacman.setOffsetX(offsetX);
                    pacman.setOffsetY(offsetY);
                    pacman.setX(x + offsetX);
                    pacman.setSpawnX(x + offsetX);
                    pacman.setY(y + offsetY);
                    pacman.setSpawnY(y + offsetY);
                    pacman.show();
                    Platform.runLater(() -> gameWindow.getChildren().addAll(pacman.getView()));
                    break;

                case MessageType.CREATE_BONUSES_RESPONSE:
                    bonuses = (List<Bonus>) SerializationUtils.deserialize(message.getData());
                    if (bonuses != null) {
                        for (Bonus b : bonuses) {
                            Platform.runLater(() -> b.show(Screen.getPrimary().getVisualBounds()));
                        }
                        Platform.runLater(() -> gameWindow.getChildren().addAll(bonuses.stream().map(Bonus::getView).collect(Collectors.toList())));
                    }
                    break;

                case MessageType.CREATE_PELLETS_RESPONSE:
                    pellets = (List<Pellet>) SerializationUtils.deserialize(message.getData());
                    if (pellets != null) {
                        for (Pellet p : pellets) {
                            Platform.runLater(() -> p.show(Screen.getPrimary().getVisualBounds()));
                        }
                        Platform.runLater(() -> gameWindow.getChildren().addAll(pellets.stream().map(Pellet::getView).collect(Collectors.toList())));
                    }
                    break;

                case MessageType.CREATE_GHOST_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    int id = buffer.getInt();
                    double x2 = buffer.getDouble();
                    double y2 = buffer.getDouble();

                    Ghost g;
                    switch (id) {
                        case 0:
                            g = new Ghost(maze, GameResources.RED_GHOST_PACKAGE);
                            break;
                        case 1:
                            g = new Ghost(maze, GameResources.BLUE_GHOST_PACKAGE);
                            break;
                        default:
                            g = new Ghost(maze, GameResources.GREEN_GHOST_PACKAGE);
                            break;
                    }
                    g.setOffsetX(offsetX);
                    g.setX(x2 + offsetX);
                    g.setSpawnX(x2 + offsetX);
                    g.setOffsetY(offsetY);
                    g.setY(y2 + offsetY);
                    g.setSpawnY(y2 + offsetY);
                    g.show();
                    ghosts.add(g);
                    Platform.runLater(() -> gameWindow.getChildren().addAll(g.getView()));
                    break;

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
                                try {
                                    client.sendMessage(GameMessageProvider.createMessage(MessageType.EAT_PLAYER_REQUEST, buffer.array()));
                                } catch (ClientException e) {
                                    visualizer.show(GameResources.NET_ERROR_SCREEN);
                                }
                            }
                        }
                    }
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
                        timer.schedule(task, GameSettings.GHOST_RESPAWN_DELAY);
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
                            try {
                                client.sendMessage(GameMessageProvider.createMessage(MessageType.GAME_END_REQUEST, new byte[]{1}));
                            } catch (ClientException e) {
                                visualizer.show(GameResources.NET_ERROR_SCREEN);
                            }
                        }
                    }
                    break;
                case MessageType.BLINK_BONUSES_RESPONSE:
                    blinkBonuses();
                    break;

                case MessageType.PACMAN_EAT_PELLET_RESPONSE:
                    Platform.runLater(() -> {
                        Pellet pellet = pacman.eatPellet(pellets);
                        if (pellet != null) {
                            if (userId == 0) {
                                scores += pellet.getScore();
                            }
                            Platform.runLater(() -> gameWindow.getChildren().remove(pellet.getView()));
                        }
                    });
                    break;

                case MessageType.PACMAN_EAT_BONUS_RESPONSE:
                    Platform.runLater(() -> {
                        Bonus bonus = pacman.eatBonus(bonuses);
                        if (bonus != null) {
                            try {
                                client.sendMessage(GameMessageProvider.createMessage(MessageType.RUSH_MODE_REQUEST, new byte[]{1}));
                            } catch (ClientException e) {
                                visualizer.show(GameResources.NET_ERROR_SCREEN);
                            }
                            if (userId == 0) scores += bonus.getScore();
                            Platform.runLater(() -> gameWindow.getChildren().remove(bonus.getView()));
                        }
                    });
                    break;

                case MessageType.CHANGE_SCORES_RESPONSE:
                    setUpGameInfo();
                    break;

                case MessageType.GHOST_SCORES_RESPONSE:
                    if (userId != 0) {
                        scores += GameSettings.GHOST_SCORES_DEFAULT;
                    }
                    break;

                case MessageType.RUSH_MODE_RESPONSE:
                    byte[] data = message.getData();
                    isRushMode = data[0] == 1;
                    for (Ghost ghost : ghosts) {
                        ghost.setFrightened(isRushMode);
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

                        passedTime = String.format(PASSED_TIME, hours, minutes, seconds);
                        timeLabel.setText(String.format(SPACE_DIVIDER, resources.getString(GameResources.TIME_PASSED), passedTime));
                    });
                    break;

                case MessageType.GAME_WIN_RESPONSE:
                    if (pellets.isEmpty() && bonuses.isEmpty()) {
                        try {
                            client.sendMessage(GameMessageProvider.createMessage(MessageType.GAME_END_REQUEST, new byte[]{0}));
                        } catch (ClientException e) {
                            visualizer.show(GameResources.NET_ERROR_SCREEN);
                        }
                    }
                    break;

                case MessageType.GAME_END_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    if (buffer.get() == 0) {
                        if (userId == 0) gameOver(resources.getString(GameResources.GAME_WIN));
                        else gameOver(resources.getString(GameResources.GAME_LOSE));
                    } else {
                        if (userId == 0) gameOver(resources.getString(GameResources.GAME_LOSE));
                        else gameOver(resources.getString(GameResources.GAME_WIN));
                    }
                    break;
            }
        }
    }

    private void setUpGameInfo() {
        Platform.runLater(() -> scoreInfo.setText(String.format(SPACE_DIVIDER, resources.getString(GameResources.GAME_SCORES), scores)));
    }

    private void blinkBonuses() {
        Platform.runLater(() -> {
            for (Bonus bonus : bonuses) {
                Circle bonusView = bonus.getView();
                if (bonusView.getFill() == Color.BLUE) {
                    bonusView.setFill(Color.CYAN);
                } else {
                    bonusView.setFill(Color.BLUE);
                }
            }
        });
    }

    private void gameOver(String message) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    AppConfig.gameOverMessage = message;
                    AppConfig.playerScores = scores;
                    AppConfig.timePassed = passedTime;
                    visualizer.show(GameResources.GAME_OVER_SCREEN);
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, GAME_OVER_TIMER);
    }

    private void setLightTheme() {
        gameScreen.setStyle("-fx-background-color: white");
        scoreInfo.setTextFill(Color.BLACK);
        timeLabel.setTextFill(Color.BLACK);
    }

    private void setDarkTheme() {
        gameScreen.setStyle("-fx-background-color: black");
        scoreInfo.setTextFill(Color.BLUE);
        timeLabel.setTextFill(Color.BLUE);
    }
}
