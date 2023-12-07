package ru.kpfu.itis.lobanov.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import ru.kpfu.itis.lobanov.utils.Direction;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.GameSettings;
import ru.kpfu.itis.lobanov.utils.MessageType;

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
    @FXML
    private VBox gameEndWindow;
    @FXML
    private Button gameEndButton;
    @FXML
    private Label gameEndLabel;

    private Timeline timeline;
    //    private final Maze maze = new Maze();
    private int scores;
    private Pacman pacman;
    private List<Ghost> ghosts;
    private Direction currentDirection;
    private List<Pellet> pellets;
    private List<Bonus> bonuses;
    private PacmanClient client;
    private int userId;
    private boolean isRushMode = false;

    @FXML
    private void initialize() {
        gameWindow.setStyle("-fx-background-color: lightgrey");
        ghosts = new ArrayList<>();

        client = PacmanApplication.getClient();
        client.setController(this);
        client.sendMessage(GameMessageProvider.createMessage(MessageType.USER_ID_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_WALLS_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_PACMAN_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_GHOST_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_BONUSES_REQUEST, new byte[0]));
        client.sendMessage(GameMessageProvider.createMessage(MessageType.CREATE_PELLETS_REQUEST, new byte[0]));

        setUpGameInfo();

//        KeyFrame keyFrame = createKeyFrame();
//        startGame(keyFrame);

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
        Platform.runLater(() -> scoreInfo.setText("Scores: " + scores));
    }

