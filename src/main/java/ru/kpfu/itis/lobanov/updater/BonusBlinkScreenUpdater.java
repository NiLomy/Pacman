package ru.kpfu.itis.lobanov.updater;

import ru.kpfu.itis.lobanov.exceptions.ScreenUpdaterException;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

public class BonusBlinkScreenUpdater extends AbstractScreenUpdater {

    @Override
    public void run() {
        if (!isInit) throw new ScreenUpdaterException("Screen updater hasn't been initialized yet.");
        try {
            Thread.sleep(GameSettings.GAME_DOWNLOADING_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (isGameAlive) {
            server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.BLINK_BONUSES_RESPONSE, new byte[0]));
            try {
                Thread.sleep(GameSettings.BONUS_BLINK_UPDATE_FREQUENCY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
