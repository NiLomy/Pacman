package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.server.GameServer;
import ru.kpfu.itis.lobanov.server.PacmanServer;
import ru.kpfu.itis.lobanov.server.Server;

public interface EventListener {
    void init(PacmanServer server);

    void handle(Message message, int connectionId, int clientsCount) throws EventListenerException;

    int getType();
}
