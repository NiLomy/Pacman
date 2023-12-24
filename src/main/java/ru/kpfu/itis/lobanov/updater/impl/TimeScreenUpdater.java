package ru.kpfu.itis.lobanov.updater.impl;

import ru.kpfu.itis.lobanov.exceptions.ScreenUpdaterException;
import ru.kpfu.itis.lobanov.updater.AbstractScreenUpdater;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.nio.ByteBuffer;
import java.util.ConcurrentModificationException;

public class TimeScreenUpdater extends AbstractScreenUpdater {
    @Override
    public void run() {
        if (!isInit)
            throw new ScreenUpdaterException(String.format(LogMessages.INITIALIZE_UPDATER_EXCEPTION, TimeScreenUpdater.class.getSimpleName()));
        try {
            Thread.sleep(GameSettings.GAME_DOWNLOADING_TIME);
        } catch (InterruptedException e) {
            isGameAlive = false;
        }
        int time = 0;
        while (isGameAlive) {
            try {
                time++;
                server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.TIME_RESPONSE, ByteBuffer.allocate(4).putInt(time).array()));
            } catch (ConcurrentModificationException ignored) {
            }
            try {
                Thread.sleep(GameSettings.TIME_UPDATE_FREQUENCY);
            } catch (InterruptedException e) {
                isGameAlive = false;
            }
        }
    }
}
