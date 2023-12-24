package ru.kpfu.itis.lobanov.listener.impl;

import org.apache.commons.lang.SerializationUtils;
import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.listener.AbstractEventListener;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.util.Arrays;

public class CreateWallsEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit)
            throw new EventListenerException(String.format(LogMessages.INITIALIZE_LISTENER_EXCEPTION, CreateWallsEventListener.class.getSimpleName()));

        byte[] maze = SerializationUtils.serialize(server.getMaze());
        System.out.println(Arrays.toString(maze));
        Message response = GameMessageProvider.createMessage(MessageType.CREATE_WALLS_RESPONSE, maze);
        server.sendMessage(connectionId, response);
    }

    @Override
    public int getType() {
        return MessageType.CREATE_WALLS_REQUEST;
    }
}