//    private KeyFrame createKeyFrame() {
//        return new KeyFrame(GameSettings.UPDATE_FREQUENCY, event -> {
//            if (bonuses != null) blinkBonuses();
//            if (pacman != null && ghost != null && bonuses != null && pellets != null) {
//                pacman.go();
//                ghost.go();
//                Pellet pellet = pacman.eatPellet(pellets);
//                if (pellet != null) {
//                    scores += pellet.getScore();
//                    gameWindow.getChildren().remove(pellet.getView());
//                }
//                Bonus bonus = pacman.eatBonus(bonuses);
//                if (bonus != null) {
//                    scores += bonus.getScore();
//                    gameWindow.getChildren().remove(bonus.getView());
//                }
//                if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY()) {
//                    ByteBuffer buffer = ByteBuffer.allocate(8 * 2 + 4 * 2);
//                    buffer.putInt(1);
//                    buffer.putInt(0);
//                    buffer.putDouble(pacman.getX());
//                    buffer.putDouble(pacman.getY());
//                    client.sendMessage(GameMessageProvider.createMessage(MessageType.EAT_PLAYER_REQUEST, buffer.array()));
//                }
//
//                setUpGameInfo();
//
//                if (pellets.isEmpty() && bonuses.isEmpty()) endGameWin();
//            }
//        });
//    }

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

    private void endGameWin() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    gameEndWindow.setVisible(true);
                    gameEndButton.setOnAction(event -> {
                        goToHomePage();
                    });
                    gameEndLabel.setText("You win the game");
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 200);
    }

    private void endGameLoose() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    gameEndWindow.setVisible(true);
                    gameEndButton.setOnAction(event -> {
                        goToHomePage();
                    });
                    gameEndLabel.setText("You lost the game");
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 200);
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
//                    if (playerId == 1) {
//                        switch (buffer.get()) {
//                            case 1:
//                                ghost.setCurrentDirection(Direction.UP);
//                                break;
//                            case 2:
//                                ghost.setCurrentDirection(Direction.DOWN);
//                                break;
//                            case 3:
//                                ghost.setCurrentDirection(Direction.LEFT);
//                                break;
//                            case 4:
//                                ghost.setCurrentDirection(Direction.RIGHT);
//                                break;
//                        }
//                    }
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
                    Maze m = (Maze) SerializationUtils.deserialize(Arrays.copyOfRange(buffer.array(), 8 * 2, buffer.array().length));
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
                    double x2 = buffer.getDouble();
                    double y2 = buffer.getDouble();
                    Maze m2 = (Maze) SerializationUtils.deserialize(Arrays.copyOfRange(buffer.array(), 8 * 2, buffer.array().length));
                    Ghost g = new Ghost(m2);
                    g.setX(x2);
                    g.setSpawnX(x2);
                    g.setY(y2);
                    g.setSpawnY(y2);
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
                    switch (eaterId) {
                        case 0:
                            Ghost gho = ghosts.get(victimId);
                            gho.getView().setVisible(false);
                            gho.setX(0);
                            gho.setY(0);
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    gho.setX(gho.getSpawnX());
                                    gho.setY(gho.getSpawnY());
                                    gho.getView().setVisible(true);
                                }
                            };
                            Timer timer = new Timer();
                            timer.schedule(task, 3 * 1_000);
                            scores += 500;
                            break;
                        default:
                            pacman.setX(pacman.getSpawnX());
                            pacman.setY(pacman.getSpawnY());
                            for (Ghost gh : ghosts) {
                                gh.setX(gh.getSpawnX());
                                gh.setY(gh.getSpawnY());
                            }
                            pacman.setHp(pacman.getHp() - 1);
                            if (pacman.getHp() == 0) {
                                client.sendMessage(GameMessageProvider.createMessage(MessageType.GAME_LOOSE_REQUEST, new byte[0]));
                            }
                            break;
                    }
                    break;
                case MessageType.BLINK_BONUSES_RESPONSE:
                    blinkBonuses();
                    break;
                case MessageType.PACMAN_EAT_PELLET_RESPONSE:
                    Pellet pellet = pacman.eatPellet(pellets);
                    if (pellet != null) {
                        scores += pellet.getScore();
                        Platform.runLater(() -> gameWindow.getChildren().remove(pellet.getView()));
                    }
                    break;
                case MessageType.PACMAN_EAT_BONUS_RESPONSE:
                    Bonus bonus = pacman.eatBonus(bonuses);
                    if (bonus != null) {
                        client.sendMessage(GameMessageProvider.createMessage(MessageType.RUSH_MODE_REQUEST, new byte[] {1}));
                        scores += bonus.getScore();
                        Platform.runLater(() -> gameWindow.getChildren().remove(bonus.getView()));
                    }
                    break;
                case MessageType.CHANGE_SCORES_RESPONSE:
                    setUpGameInfo();
                    break;
                case MessageType.GAME_WIN_RESPONSE:
                    if (pellets.isEmpty() && bonuses.isEmpty()) endGameWin();
                    break;
                case MessageType.GAME_LOOSE_RESPONSE:
                    Platform.runLater(this::endGameLoose);
                    break;
                case MessageType.PLAYERS_MOVE_RESPONSE:
                    pacman.go();
                    for (int i = 0; i < ghosts.size(); i++) {
                        Ghost ghos = ghosts.get(i);
                        ghos.go();
                        if (pacman.getView().getBoundsInParent().intersects(ghos.getView().getBoundsInParent())) {
                            buffer = ByteBuffer.allocate(8 * 2 + 4 * 2);
                            if (userId != 0) {
                                if (isRushMode) {
                                    buffer.putInt(0);
                                    buffer.putInt(i);
                                } else {
                                    buffer.putInt(i);
                                    buffer.putInt(0);
                                }
                                buffer.putDouble(pacman.getX());
                                buffer.putDouble(pacman.getY());
                                client.sendMessage(GameMessageProvider.createMessage(MessageType.EAT_PLAYER_REQUEST, buffer.array()));
                            }
                        }
                    }
//                    for (Ghost ghos : ghosts) {
//                        ghos.go();
//                        if (pacman.getX() == ghos.getX() && pacman.getY() == ghos.getY()) {
//                            buffer = ByteBuffer.allocate(8 * 2 + 4 * 2);
//                            buffer.putInt(1);
//                            buffer.putInt(0);
//                            buffer.putDouble(pacman.getX());
//                            buffer.putDouble(pacman.getY());
//                            client.sendMessage(GameMessageProvider.createMessage(MessageType.EAT_PLAYER_REQUEST, buffer.array()));
//                        }
//                    }
                    break;
                case MessageType.RUSH_MODE_RESPONSE:
                    byte[] data = message.getData();
                    isRushMode = data[0] == 1;
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
