package ru.kpfu.itis.lobanov.model.entity.player.impl;

import javafx.scene.image.Image;
import ru.kpfu.itis.lobanov.model.entity.environment.Cell;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.entity.player.AbstractPlayer;
import ru.kpfu.itis.lobanov.utils.constants.Direction;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.Placement;

import java.util.List;
import java.util.Random;

import static ru.kpfu.itis.lobanov.utils.WallDetector.checkForWall;

public class Pacman extends AbstractPlayer {
    private int stayCount;
    private Direction lastDirection;
    private int moveCount;
    private final Image calmPacmanImage;
    private final Image upOpenPacmanImage;
    private final Image downOpenPacmanImage;
    private final Image leftOpenPacmanImage;
    private final Image rightOpenPacmanImage;
    private final Image upWideOpenPacmanImage;
    private final Image downWideOpenPacmanImage;
    private final Image leftWideOpenPacmanImage;
    private final Image rightWideOpenPacmanImage;

    public Pacman(Maze maze) {
        super(maze);
        this.stayCount = 0;
        this.calmPacmanImage = new Image(GameResources.PACMAN_CALM_IMAGE);
        this.upOpenPacmanImage = new Image(GameResources.UP_OPEN_PACMAN_IMAGE);
        this.downOpenPacmanImage = new Image(GameResources.DOWN_OPEN_PACMAN_IMAGE);
        this.leftOpenPacmanImage = new Image(GameResources.LEFT_OPEN_PACMAN_IMAGE);
        this.rightOpenPacmanImage = new Image(GameResources.RIGHT_OPEN_PACMAN_IMAGE);
        this.upWideOpenPacmanImage = new Image(GameResources.UP_WIDE_OPEN_PACMAN_IMAGE);
        this.downWideOpenPacmanImage = new Image(GameResources.DOWN_WIDE_OPEN_PACMAN_IMAGE);
        this.leftWideOpenPacmanImage = new Image(GameResources.LEFT_WIDE_OPEN_PACMAN_IMAGE);
        this.rightWideOpenPacmanImage = new Image(GameResources.RIGHT_WIDE_OPEN_PACMAN_IMAGE);
        setSpawnPoint();
        setHp(GameSettings.PACMAN_HP);
    }

    private void setSpawnPoint() {
        Cell[] cellsForPlacement = maze.getCellsForPlacement(Placement.CENTER);
        Random random = new Random();

        while (true) {
            int index = random.nextInt(cellsForPlacement.length);
            Cell cell = cellsForPlacement[index];
            if (!cell.isWall()) {
                x = spawnX = cell.getX() * GameSettings.CELL_SIZE + 3;
                y = spawnY = cell.getY() * GameSettings.CELL_SIZE + 3;
                break;
            }
        }
    }

