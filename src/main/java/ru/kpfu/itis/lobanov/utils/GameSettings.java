package ru.kpfu.itis.lobanov.utils;

import javafx.util.Duration;

public class GameSettings {
    public static final int SIZE = 11;
    public static final double CELL_SIZE = 20;
    public static final int PACMAN_HP = 3;
    public static final int GHOST_HP = 1;
    public static final int PACMAN_SPEED = 10;
    public static final int GHOST_SPEED = 5;
    public static final int PELLET_SCORES = 20;
    public static final int BONUS_SCORES = 20;
    public static final int CALM_COUNT = 5;
    public static final int BONUS_DURATION = 5 * 1_000;
    public static final Duration UPDATE_FREQUENCY = Duration.millis(200);

    private GameSettings() {}
}
