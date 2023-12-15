package ru.kpfu.itis.lobanov.updater;

import ru.kpfu.itis.lobanov.exceptions.ScreenUpdaterException;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.nio.ByteBuffer;

public class TimeScreenUpdater extends AbstractScreenUpdater {
    @Override
    public void run() {
        if (!isInit) throw new ScreenUpdaterException("Screen updater hasn't been initialized yet.");
        try {
            Thread.sleep(GameSettings.GAME_DOWNLOADING_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int time = 0;
        while (isGameAlive) {
            time++;
            server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.TIME_RESPONSE, ByteBuffer.allocate(4).putInt(time).array()));
            try {
                Thread.sleep(GameSettings.TIME_UPDATE_FREQUENCY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
