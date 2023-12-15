package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.nio.ByteBuffer;

public class SendClientsCountEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

        ByteBuffer buffer = ByteBuffer.allocate(GameSettings.INTEGER_BYTES).putInt(clientsCount);
        Message response = GameMessageProvider.createMessage(MessageType.USER_COUNT_INFO_RESPONSE, buffer.array());
        server.sendBroadCastMessage(response);
    }

    @Override
    public int getType() {
        return MessageType.USER_COUNT_INFO_REQUEST;
    }
}
