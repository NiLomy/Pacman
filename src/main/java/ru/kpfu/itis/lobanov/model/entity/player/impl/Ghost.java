package ru.kpfu.itis.lobanov.model.entity.player.impl;

import javafx.scene.image.Image;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.player.AbstractPlayer;
import ru.kpfu.itis.lobanov.utils.constants.Direction;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import static ru.kpfu.itis.lobanov.utils.WallDetector.checkForWall;

public class Ghost extends AbstractPlayer {
    private String ghostPackageSprite;
    private boolean isFrightened;
    private Image clamGhostImage;
    private Image upGhostImage;
    private Image dowmGhostImage;
    private Image leftGhostImage;
    private Image rightGhostImage;


    public Ghost(Maze maze, String ghostPackageSprite) {
        super(maze);
        this.ghostPackageSprite = ghostPackageSprite;

        setSpawnPoint(GameSettings.CELL_SIZE + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS, GameSettings.CELL_SIZE + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
        setHp(GameSettings.GHOST_HP);
    }

    public void setSpawnPoint(double x, double y) {
        this.x = spawnX = x;
        this.y = spawnY = y;
    }

    @Override
    public void go() {
        Direction movingDirection;

        if (!checkForWall(currentDirection, walls, view, offsetX, offsetY)) {
            movingDirection = currentDirection;
        } else {
            movingDirection = null;
            if (currentDirection != null) {
                if (isFrightened) {
                    view.setImage(new Image(GameResources.FRIGHTENED_GHOST_IMAGE));
                } else {
                    switch (currentDirection) {
                        case UP:
                            view.setImage(upGhostImage);
                            break;
                        case DOWN:
                            view.setImage(dowmGhostImage);
                            break;
                        case LEFT:
                            view.setImage(leftGhostImage);
                            break;
                        case RIGHT:
                            view.setImage(rightGhostImage);
                            break;
                    }
                }
            }
        }

        if (movingDirection != null) {
            switch (movingDirection) {
                case UP:
                    if (isFrightened) {
                        view.setImage(clamGhostImage);
                    } else {
                        view.setImage(upGhostImage);
                    }
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setY(y - GameSettings.GHOST_SPEED);
                    }
                    if (y + GameSettings.CELL_SIZE / 2 <= offsetY + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS * 2) {
                        setY(maze.getLowerExit().getY() * GameSettings.CELL_SIZE + offsetY + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
                        setX(maze.getLowerExit().getX() * GameSettings.CELL_SIZE + offsetX + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
                    }
                    break;
                case DOWN:
                    if (isFrightened) {
                        view.setImage(clamGhostImage);
                    } else {
                        view.setImage(dowmGhostImage);
                    }
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setY(y + GameSettings.GHOST_SPEED);
                    }
                    if (y >= maze.labyrinthLength() * GameSettings.CELL_SIZE + offsetY - GameSettings.CELL_SIZE / 2 - GameSettings.PLAYER_SET_UP_COORDINATE_BIAS) {
                        setY(GameSettings.CELL_SIZE / 2 + offsetY + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
                        setX(maze.getUpperExit().getX() * GameSettings.CELL_SIZE + offsetX + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
                    }
                    break;
                case LEFT:
                    if (isFrightened) {
                        view.setImage(clamGhostImage);
                    } else {
                        view.setImage(leftGhostImage);
                    }
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setX(x - GameSettings.GHOST_SPEED);
                    }
                    if (x + GameSettings.CELL_SIZE / 2 <= offsetX + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS * 2) {
                        setX(maze.getRightExit().getX() * GameSettings.CELL_SIZE + offsetX + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
                        setY(maze.getRightExit().getY() * GameSettings.CELL_SIZE + offsetY + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
                    }
                    break;
                case RIGHT:
                    if (isFrightened) {
                        view.setImage(clamGhostImage);
                    } else {
                        view.setImage(rightGhostImage);
                    }
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setX(x + GameSettings.GHOST_SPEED);
                    }
                    if (x >= maze.labyrinthLength() * GameSettings.CELL_SIZE + offsetX - GameSettings.CELL_SIZE / 2 - GameSettings.PLAYER_SET_UP_COORDINATE_BIAS) {
                        setX(GameSettings.CELL_SIZE / 2 + offsetX + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
                        setY(maze.getLeftExit().getY() * GameSettings.CELL_SIZE + offsetY + GameSettings.PLAYER_SET_UP_COORDINATE_BIAS);
                    }
            }
        }
    }

    public void show() {
        this.clamGhostImage = new Image(GameResources.FRIGHTENED_GHOST_IMAGE);
        this.upGhostImage = new Image(GameResources.GHOST_PACKAGE_PREFIX + ghostPackageSprite + GameResources.UP_IMAGE);
        this.dowmGhostImage = new Image(GameResources.GHOST_PACKAGE_PREFIX + ghostPackageSprite + GameResources.DOWN_IMAGE);
        this.leftGhostImage = new Image(GameResources.GHOST_PACKAGE_PREFIX + ghostPackageSprite + GameResources.LEFT_IMAGE);
        this.rightGhostImage = new Image(GameResources.GHOST_PACKAGE_PREFIX + ghostPackageSprite + GameResources.RIGHT_IMAGE);

        view.setImage(rightGhostImage);
        view.setFitWidth(GameSettings.CELL_SIZE - GameSettings.PLAYER_SET_UP_VIEW_SIZE_BIAS);
        view.setFitHeight(GameSettings.CELL_SIZE - GameSettings.PLAYER_SET_UP_VIEW_SIZE_BIAS);
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

    public String getGhostPackageSprite() {
        return ghostPackageSprite;
    }

    public void setGhostPackageSprite(String ghostPackageSprite) {
        this.ghostPackageSprite = ghostPackageSprite;
    }

    public boolean isFrightened() {
        return isFrightened;
    }

    public void setFrightened(boolean frightened) {
        isFrightened = frightened;
    }
}
