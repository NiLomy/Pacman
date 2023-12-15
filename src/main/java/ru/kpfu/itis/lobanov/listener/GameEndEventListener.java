package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

public class GameEndEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

        server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.GAME_END_RESPONSE, message.getData()));
        server.endGame();
        server.closeServer();
    }

    @Override
    public int getType() {
        return MessageType.GAME_END_REQUEST;
    }
}
