package ru.kpfu.itis.lobanov.model.player;

import ru.kpfu.itis.lobanov.utils.Direction;

public abstract class AbstractPlayer implements Player {
    protected double x;
    protected double y;
    protected double spawnX;
    protected double spawnY;
    protected int hp;

    public abstract void go(Direction currentDirection);

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
}
