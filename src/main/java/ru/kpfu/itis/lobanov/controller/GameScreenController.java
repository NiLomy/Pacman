package ru.kpfu.itis.lobanov.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.lobanov.model.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.environment.Cell;
import ru.kpfu.itis.lobanov.model.environment.Maze;
import ru.kpfu.itis.lobanov.model.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.player.Pacman;
import ru.kpfu.itis.lobanov.utils.Direction;
import ru.kpfu.itis.lobanov.utils.GameSettings;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class GameScreenController {
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
    private final Maze maze = new Maze();
    private int scores;
    private Pacman pacman;
    private Direction currentDirection;
    private List<Pellet> pellets;
    private List<Bonus> bonuses;

    @FXML
    private void initialize() {
        gameWindow.setStyle("-fx-background-color: lightgrey");
        drawWalls();
        setUpGameInfo();
        createPacman();
        generateBonuses();
        generatePellets();

        KeyFrame keyFrame = createKeyFrame();
        startGame(keyFrame);
    }


    private void drawWalls() {
//        Cell[][] cells = maze.getData();
//        for (int i = 0; i < cells.length; i++) {
//            HBox line = new HBox();
//            line.setAlignment(Pos.CENTER);
//            for (int j = 0; j < cells.length; j++) {
//                if (cells[i][j].isWall()) {
//                    Rectangle rectangle = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//                    line.getChildren().addAll(rectangle);
//                    walls.add(rectangle);
//                } else {
//                    Rectangle rectangle = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//                    rectangle.setFill(Color.WHITE);
//                    line.getChildren().addAll(rectangle);
//                }
//            }
//            gameField.getChildren().addAll(line);
//        }

        Cell[][] cells = maze.getData();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].isWall()) {
                    Rectangle rectangle = new Rectangle(i * GameSettings.CELL_SIZE, j * GameSettings.CELL_SIZE, GameSettings.CELL_SIZE, GameSettings.CELL_SIZE);
                    gameWindow.getChildren().addAll(rectangle);
                }
            }
        }
    }

    private void setUpGameInfo() {
        scoreInfo.setText("Scores: " + scores);
    }

    private void createPacman() {
        pacman = new Pacman(maze);
        gameWindow.getChildren().addAll(pacman.getView());
    }

    private void generateBonuses() {
        bonuses = maze.generateBonuses(pacman.getX(), pacman.getY());
        gameWindow.getChildren().addAll(bonuses.stream().map(Bonus::getView).collect(Collectors.toList()));
    }

    private void generatePellets() {
        pellets = maze.generatePellets(pacman.getX(), pacman.getY(), bonuses);
        gameWindow.getChildren().addAll(pellets.stream().map(Pellet::getView).collect(Collectors.toList()));
    }

    private KeyFrame createKeyFrame() {
        return new KeyFrame(GameSettings.UPDATE_FREQUENCY, event -> {
            Scene s = StartScreenController.getScene();
            if (s != null) {
                s.setOnKeyPressed(event1 -> {
                    switch (event1.getCode()) {
                        case UP:
                            currentDirection = Direction.UP;
                            break;
                        case DOWN:
                            currentDirection = Direction.DOWN;
                            break;
                        case LEFT:
                            currentDirection = Direction.LEFT;
                            break;
                        case RIGHT:
                            currentDirection = Direction.RIGHT;
                            break;
                        case ENTER:
                            System.out.println("YAAA");
                            break;
                    }
                });
            }

            blinkBonuses();
            pacman.go(currentDirection);
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
                Runtime.getRuntime().exit(0);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 500);
    }
}