    @Override
    public void go() {
        Direction movingDirection;
        if (!checkForWall(currentDirection, walls, view, offsetX, offsetY)) {
            movingDirection = currentDirection;
        } else {
            movingDirection = null;
            stayCount++;
            if (currentDirection != null) switch (currentDirection) {
                case UP:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(upOpenPacmanImage);
                    } else {
                        view.setImage(calmPacmanImage);
                    }
                    break;
                case DOWN:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(downOpenPacmanImage);
                    } else {
                        view.setImage(calmPacmanImage);
                    }
                    break;
                case LEFT:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(leftOpenPacmanImage);
                    } else {
                        view.setImage(calmPacmanImage);
                    }
                    break;
                case RIGHT:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(rightOpenPacmanImage);
                    } else {
                        view.setImage(calmPacmanImage);
                    }
                    break;
            }
        }

        if (movingDirection != null) {
            switch (movingDirection) {
                case UP:
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setY(y - GameSettings.PACMAN_SPEED);
                    }
                    if (y + GameSettings.CELL_SIZE / 2 <= offsetY + 3 * 2) {
                        setY(maze.getLowerExit().getY() * GameSettings.CELL_SIZE + offsetY + 3);
                        setX(maze.getLowerExit().getX() * GameSettings.CELL_SIZE + offsetX + 3);
                    }
                    if (stayCount == 0) {
                        if (lastDirection == Direction.UP) {
                            moveCount++;
                        } else {
                            moveCount = 0;
                        }
                        if (moveCount < GameSettings.MOVING_COUNT) {
                            view.setImage(upWideOpenPacmanImage);
                        } else {
                            view.setImage(upOpenPacmanImage);
                            if (moveCount == GameSettings.MOVING_COUNT * 2) moveCount = 0;
                        }
                        lastDirection = Direction.UP;
                    } else {
                        view.setImage(calmPacmanImage);
                    }
                    break;
                case DOWN:
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setY(y + GameSettings.PACMAN_SPEED);
                    }
                    if (y >= maze.labyrinthLength() * GameSettings.CELL_SIZE + offsetY - GameSettings.CELL_SIZE / 2 - 3) {
                        setY(GameSettings.CELL_SIZE / 2 + offsetY + 3);
                        setX(maze.getUpperExit().getX() * GameSettings.CELL_SIZE + offsetX + 3);
                    }
                    if (stayCount == 0) {
                        if (lastDirection == Direction.DOWN) {
                            moveCount++;
                        } else {
                            moveCount = 0;
                        }
                        if (moveCount < GameSettings.MOVING_COUNT) {
                            view.setImage(downWideOpenPacmanImage);
                        } else {
                            view.setImage(downOpenPacmanImage);
                            if (moveCount == GameSettings.MOVING_COUNT * 2) moveCount = 0;
                        }
                        lastDirection = Direction.DOWN;
                    } else {
                        view.setImage(calmPacmanImage);
                    }
                    break;
                case LEFT:
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setX(x - GameSettings.PACMAN_SPEED);
                    }
                    if (x + GameSettings.CELL_SIZE / 2 <= offsetX + 3 * 2) {
                        setX(maze.getRightExit().getX() * GameSettings.CELL_SIZE + offsetX + 3);
                        setY(maze.getRightExit().getY() * GameSettings.CELL_SIZE + offsetY + 3);
                    }
                    if (stayCount == 0) {
                        if (lastDirection == Direction.LEFT) {
                            moveCount++;
                        } else {
                            moveCount = 0;
                        }
                        if (moveCount < GameSettings.MOVING_COUNT) {
                            view.setImage(leftWideOpenPacmanImage);
                        } else {
                            view.setImage(leftOpenPacmanImage);
                            if (moveCount == GameSettings.MOVING_COUNT * 2) moveCount = 0;
                        }
                        lastDirection = Direction.LEFT;
                    } else {
                        view.setImage(calmPacmanImage);
                    }
                    break;
                case RIGHT:
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setX(x + GameSettings.PACMAN_SPEED);
                    }
                    if (x >= maze.labyrinthLength() * GameSettings.CELL_SIZE + offsetX - GameSettings.CELL_SIZE / 2 - 3) {
                        setX(GameSettings.CELL_SIZE / 2 + offsetX + 3);
                        setY(maze.getLeftExit().getY() * GameSettings.CELL_SIZE + offsetY + 3);
                    }
                    if (stayCount == 0) {
                        if (lastDirection == Direction.RIGHT) {
                            moveCount++;
                        } else {
                            moveCount = 0;
                        }
                        if (moveCount < GameSettings.MOVING_COUNT) {
                            view.setImage(rightWideOpenPacmanImage);
                        } else {
                            view.setImage(rightOpenPacmanImage);
                            if (moveCount == GameSettings.MOVING_COUNT * 2) moveCount = 0;
                        }
                        lastDirection = Direction.RIGHT;
                    } else {
                        view.setImage(calmPacmanImage);
                    }
                    break;
            }
            stayCount = 0;
        }
    }

    public void show() {
        view.setImage(calmPacmanImage);
        view.setFitWidth(GameSettings.CELL_SIZE - 4);
        view.setFitHeight(GameSettings.CELL_SIZE - 4);
        view.setX(spawnX);
        view.setY(spawnY);
    }

    public Pellet eatPellet(List<Pellet> pellets) {
        for (int i = 0; i < pellets.size(); i++) {
            Pellet pellet = pellets.get(i);

            if (view.getBoundsInParent().intersects(pellet.getView().getBoundsInParent())) {
                return pellets.remove(i);
            }
        }
        return null;
    }

    public Bonus eatBonus(List<Bonus> bonuses) {
        for (int i = 0; i < bonuses.size(); i++) {
            Bonus bonus = bonuses.get(i);
            if (view.getBoundsInParent().intersects(bonus.getView().getBoundsInParent())) {
                return bonuses.remove(i);
            }
        }
        return null;
    }

    @Override
    public void setX(double x) {
        this.x = x;
        this.view.setX(x);
    }

    @Override
    public void setY(double y) {
        this.y = y;
        this.view.setY(y);
    }
}
