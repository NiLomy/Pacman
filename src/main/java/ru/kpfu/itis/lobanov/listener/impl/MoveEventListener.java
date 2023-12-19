package ru.kpfu.itis.lobanov.listener.impl;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.listener.AbstractEventListener;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.nio.ByteBuffer;

public class MoveEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit)
            throw new EventListenerException(String.format(LogMessages.INITIALIZE_LISTENER_EXCEPTION, MoveEventListener.class.getSimpleName()));

        byte[] currentData = message.getData();

        ByteBuffer buffer = ByteBuffer.allocate(currentData.length + GameSettings.INTEGER_BYTES);
        buffer.putInt(connectionId);
        buffer.put(currentData);

        server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.MOVEMENT_RESPONSE, buffer.array()));
    }

    @Override
    public int getType() {
        return MessageType.MOVEMENT_REQUEST;
    }
}
