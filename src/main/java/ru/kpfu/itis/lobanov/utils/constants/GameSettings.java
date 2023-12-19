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
    public static final int GAME_STATUS_UPDATE_FREQUENCY = 60;
    public static final int BONUS_BLINK_UPDATE_FREQUENCY = 120;
    public static final int GHOST_SCORE_INCREASE_UPDATE_FREQUENCY = 2 * 1_000;
    public static final int GHOST_SCORES_DEFAULT = 20;
    public static final int PACMAN_EAT_GHOST_BONUS = 500;
    public static final int GHOST_EAT_PACMAN_BONUS = 600;
    public static final int TIME_UPDATE_FREQUENCY = 1_000;
    public static final int GAME_DOWNLOADING_TIME = 350;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int INTEGER_BYTES = 4;
    public static final int DOUBLE_BYTES = 8;
    public static Locale LOCALE = new Locale("en");


    // duration
    public static final int GHOST_RESPAWN_DELAY = 3 * 1_000;
    public static final int PACMAN_GIF_DURATION_MILLIS = 4_000;
    public static final int PACMAN_GIF_CYCLE_TIME_INDEFINITE = -1;
    // game levellers
    public static final int PLAYER_SET_UP_COORDINATE_BIAS = 3;
    public static final int PLAYER_SET_UP_VIEW_SIZE_BIAS = 4;
    public static final int SCREEN_WIDTH_DIVIDER = 3;
    public static final int SCREEN_HEIGHT_DIVIDER = 6;
    // sizes
    public static final int PACMAN_GIF_WIDTH = 550;
    public static final int PACMAN_GIF_HEIGHT = 300;
    public static final int SERVERS_SCROLL_PANE_MAX_HEIGHT = 400;
    public static final int MAZE_SIZE_FOR_ROOMS_PREVIEW = 15;
    public static final int MAZE_SIZE_FOR_CREATION_PREVIEW = 18;
    public static final double SMALL_CELL_SIZE = 20;
    public static final double MEDIUM_CELL_SIZE = 22.5;
    public static final double LARGE_CELL_SIZE = 25;
    // spacings
    public static final int SERVER_BOX_SPACING = 24;
    public static final int MAZE_BOX_SPACING = 10;
    // system properties
    public static final int PORT_LENGTH = 4;
    // decoding
    public static final char WALL_DECODER_CHAR = '1';
    public static final char SPACE_DECODER_CHAR = '0';

    private GameSettings() {
    }
}
