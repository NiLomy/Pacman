package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.MessageType;

public class EatPlayerEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

//        byte[] currentData = message.getData();
//        ByteBuffer buffer = ByteBuffer.allocate(currentData.length + 4);
//        buffer.putInt(connectionId);
//        buffer.put(currentData);
        server.sendBroadCastMessage(GameMessageProvider.createMessage(MessageType.EAT_PLAYER_RESPONSE, message.getData()));
    }

    @Override
    public int getType() {
        return MessageType.EAT_PLAYER_REQUEST;
    }
}
