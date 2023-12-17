package ru.kpfu.itis.lobanov.utils.repository;

import ru.kpfu.itis.lobanov.updater.ScreenUpdater;
import ru.kpfu.itis.lobanov.updater.impl.BonusBlinkScreenUpdater;
import ru.kpfu.itis.lobanov.updater.impl.GameStatusScreenUpdater;
import ru.kpfu.itis.lobanov.updater.impl.GhostScoreIncreaseScreenUpdater;
import ru.kpfu.itis.lobanov.updater.impl.TimeScreenUpdater;

public class UpdatersRepository {
    public static ScreenUpdater[] getScreenUpdaters() {
        return new ScreenUpdater[]{
                new BonusBlinkScreenUpdater(),
                new GameStatusScreenUpdater(),
                new GhostScoreIncreaseScreenUpdater(),
                new TimeScreenUpdater()
        };
    }

    private UpdatersRepository() {
    }
}
