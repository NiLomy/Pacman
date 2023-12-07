package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.MessageType;

import java.nio.ByteBuffer;

public class SendIdEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int info) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

        ByteBuffer buffer = ByteBuffer.allocate(4).putInt(connectionId);
        Message response = GameMessageProvider.createMessage(MessageType.USER_ID_RESPONSE, buffer.array());
        server.sendMessage(connectionId, response);
    }

    @Override
    public int getType() {
        return MessageType.USER_ID_REQUEST;
    }
}
