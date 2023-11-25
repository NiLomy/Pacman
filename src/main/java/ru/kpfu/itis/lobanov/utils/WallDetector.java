package ru.kpfu.itis.lobanov.utils;

import javafx.scene.shape.Rectangle;

import java.util.List;

public class WallDetector {
    public static boolean checkForWall(Direction direction, double currentX, double currentY, List<Rectangle> walls) {
        for (Rectangle wall : walls) {
            double wallX = wall.getX();
            double wallY = wall.getY();

            if (direction == Direction.UP && wallY < currentY) {
                if ((wallY + GameSettings.CELL_SIZE / 2 >= currentY - GameSettings.PACMAN_SPEED) && checkForNoWallX(currentX, wallX)) {
                    return true;
                }
            }
            if (direction == Direction.DOWN && wallY > currentY) {
                if ((wallY - GameSettings.CELL_SIZE / 2 <= currentY + GameSettings.PACMAN_SPEED) && checkForNoWallX(currentX, wallX)) {
                    return true;
                }
            }
            if (direction == Direction.LEFT && wallX < currentX) {
                if ((wallX + GameSettings.CELL_SIZE / 2 >= currentX - GameSettings.PACMAN_SPEED) && checkForNoWallY(currentY, wallY)) {
                    return true;
                }
            }
            if (direction == Direction.RIGHT && wallX > currentX) {
                if ((wallX - GameSettings.CELL_SIZE / 2 <= currentX + GameSettings.PACMAN_SPEED) && checkForNoWallY(currentY, wallY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkForNoWallX(double currentX, double wallX) {
        return wallX <= currentX && wallX + GameSettings.CELL_SIZE > currentX;
    }

    private static boolean checkForNoWallY(double currentY, double wallY) {
        return wallY <= currentY && wallY + GameSettings.CELL_SIZE > currentY;
    }

    private WallDetector() {
    }
}
