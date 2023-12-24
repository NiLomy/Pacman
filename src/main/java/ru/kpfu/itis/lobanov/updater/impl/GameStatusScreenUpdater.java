package ru.kpfu.itis.lobanov.updater.impl;

import ru.kpfu.itis.lobanov.exceptions.ScreenUpdaterException;
import ru.kpfu.itis.lobanov.updater.AbstractScreenUpdater;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.util.ConcurrentModificationException;

public class GameStatusScreenUpdater extends AbstractScreenUpdater {

    @Override
    public void run() {
        if (!isInit)
            throw new ScreenUpdaterException(String.format(LogMessages.INITIALIZE_UPDATER_EXCEPTION, GameStatusScreenUpdater.class.getSimpleName()));
        try {
            Thread.sleep(GameSettings.GAME_DOWNLOADING_TIME);
        } catch (InterruptedException e) {
            isGameAlive = false;
        }
        while (isGameAlive) {
            try {
                server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.PLAYERS_MOVE_RESPONSE, new byte[0]));
                server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.PACMAN_EAT_PELLET_RESPONSE, new byte[0]));
                server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.PACMAN_EAT_BONUS_RESPONSE, new byte[0]));
                server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.CHANGE_SCORES_RESPONSE, new byte[0]));
                server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.GAME_WIN_RESPONSE, new byte[0]));
            } catch (ConcurrentModificationException ignored) {
            }
            try {
                Thread.sleep(GameSettings.GAME_STATUS_UPDATE_FREQUENCY);
            } catch (InterruptedException e) {
                isGameAlive = false;
            }
        }
    }
}
