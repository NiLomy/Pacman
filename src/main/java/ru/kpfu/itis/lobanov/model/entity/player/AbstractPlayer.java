package ru.kpfu.itis.lobanov.model.entity.player;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.utils.constants.Direction;

import java.util.List;

public abstract class AbstractPlayer implements Player {
    protected double x;
    protected double y;
    protected double spawnX;
    protected double spawnY;
    protected int hp;
    protected Direction currentDirection;
    protected final Maze maze;
    protected final List<Rectangle> walls;
    protected ImageView view;

    public AbstractPlayer(Maze maze) {
        this.maze = maze;
        this.walls = maze.getWalls();
        this.view = new ImageView();
    }

    public abstract void go();

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpawnX() {
        return spawnX;
    }

    public void setSpawnX(double spawnX) {
        this.spawnX = spawnX;
    }

    public double getSpawnY() {
        return spawnY;
    }

    public void setSpawnY(double spawnY) {
        this.spawnY = spawnY;
    }

    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }
}
