package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.util.Timer;
import java.util.TimerTask;

public class RushModeEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

        server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.RUSH_MODE_RESPONSE, message.getData()));
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.RUSH_MODE_RESPONSE, new byte[] {0}));
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, GameSettings.BONUS_DURATION);
    }

    @Override
    public int getType() {
        return MessageType.RUSH_MODE_REQUEST;
    }
}
