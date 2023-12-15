package ru.kpfu.itis.lobanov.utils;

import ru.kpfu.itis.lobanov.updater.*;

public class UpdatersRepository {
    public static ScreenUpdater[] getScreenUpdaters() {
        return new ScreenUpdater[] {
                new BonusBlinkScreenUpdater(),
                new GameStatusScreenUpdater(),
                new GhostScoreIncreaseScreenUpdater(),
                new TimeScreenUpdater()
        };
    }

    private UpdatersRepository() {}
}
