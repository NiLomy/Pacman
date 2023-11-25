package ru.kpfu.itis.lobanov.model.environment.pickups;

import javafx.scene.Node;

public abstract class LocatableObject {
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
