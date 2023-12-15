package ru.kpfu.itis.lobanov.listener;

import org.apache.commons.lang.SerializationUtils;
import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

import java.util.ArrayList;

public class CreatePelletsEventListener extends AbstractEventListener {
    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

        Message response = GameMessageProvider.createMessage(MessageType.CREATE_PELLETS_RESPONSE, SerializationUtils.serialize((ArrayList<Pellet>) server.getPellets()));
        server.sendMessage(connectionId, response);
    }

    @Override
    public int getType() {
        return MessageType.CREATE_PELLETS_REQUEST;
    }
}
