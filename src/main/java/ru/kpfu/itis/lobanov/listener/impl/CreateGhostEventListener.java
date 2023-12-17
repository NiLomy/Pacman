package ru.kpfu.itis.lobanov.listener.impl;

import org.apache.commons.lang.SerializationUtils;
import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.listener.AbstractEventListener;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Ghost;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.nio.ByteBuffer;

public class CreateGhostEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit)
            throw new EventListenerException(String.format(LogMessages.INITIALIZE_LISTENER_EXCEPTION, CreateGhostEventListener.class.getSimpleName()));

        byte[] maze = SerializationUtils.serialize(server.getMaze());
        for (int i = 0; i < server.getGhosts().size(); i++) {
            Ghost ghost = server.getGhosts().get(i);
            ByteBuffer buffer = ByteBuffer.allocate(maze.length + GameSettings.DOUBLE_BYTES * 2 + GameSettings.INTEGER_BYTES);
            buffer.putInt(i);
            buffer.putDouble(ghost.getSpawnX());
            buffer.putDouble(ghost.getSpawnY());
            buffer.put(maze);
            Message response = GameMessageProvider.createMessage(MessageType.CREATE_GHOST_RESPONSE, buffer.array());
            server.sendMessage(connectionId, response);
        }
    }

    @Override
    public int getType() {
        return MessageType.CREATE_GHOST_REQUEST;
    }
}
