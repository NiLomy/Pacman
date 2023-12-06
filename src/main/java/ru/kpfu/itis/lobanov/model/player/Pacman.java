package ru.kpfu.itis.lobanov.model.player;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang.SerializationUtils;
import ru.kpfu.itis.lobanov.model.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.environment.Cell;
import ru.kpfu.itis.lobanov.model.environment.Maze;
import ru.kpfu.itis.lobanov.model.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.utils.Direction;
import ru.kpfu.itis.lobanov.utils.GameSettings;
import ru.kpfu.itis.lobanov.utils.Placement;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import static ru.kpfu.itis.lobanov.utils.WallDetector.checkForWall;

public class Pacman extends AbstractPlayer {
    private int stayCount = 0;

    public Pacman(Maze maze) {
        super(maze);

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
//                x = spawnX = cell.getX() * GameSettings.CELL_SIZE + GameSettings.CELL_SIZE / 2;
//                y = spawnY = cell.getY() * GameSettings.CELL_SIZE + GameSettings.CELL_SIZE / 2;
                x = spawnX = cell.getX() * GameSettings.CELL_SIZE + 3;
                y = spawnY = cell.getY() * GameSettings.CELL_SIZE + 3;
                break;
            }
        }
    }

    @Override
    public void go() {
        Direction movingDirection;
        if (!checkForWall(currentDirection, walls, view)) {
            movingDirection = currentDirection;
        } else {
            movingDirection = null;
            stayCount++;
            if (currentDirection != null) switch (currentDirection) {
                case UP:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(new Image("/images/pacman-up/2.png"));
                    } else {
                        view.setImage(new Image("/images/pacman-up/3.png"));
                    }
                    break;
                case DOWN:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(new Image("/images/pacman-down/2.png"));
                    } else {
                        view.setImage(new Image("/images/pacman-down/3.png"));
                    }
                    break;
                case LEFT:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(new Image("/images/pacman-left/2.png"));
                    } else {
                        view.setImage(new Image("/images/pacman-left/3.png"));
                    }
                    break;
                case RIGHT:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(new Image("/images/pacman-right/2.png"));
                    } else {
                        view.setImage(new Image("/images/pacman-right/3.png"));
                    }
                    break;
            }
        }

        if (movingDirection != null) {
            switch (movingDirection) {
                case UP:
                    if (!checkForWall(movingDirection, walls, view)) {
                        setY(y - GameSettings.PACMAN_SPEED);
                    }
                    if (y + GameSettings.CELL_SIZE / 2 <= 0) {
                        setY(maze.getLowerExit().getY() * GameSettings.CELL_SIZE);
                        setX(maze.getLowerExit().getX() * GameSettings.CELL_SIZE);
                    }
                    if (stayCount == 0) {
                        view.setImage(new Image("/images/pacman-up/1.png"));
                    } else {
                        view.setImage(new Image("/images/pacman-up/2.png"));
                    }
                    break;
                case DOWN:
                    if (!checkForWall(movingDirection, walls, view)) {
                        setY(y + GameSettings.PACMAN_SPEED);
                    }
                    if (y >= maze.labyrinthLength() * GameSettings.CELL_SIZE - GameSettings.CELL_SIZE / 2) {
                        setY(GameSettings.CELL_SIZE / 2);
                        setX(maze.getUpperExit().getX() * GameSettings.CELL_SIZE);
                    }
                    if (stayCount == 0) {
                        view.setImage(new Image("/images/pacman-down/1.png"));
                    } else {
                        view.setImage(new Image("/images/pacman-down/2.png"));
                    }
                    break;
                case LEFT:
                    if (!checkForWall(movingDirection, walls, view)) {
                        setX(x - GameSettings.PACMAN_SPEED);
                    }
                    if (x + GameSettings.CELL_SIZE / 2 <= 0) {
                        setX(maze.getRightExit().getX() * GameSettings.CELL_SIZE);
                        setY(maze.getRightExit().getY() * GameSettings.CELL_SIZE);
                    }
                    if (stayCount == 0) {
                        view.setImage(new Image("/images/pacman-left/1.png"));
                    } else {
                        view.setImage(new Image("/images/pacman-left/2.png"));
                    }
                    break;
                case RIGHT:
                    if (!checkForWall(movingDirection, walls, view)) {
                        setX(x + GameSettings.PACMAN_SPEED);
                    }
                    if (x >= maze.labyrinthLength() * GameSettings.CELL_SIZE - GameSettings.CELL_SIZE / 2) {
                        setX(GameSettings.CELL_SIZE / 2);
                        setY(maze.getLeftExit().getY() * GameSettings.CELL_SIZE);
                    }
                    if (stayCount == 0) {
                        view.setImage(new Image("/images/pacman-right/1.png"));
                    } else {
                        view.setImage(new Image("/images/pacman-right/2.png"));
                    }
                    break;
            }
            stayCount = 0;
        }
    }

    public void show() {
        view.setImage(new Image("/images/pacman-left/3.png"));
        view.setFitWidth(GameSettings.CELL_SIZE - 4);
        view.setFitHeight(GameSettings.CELL_SIZE - 4);
        view.setX(spawnX);
        view.setY(spawnY);
    }

    public Pellet eatPellet(List<Pellet> pellets) {
        for (int i = 0; i < pellets.size(); i++) {
            Pellet pellet = pellets.get(i);
            if (Math.abs(pellet.getX() - x) <= GameSettings.CELL_SIZE / 4 && Math.abs(pellet.getY() - y) <= GameSettings.CELL_SIZE / 4) {
                return pellets.remove(i);
            }
        }
        return null;
    }

    public Bonus eatBonus(List<Bonus> bonuses) {
        for (int i = 0; i < bonuses.size(); i++) {
            Bonus bonus = bonuses.get(i);
            if (Math.abs(bonus.getX() - x) <= GameSettings.CELL_SIZE / 2 && Math.abs(bonus.getY() - y) <= GameSettings.CELL_SIZE / 2) {
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
