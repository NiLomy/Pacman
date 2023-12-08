package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.MessageType;

public class GameLooseEndEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

        server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.GAME_LOOSE_RESPONSE, message.getData()));
        server.closeServer();
    }

    @Override
    public int getType() {
        return MessageType.GAME_LOOSE_REQUEST;
    }
}
