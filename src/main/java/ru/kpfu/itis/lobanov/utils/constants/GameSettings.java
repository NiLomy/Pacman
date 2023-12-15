package ru.kpfu.itis.lobanov.utils.constants;

import java.util.Locale;

public class GameSettings {
    public static final int MAZE_SIZE = 19;
    public static final int RANDOM_PASSAGES_COUNT = 20;
    public static double CELL_SIZE = 20;
    public static final int PACMAN_HP = 3;
    public static final int GHOST_HP = 1;
    public static final int PACMAN_SPEED = 3;
    public static final int GHOST_SPEED = 2;
    public static final int PELLET_SCORES = 20;
    public static final int BONUS_SCORES = 20;
    public static final int CALM_COUNT = 5;
    public static final int MOVING_COUNT = 4;
    public static final int BONUS_DURATION = 5 * 1_000;
    public static final int PLAYERS_COUNT = 2;
    public static final int GAME_STATUS_UPDATE_FREQUENCY = 60;
    public static final int BONUS_BLINK_UPDATE_FREQUENCY = 120;
    public static final int GHOST_SCORE_INCREASE_UPDATE_FREQUENCY = 2 * 1_000;
    public static final int GHOST_SCORES_DEFAULT = 20;
    public static final int PACMAN_EAT_GHOST_BONUS = 500;
    public static final int GHOST_EAT_PACMAN_BONUS = 1_000;
    public static final int TIME_UPDATE_FREQUENCY = 1_000;
    public static final int GAME_DOWNLOADING_TIME = 250;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int INTEGER_BYTES = 4;
    public static final int DOUBLE_BYTES = 8;
    public static Locale LOCALE = new Locale("en");

    private GameSettings() {}
}
