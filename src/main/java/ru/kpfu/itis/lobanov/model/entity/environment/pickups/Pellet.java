package ru.kpfu.itis.lobanov.model.entity.environment.pickups;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

public class Pellet extends LocatableObject {
    private int score;
    private ImageView view;

    public Pellet(double x, double y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }

    public void show() {
        view = new ImageView();
        view.toBack();
        view.setImage(new Image("/images/pellet2.png"));
        view.setFitHeight(GameSettings.CELL_SIZE / 5);
        view.setFitWidth(GameSettings.CELL_SIZE / 5);
        view.setX(x);
        view.setY(y);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }
}
