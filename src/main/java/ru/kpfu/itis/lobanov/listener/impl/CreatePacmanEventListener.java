package ru.kpfu.itis.lobanov.listener.impl;

import org.apache.commons.lang.SerializationUtils;
import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.listener.AbstractEventListener;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.nio.ByteBuffer;

public class CreatePacmanEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit)
            throw new EventListenerException(String.format(LogMessages.INITIALIZE_LISTENER_EXCEPTION, CreatePacmanEventListener.class.getSimpleName()));

        ByteBuffer buffer = ByteBuffer.allocate( GameSettings.DOUBLE_BYTES * 2);
        buffer.putDouble(server.getPacman().getSpawnX());
        buffer.putDouble(server.getPacman().getSpawnY());

        Message response = GameMessageProvider.createMessage(MessageType.CREATE_PACMAN_RESPONSE, buffer.array());
        server.sendMessage(connectionId, response);
    }

    @Override
    public int getType() {
        return MessageType.CREATE_PACMAN_REQUEST;
    }
}
