package ru.kpfu.itis.lobanov.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.commons.lang.SerializationUtils;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.model.environment.Maze;
import ru.kpfu.itis.lobanov.model.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.model.player.Ghost;
import ru.kpfu.itis.lobanov.model.player.Pacman;
import ru.kpfu.itis.lobanov.utils.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class GameScreenController implements Controller {
    @FXML
    private Pane gameWindow;
    @FXML
    private AnchorPane gameScreen;
    @FXML
    private VBox gameField;
    @FXML
    private HBox gameInfo;
    @FXML
    private Label scoreInfo;

    private Timeline timeline;
//    private final Maze maze = new Maze();
    private int scores;
    private Pacman pacman;
    private Ghost ghost;
    private Direction currentDirection;
    private byte direction;
    private List<Pellet> pellets;
    private List<Bonus> bonuses;
    private PacmanClient client;
    private int userId;

    @FXML
    private void initialize() {
        gameWindow.setStyle("-fx-background-color: lightgrey");

        client = PacmanApplication.getClient();
        client.setController(this);
        client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_ID_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_WALLS_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_PACMAN_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_GHOST_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_BONUSES_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_PELLETS_REQUEST, new byte[0]));

        setUpGameInfo();

        KeyFrame keyFrame = createKeyFrame();
        startGame(keyFrame);

        gameWindow.setOnKeyPressed(event1 -> {
            switch (event1.getCode()) {
                case UP:
                case W:
                    currentDirection = Direction.UP;
                    direction = 1;
                    break;
                case DOWN:
                case S:
                    currentDirection = Direction.DOWN;
                    direction = 2;
                    break;
                case LEFT:
                case A:
                    currentDirection = Direction.LEFT;
                    direction = 3;
                    break;
                case RIGHT:
                case D:
                    currentDirection = Direction.RIGHT;
                    direction = 4;
                    break;
            }
            if (direction != 0) {
                Message message = GameMessageProvider.createMessage(MessageType.MOVEMENT, new byte[]{direction});
                client.sendMessage(message);
            }
        });
        Platform.runLater(() -> {
            gameWindow.requestFocus();
        });
    }


