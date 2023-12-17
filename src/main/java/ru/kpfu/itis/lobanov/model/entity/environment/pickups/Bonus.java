package ru.kpfu.itis.lobanov.model.entity.environment.pickups;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.util.Objects;

public class Bonus extends ScoreLocatableObject {
    private Circle view;

    public Bonus(double x, double y, int score) {
        super(x, y, score);
    }

    public void show(Rectangle2D coordinates) {
        view = new Circle();
        view.setRadius(GameSettings.CELL_SIZE / 4);
        view.setFill(Color.BLUE);
        view.setCenterX(x + coordinates.getWidth() / 3);
        view.setCenterY(y + coordinates.getHeight() / 6);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Bonus bonus = (Bonus) o;

        return Objects.equals(view, bonus.view);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (view != null ? view.hashCode() : 0);
        return result;
    }

    public Circle getView() {
        return view;
    }

    public void setView(Circle view) {
        this.view = view;
    }
}
