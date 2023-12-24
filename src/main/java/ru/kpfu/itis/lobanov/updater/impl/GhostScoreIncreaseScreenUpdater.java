package ru.kpfu.itis.lobanov.updater.impl;

import ru.kpfu.itis.lobanov.exceptions.ScreenUpdaterException;
import ru.kpfu.itis.lobanov.updater.AbstractScreenUpdater;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

public class GhostScoreIncreaseScreenUpdater extends AbstractScreenUpdater {

    @Override
    public void run() {
        if (!isInit)
            throw new ScreenUpdaterException(String.format(LogMessages.INITIALIZE_UPDATER_EXCEPTION, GhostScoreIncreaseScreenUpdater.class.getSimpleName()));
        try {
            Thread.sleep(GameSettings.GAME_DOWNLOADING_TIME);
        } catch (InterruptedException e) {
            isGameAlive = false;
        }
        while (isGameAlive) {
            server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.GHOST_SCORES_RESPONSE, new byte[0]));
            try {
                Thread.sleep(GameSettings.GHOST_SCORE_INCREASE_UPDATE_FREQUENCY);
            } catch (InterruptedException e) {
                isGameAlive = false;
            }
        }
    }
}
