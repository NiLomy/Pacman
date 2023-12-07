package ru.kpfu.itis.lobanov.model.entity.environment.pickups;

import javafx.scene.Node;

import java.io.Serializable;

public abstract class LocatableObject implements Serializable {
    protected double x;
    protected double y;

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
}
