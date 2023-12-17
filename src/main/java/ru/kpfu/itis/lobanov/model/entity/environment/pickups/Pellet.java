package ru.kpfu.itis.lobanov.model.entity.environment.pickups;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.util.Objects;

public class Pellet extends ScoreLocatableObject {
    private ImageView view;

    public Pellet(double x, double y, int score) {
        super(x, y, score);
    }

    public void show(Rectangle2D coordinates) {
        view = new ImageView();
        view.toBack();
        view.setImage(new Image(GameResources.PELLET_IMAGE));
        view.setFitHeight(GameSettings.CELL_SIZE / 5);
        view.setFitWidth(GameSettings.CELL_SIZE / 5);
        view.setX(x + coordinates.getWidth() / 3);
        view.setY(y + coordinates.getHeight() / 6);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Pellet pellet = (Pellet) o;

        return Objects.equals(view, pellet.view);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (view != null ? view.hashCode() : 0);
        return result;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }
}
