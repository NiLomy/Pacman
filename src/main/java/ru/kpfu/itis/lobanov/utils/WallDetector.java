package ru.kpfu.itis.lobanov.utils;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.lobanov.utils.constants.Direction;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.util.List;

public class WallDetector {
    public static boolean checkForWall(Direction direction, List<Rectangle> walls, ImageView view, double offsetX, double offsetY) {
        for (Rectangle w : walls) {
            Rectangle wall = new Rectangle(w.getX() + offsetX, w.getY() + offsetY, w.getWidth(), w.getHeight());
            if (direction != null) switch (direction) {
                case UP:
                    view.setY(view.getY() - GameSettings.PACMAN_SPEED);
                    if (wall.getBoundsInParent().intersects(view.getBoundsInParent())) {
                        view.setY(view.getY() + GameSettings.PACMAN_SPEED);
                        return true;
                    }
                    view.setY(view.getY() + GameSettings.PACMAN_SPEED);
                    break;
                case DOWN:
                    view.setY(view.getY() + GameSettings.PACMAN_SPEED);
                    if (wall.getBoundsInParent().intersects(view.getBoundsInParent())) {
                        view.setY(view.getY() - GameSettings.PACMAN_SPEED);
                        return true;
                    }
                    view.setY(view.getY() - GameSettings.PACMAN_SPEED);
                    break;
                case LEFT:
                    view.setX(view.getX() - GameSettings.PACMAN_SPEED);
                    if (wall.getBoundsInParent().intersects(view.getBoundsInParent())) {
                        view.setX(view.getX() + GameSettings.PACMAN_SPEED);
                        return true;
                    }
                    view.setX(view.getX() + GameSettings.PACMAN_SPEED);
                    break;
                case RIGHT:
                    view.setX(view.getX() + GameSettings.PACMAN_SPEED);
                    if (wall.getBoundsInParent().intersects(view.getBoundsInParent())) {
                        view.setX(view.getX() - GameSettings.PACMAN_SPEED);
                        return true;
                    }
                    view.setX(view.getX() - GameSettings.PACMAN_SPEED);
                    break;
            }
        }
        return false;
    }

    private WallDetector() {
    }
}
