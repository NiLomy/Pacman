package ru.kpfu.itis.lobanov.model.environment.pickups;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import ru.kpfu.itis.lobanov.utils.GameSettings;

public class Pellet extends LocatableObject {
    private int score;
    private ImageView view;

    public Pellet(double x, double y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
//        view = new Circle();
        view = new ImageView();
        view.setImage(new Image("/images/dot.png"));
        view.setFitHeight(GameSettings.CELL_SIZE);
        view.setFitWidth(GameSettings.CELL_SIZE);
        view.setX(x);
        view.setY(y);
//        view.setRadius(GameSettings.CELL_SIZE / 5);
//        view.setFill(Color.GREEN);
//        view.setCenterX(x);
//        view.setCenterY(y);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

//    public Circle getView() {
//        return view;
//    }
//
//    public void setView(Circle view) {
//        this.view = view;
//    }


    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }
}
