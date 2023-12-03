package ru.kpfu.itis.lobanov.model.environment.pickups;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import ru.kpfu.itis.lobanov.utils.GameSettings;

public class Bonus extends LocatableObject {
    private int score;
    private Circle view;

    public Bonus(double x, double y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }

    public void show() {
        view = new Circle();
        view.setRadius(GameSettings.CELL_SIZE / 4);
        view.setFill(Color.BLUE);
        view.setCenterX(x);
        view.setCenterY(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bonus bonus = (Bonus) o;

        if (x != bonus.getX() || y != bonus.getY()) return false;
        return score == bonus.score;
    }

    @Override
    public int hashCode() {
        int result = score;
        result = 31 * result + view.hashCode();
        return result;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Circle getView() {
        return view;
    }

    public void setView(Circle view) {
        this.view = view;
    }
}
