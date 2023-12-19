package ru.kpfu.itis.lobanov.utils.constants;

/**
 * This class provides strings for different resources
 */
public class GameResources {
    // localization
    public static final String LOCALIZED_TEXTS_RESOURCE_BUNDLE = "property/game_strings";
    // views for app
    public static final String START_SCREEN = "/view/start_screen.fxml";
    public static final String SETTINGS_SCREEN = "/view/settings_screen.fxml";
    public static final String ROOMS_SCREEN = "/view/rooms_screen.fxml";
    public static final String WAITING_ROOM_SCREEN = "/view/waiting_room_screen.fxml";
    public static final String FULL_ROOM_SCREEN = "/view/full_room_screen.fxml";
    public static final String CREATE_ROOM_SCREEN = "/view/create_room_screen.fxml";
    public static final String GAME_SCREEN = "/view/game_screen.fxml";
    public static final String GAME_OVER_SCREEN = "/view/game_over_screen.fxml";
    // text resources
    public static final String GAME_STATS = "game.stats";
    public static final String GAME_SCORES = "game.scores";
    public static final String GAME_PLAYED_TIME = "game.played.time";
    public static final String GAME_WIN = "game.win";
    public static final String GAME_LOSE = "game.lose";
    public static final String ROOM_CREATE = "room.create";
    public static final String ROOM_ENTER = "room.enter";
    public static final String TIME_PASSED = "time.passed";
    public static final String GO_BACK = "go_back";
    public static final String NO_ROOMS = "no_rooms";
    // sprites packages
    public static final String PACMAN_GIF = "/images/pacman/pacman.gif";
    public static final String PELLET_IMAGE = "/images/pellets/pellet1.png";
    public static final String PACMAN_CALM_IMAGE = "/images/pacman/calm/pacman.png";
    public static final String UP_WIDE_OPEN_PACMAN_IMAGE = "/images/pacman/up/1.png";
    public static final String DOWN_WIDE_OPEN_PACMAN_IMAGE = "/images/pacman/down/1.png";
    public static final String LEFT_WIDE_OPEN_PACMAN_IMAGE = "/images/pacman/left/1.png";
    public static final String RIGHT_WIDE_OPEN_PACMAN_IMAGE = "/images/pacman/right/1.png";
    public static final String UP_OPEN_PACMAN_IMAGE = "/images/pacman/up/2.png";
    public static final String DOWN_OPEN_PACMAN_IMAGE = "/images/pacman/down/2.png";
    public static final String LEFT_OPEN_PACMAN_IMAGE = "/images/pacman/left/2.png";
    public static final String RIGHT_OPEN_PACMAN_IMAGE = "/images/pacman/right/2.png";
    public static final String FRIGHTENED_GHOST_IMAGE = "/images/ghosts/frightened/1.png";
    public static final String GHOST_PACKAGE_PREFIX = "/images/ghosts";
    public static final String RED_GHOST_PACKAGE = "/red-ghost";
    public static final String BLUE_GHOST_PACKAGE = "/blue-ghost";
    public static final String GREEN_GHOST_PACKAGE = "/green-ghost";
    public static final String UP_IMAGE = "/up.png";
    public static final String DOWN_IMAGE = "/down.png";
    public static final String LEFT_IMAGE = "/left.png";
    public static final String RIGHT_IMAGE = "/right.png";
    // styles
    public static final String BIG_CENTER_TEXT = "-fx-font-size: 32; -fx-alignment: center; -fx-text-alignment: center; -fx-font-weight: bold; -fx-font-family: 'Comic Sans MS'";
    public static final String MEDIUM_TEXT = "-fx-font-size: 24; -fx-font-family: 'Comic Sans MS'";
    // localization languages
    public static final String ENGLISH_LOCALIZATION = "en";
    public static final String RUSSIAN_LOCALIZATION = "ru";
    // db property keys
    public static final String URL_KEY = "db.url";
    public static final String USER_KEY = "db.user";
    public static final String PASSWORD_KEY = "db.password";

    private GameResources() {
    }
}
