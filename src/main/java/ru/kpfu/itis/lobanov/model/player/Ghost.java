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
    }

    public void setSpawnPoint() {
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
        x = spawnX = GameSettings.CELL_SIZE + 3;
        y = spawnY = GameSettings.CELL_SIZE + 3;
    }

    @Override
    public void go() {
        Direction movingDirection;

        if (!checkForWall(currentDirection, walls, view)) {
            movingDirection = currentDirection;
        } else {
            movingDirection = null;
        }

        if (movingDirection != null) {
            switch (movingDirection) {
                case UP:
                    if (!checkForWall(movingDirection, walls, view)) {
                        setY(y - GameSettings.GHOST_SPEED);
                    }
                    if (y + GameSettings.CELL_SIZE / 2 <= 0) {
                        setY(maze.getLowerExit().getY() * GameSettings.CELL_SIZE);
                        setX(maze.getLowerExit().getX() * GameSettings.CELL_SIZE);
                    }
                    break;
                case DOWN:
                    if (!checkForWall(movingDirection, walls, view)) {
                        setY(y + GameSettings.GHOST_SPEED);
                    }
                    if (y >= maze.labyrinthLength() * GameSettings.CELL_SIZE - GameSettings.CELL_SIZE / 2) {
                        setY(GameSettings.CELL_SIZE / 2);
                        setX(maze.getUpperExit().getX() * GameSettings.CELL_SIZE);
                    }
                    break;
                case LEFT:
                    if (!checkForWall(movingDirection, walls, view)) {
                        setX(x - GameSettings.GHOST_SPEED);
                    }
                    if (x + GameSettings.CELL_SIZE / 2 <= 0) {
                        setX(maze.getRightExit().getX() * GameSettings.CELL_SIZE);
                        setY(maze.getRightExit().getY() * GameSettings.CELL_SIZE);
                    }
                    break;
                case RIGHT:
                    if (!checkForWall(movingDirection, walls, view)) {
                        setX(x + GameSettings.GHOST_SPEED);
                    }
                    if (x >= maze.labyrinthLength() * GameSettings.CELL_SIZE - GameSettings.CELL_SIZE / 2) {
                        setX(GameSettings.CELL_SIZE / 2);
                        setY(maze.getLeftExit().getY() * GameSettings.CELL_SIZE);
                    }
            }
        }
    }

    public void show() {
        view.setImage(new Image("/images/blinky.png"));
        view.setFitWidth(GameSettings.CELL_SIZE - 4);
        view.setFitHeight(GameSettings.CELL_SIZE - 4);
        view.setX(spawnX);
        view.setY(spawnY);
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
