package ru.kpfu.itis.lobanov.model.entity.player;

import javafx.scene.image.Image;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.utils.constants.Direction;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import static ru.kpfu.itis.lobanov.utils.WallDetector.checkForWall;

public class Ghost extends AbstractPlayer {
    private String ghostPackageSprite;
    private boolean isFrightened;

    public Ghost(Maze maze) {
        super(maze);

        setSpawnPoint(GameSettings.CELL_SIZE + 3, GameSettings.CELL_SIZE + 3);
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
                    view.setImage(new Image("/images/ghosts/frightened/1.png"));
                } else {
                    switch (currentDirection) {
                        case UP:
                            view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/up.png"));
                            break;
                        case DOWN:
                            view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/down.png"));
                            break;
                        case LEFT:
                            view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/left.png"));
                            break;
                        case RIGHT:
                            view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/right.png"));
                            break;
                    }
                }
            }
        }

        if (movingDirection != null) {
            switch (movingDirection) {
                case UP:
                    if (isFrightened) {
                        view.setImage(new Image("/images/ghosts/frightened/1.png"));
                    } else {
                        view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/up.png"));
                    }
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setY(y - GameSettings.GHOST_SPEED);
                    }
                    if (y + GameSettings.CELL_SIZE / 2 <= offsetY + 3 * 2) {
                        setY(maze.getLowerExit().getY() * GameSettings.CELL_SIZE + offsetY + 3);
                        setX(maze.getLowerExit().getX() * GameSettings.CELL_SIZE + offsetX + 3);
                    }
                    break;
                case DOWN:
                    if (isFrightened) {
                        view.setImage(new Image("/images/ghosts/frightened/1.png"));
                    } else {
                        view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/down.png"));
                    }
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setY(y + GameSettings.GHOST_SPEED);
                    }
                    if (y >= maze.labyrinthLength() * GameSettings.CELL_SIZE + offsetY - GameSettings.CELL_SIZE / 2 - 3) {
                        setY(GameSettings.CELL_SIZE / 2 + offsetY + 3);
                        setX(maze.getUpperExit().getX() * GameSettings.CELL_SIZE + offsetX + 3);
                    }
                    break;
                case LEFT:
                    if (isFrightened) {
                        view.setImage(new Image("/images/ghosts/frightened/1.png"));
                    } else {
                        view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/left.png"));
                    }
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setX(x - GameSettings.GHOST_SPEED);
                    }
                    if (x + GameSettings.CELL_SIZE / 2 <= offsetX + 3 * 2) {
                        setX(maze.getRightExit().getX() * GameSettings.CELL_SIZE + offsetX + 3);
                        setY(maze.getRightExit().getY() * GameSettings.CELL_SIZE + offsetY + 3);
                    }
                    break;
                case RIGHT:
                    if (isFrightened) {
                        view.setImage(new Image("/images/ghosts/frightened/1.png"));
                    } else {
                        view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/right.png"));
                    }
                    if (!checkForWall(movingDirection, walls, view, offsetX, offsetY)) {
                        setX(x + GameSettings.GHOST_SPEED);
                    }
                    if (x >= maze.labyrinthLength() * GameSettings.CELL_SIZE + offsetX - GameSettings.CELL_SIZE / 2 - 3) {
                        setX(GameSettings.CELL_SIZE / 2 + offsetX + 3);
                        setY(maze.getLeftExit().getY() * GameSettings.CELL_SIZE + offsetY + 3);
                    }
            }
        }
    }

    public void show() {
        view.setImage(new Image("/images/ghosts" + ghostPackageSprite + "/right.png"));
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
