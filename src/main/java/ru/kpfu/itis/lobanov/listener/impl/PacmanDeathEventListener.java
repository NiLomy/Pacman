package ru.kpfu.itis.lobanov.listener.impl;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.listener.AbstractEventListener;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;

// TODO delete or rework this class
public class PacmanDeathEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit)
            throw new EventListenerException(String.format(LogMessages.INITIALIZE_LISTENER_EXCEPTION, PacmanDeathEventListener.class.getSimpleName()));

//        server.sendMessage(connectionId, GameMessageProvider.createMessage(MessageType.RES, message.getData()));
    }

    @Override
    public int getType() {
//        return MessageType.REQ;
        return 1000000;
    }
}
