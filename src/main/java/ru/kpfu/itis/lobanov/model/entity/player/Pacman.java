package ru.kpfu.itis.lobanov.model.entity.player;

import javafx.scene.image.Image;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.entity.environment.Cell;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.utils.constants.Direction;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.Placement;

import java.util.List;
import java.util.Random;

import static ru.kpfu.itis.lobanov.utils.WallDetector.checkForWall;

public class Pacman extends AbstractPlayer {
    private int stayCount;
    private Direction lastDirection;
    private int moveCount;

    public Pacman(Maze maze) {
        super(maze);
        this.stayCount = 0;
        setSpawnPoint();
        setHp(GameSettings.PACMAN_HP);
//        animation = new SpriteAnimation(new ImageView(new Image("/images/spritesheet.png")), Duration.seconds(2), 11, 11,351, 2, 38, 38);
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
        if (!checkForWall(currentDirection, walls, view, offsetX, offsetY)) {
            movingDirection = currentDirection;
        } else {
            movingDirection = null;
            stayCount++;
            if (currentDirection != null) switch (currentDirection) {
                case UP:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(new Image("/images/pacman/up/2.png"));
                    } else {
                        view.setImage(new Image("/images/pacman/calm/pacman.png"));
                    }
                    break;
                case DOWN:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(new Image("/images/pacman/down/2.png"));
                    } else {
                        view.setImage(new Image("/images/pacman/calm/pacman.png"));
                    }
                    break;
                case LEFT:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(new Image("/images/pacman/left/2.png"));
                    } else {
                        view.setImage(new Image("/images/pacman/calm/pacman.png"));
                    }
                    break;
                case RIGHT:
                    if (stayCount < GameSettings.CALM_COUNT) {
                        view.setImage(new Image("/images/pacman/right/2.png"));
                    } else {
                        view.setImage(new Image("/images/pacman/calm/pacman.png"));
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
                            view.setImage(new Image("/images/pacman/up/1.png"));
                        } else {
                            view.setImage(new Image("/images/pacman/up/2.png"));
                            if (moveCount == GameSettings.MOVING_COUNT * 2) moveCount = 0;
                        }
                        lastDirection = Direction.UP;
                    } else {
                        view.setImage(new Image("/images/pacman/calm/pacman.png"));
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
                            view.setImage(new Image("/images/pacman/down/1.png"));
                        } else {
                            view.setImage(new Image("/images/pacman/down/2.png"));
                            if (moveCount == GameSettings.MOVING_COUNT * 2) moveCount = 0;
                        }
                        lastDirection = Direction.DOWN;
                    } else {
                        view.setImage(new Image("/images/pacman/calm/pacman.png"));
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
                            view.setImage(new Image("/images/pacman/left/1.png"));
                        } else {
                            view.setImage(new Image("/images/pacman/left/2.png"));
                            if (moveCount == GameSettings.MOVING_COUNT * 2) moveCount = 0;
                        }
                        lastDirection = Direction.LEFT;
                    } else {
                        view.setImage(new Image("/images/pacman/calm/pacman.png"));
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
                            view.setImage(new Image("/images/pacman/right/1.png"));
                        } else {
                            view.setImage(new Image("/images/pacman/right/2.png"));
                            if (moveCount == GameSettings.MOVING_COUNT * 2) moveCount = 0;
                        }
                        lastDirection = Direction.RIGHT;
                    } else {
                        view.setImage(new Image("/images/pacman/calm/pacman.png"));
                    }
                    break;
            }
            stayCount = 0;
        }
    }

    public void show() {
        view.setImage(new Image("/images/pacman/calm/pacman.png"));
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