//    private void drawWalls() {
////        Cell[][] cells = maze.getData();
////        for (int i = 0; i < cells.length; i++) {
////            HBox line = new HBox();
////            line.setAlignment(Pos.CENTER);
////            for (int j = 0; j < cells.length; j++) {
////                if (cells[i][j].isWall()) {
////                    Rectangle rectangle = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
////                    line.getChildren().addAll(rectangle);
////                    walls.add(rectangle);
////                } else {
////                    Rectangle rectangle = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
////                    rectangle.setFill(Color.WHITE);
////                    line.getChildren().addAll(rectangle);
////                }
////            }
////            gameField.getChildren().addAll(line);
////        }
//
//        Cell[][] cells = maze.getData();
//        for (int i = 0; i < cells.length; i++) {
//            for (int j = 0; j < cells.length; j++) {
//                if (cells[i][j].isWall()) {
//                    Rectangle rectangle = new Rectangle(i * GameSettings.CELL_SIZE, j * GameSettings.CELL_SIZE, GameSettings.CELL_SIZE, GameSettings.CELL_SIZE);
//                    gameWindow.getChildren().addAll(rectangle);
//                }
//            }
//        }
//    }

    private void setUpGameInfo() {
        scoreInfo.setText("Scores: " + scores);
    }



    private KeyFrame createKeyFrame() {
        return new KeyFrame(GameSettings.UPDATE_FREQUENCY, event -> {
            if (bonuses != null) blinkBonuses();
            if (pacman != null) pacman.go();
            if (pacman != null && ghost != null && bonuses != null && pellets != null) {
                pacman.go();
                ghost.go();
                Pellet pellet = pacman.eatPellet(pellets);
                if (pellet != null) {
                    scores += pellet.getScore();
                    gameWindow.getChildren().remove(pellet.getView());
                }
                Bonus bonus = pacman.eatBonus(bonuses);
                if (bonus != null) {
                    scores += bonus.getScore();
                    gameWindow.getChildren().remove(bonus.getView());
                }
                setUpGameInfo();

                if (pellets.isEmpty() && bonuses.isEmpty()) endGame();
            }
        });
    }

    private void blinkBonuses() {
        for (Bonus bonus : bonuses) {
            Circle bonusView = bonus.getView();
            if (bonusView.getFill() == Color.BLUE) {
                bonusView.setFill(Color.CYAN);
            } else {
                bonusView.setFill(Color.BLUE);
            }
        }
    }

    private void startGame(KeyFrame keyFrame) {
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void endGame() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeline.stop();
//                client.getThread().stop();
                GameSettings.hostsCount.set(GameSettings.hostsCount.intValue() - 1);
//                Runtime.getRuntime().exit(0);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 500);
        goToHomePage();
    }

    @Override
    public void receiveMessage(Message message) {
        if (message != null) {
            ByteBuffer buffer;
            switch (message.getType()) {
                case MessageType.MOVEMENT:
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
                    }
                    if (playerId == 1) {
                        switch (buffer.get()) {
                            case 1:
                                ghost.setCurrentDirection(Direction.UP);
                                break;
                            case 2:
                                ghost.setCurrentDirection(Direction.DOWN);
                                break;
                            case 3:
                                ghost.setCurrentDirection(Direction.LEFT);
                                break;
                            case 4:
                                ghost.setCurrentDirection(Direction.RIGHT);
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
                    Platform.runLater(() -> {
                        gameWindow.getChildren().addAll(rectangles);
                    });
                    break;
                case MessageType.CREATE_PACMAN_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    double x = buffer.getDouble();
                    double y = buffer.getDouble();
                    Maze m = (Maze) SerializationUtils.deserialize(Arrays.copyOfRange(buffer.array(), 8 * 2, buffer.array().length));
                    pacman = new Pacman(m);
                    pacman.setX(x);
                    pacman.setSpawnX(x);
                    pacman.setY(y);
                    pacman.setSpawnY(y);
                    pacman.show();
                    Platform.runLater(() -> {
                        gameWindow.getChildren().addAll(pacman.getView());
                    });
                    break;
                case MessageType.CREATE_GHOST_RESPONSE:
                    buffer = ByteBuffer.wrap(message.getData());
                    double x2 = buffer.getDouble();
                    double y2 = buffer.getDouble();
                    Maze m2 = (Maze) SerializationUtils.deserialize(Arrays.copyOfRange(buffer.array(), 8 * 2, buffer.array().length));
                    ghost = new Ghost(m2);
                    ghost.setX(x2);
                    ghost.setSpawnX(x2);
                    ghost.setY(y2);
                    ghost.setSpawnY(y2);
                    ghost.show();
                    Platform.runLater(() -> {
                        gameWindow.getChildren().addAll(ghost.getView());
                    });
                    break;
                case MessageType.CREATE_BONUSES_RESPONSE:
                    bonuses = (List<Bonus>) SerializationUtils.deserialize(message.getData());
                    bonuses.stream().forEach(Bonus::show);
                    Platform.runLater(() -> {
                        gameWindow.getChildren().addAll(bonuses.stream().map(Bonus::getView).collect(Collectors.toList()));
                    });
                    break;
                case MessageType.CREATE_PELLETS_RESPONSE:
                    pellets = (List<Pellet>) SerializationUtils.deserialize(message.getData());
                    pellets.stream().forEach(Pellet::show);
                    Platform.runLater(() -> {
                        gameWindow.getChildren().addAll(pellets.stream().map(Pellet::getView).collect(Collectors.toList()));
                    });
                    break;
            }
        }
    }

    private void goToHomePage() {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/start_screen.fxml"));
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
