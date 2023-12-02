package ru.kpfu.itis.lobanov.model.player;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.lobanov.model.environment.Cell;
import ru.kpfu.itis.lobanov.model.environment.Maze;
import ru.kpfu.itis.lobanov.utils.Direction;
import ru.kpfu.itis.lobanov.utils.GameSettings;
import ru.kpfu.itis.lobanov.utils.Placement;

import java.util.List;
import java.util.Random;

import static ru.kpfu.itis.lobanov.utils.WallDetector.checkForWall;

public class Ghost extends AbstractPlayer {
    public Ghost(Maze maze) {
        super(maze);

        setSpawnPoint();
        setHp(GameSettings.GHOST_HP);
        view.setImage(new Image("/images/pacman-left/3.png"));
        view.setFitWidth(GameSettings.CELL_SIZE);
        view.setFitHeight(GameSettings.CELL_SIZE);
        view.setX(spawnX);
        view.setY(spawnY);
    }

    private void setSpawnPoint() {
//        Cell[] cellsForPlacement = maze.getCellsForPlacement(Placement.CENTER);
//        Random random = new Random();
//
//        while (true) {
//            int index = random.nextInt(cellsForPlacement.length);
//            Cell cell = cellsForPlacement[index];
//            if (!cell.isWall()) {
////                x = spawnX = cell.getX() * GameSettings.CELL_SIZE + GameSettings.CELL_SIZE / 2;
////                y = spawnY = cell.getY() * GameSettings.CELL_SIZE + GameSettings.CELL_SIZE / 2;
//                x = spawnX = cell.getX() * GameSettings.CELL_SIZE;
//                y = spawnY = cell.getY() * GameSettings.CELL_SIZE;
//                break;
//            }
//        }
        x = spawnX = 1;
        y = spawnY = 1;
    }

    @Override
    public void go() {
        Direction movingDirection;

        if (!checkForWall(currentDirection, x, y, walls)) {
            movingDirection = currentDirection;
        } else {
            movingDirection = null;
        }

        if (movingDirection != null) {
            switch (movingDirection) {
                case UP:
                    if (!checkForWall(movingDirection, x, y, walls)) {
                        setY(y - GameSettings.PACMAN_SPEED);
                    }
                    if (y + GameSettings.CELL_SIZE / 2 <= 0) {
                        setY(maze.getLowerExit().getY() * GameSettings.CELL_SIZE);
                        setX(maze.getLowerExit().getX() * GameSettings.CELL_SIZE);
                    }
                    break;
                case DOWN:
                    if (!checkForWall(movingDirection, x, y, walls)) {
                        setY(y + GameSettings.PACMAN_SPEED);
                    }
                    if (y >= maze.labyrinthLength() * GameSettings.CELL_SIZE - GameSettings.CELL_SIZE / 2) {
                        setY(GameSettings.CELL_SIZE / 2);
                        setX(maze.getUpperExit().getX() * GameSettings.CELL_SIZE);
                    }
                    break;
                case LEFT:
                    if (!checkForWall(movingDirection, x, y, walls)) {
                        setX(x - GameSettings.PACMAN_SPEED);
                    }
                    if (x + GameSettings.CELL_SIZE / 2 <= 0) {
                        setX(maze.getRightExit().getX() * GameSettings.CELL_SIZE);
                        setY(maze.getRightExit().getY() * GameSettings.CELL_SIZE);
                    }
                    break;
                case RIGHT:
                    if (!checkForWall(movingDirection, x, y, walls)) {
                        setX(x + GameSettings.PACMAN_SPEED);
                    }
                    if (x >= maze.labyrinthLength() * GameSettings.CELL_SIZE - GameSettings.CELL_SIZE / 2) {
                        setX(GameSettings.CELL_SIZE / 2);
                        setY(maze.getLeftExit().getY() * GameSettings.CELL_SIZE);
                    }
            }
        }
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
