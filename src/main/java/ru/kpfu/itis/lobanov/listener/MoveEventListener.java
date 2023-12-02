package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.utils.MessageType;

import java.nio.ByteBuffer;

public class MoveEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int info) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

        byte[] currentData = message.getData();
        ByteBuffer buffer = ByteBuffer.allocate(currentData.length + 4);
        buffer.putInt(connectionId);
        buffer.put(currentData);
        message.setData(buffer.array());
        server.sendBroadCastMessage(message);
    }

    @Override
    public int getType() {
        return MessageType.MOVEMENT;
    }
}