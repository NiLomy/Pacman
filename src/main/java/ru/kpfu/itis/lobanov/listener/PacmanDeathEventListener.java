package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;

public class PacmanDeathEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

//        server.sendMessage(connectionId, GameMessageProvider.createMessage(MessageType.RES, message.getData()));
    }

    @Override
    public int getType() {
//        return MessageType.REQ;
        return 1000000;
    }
}
