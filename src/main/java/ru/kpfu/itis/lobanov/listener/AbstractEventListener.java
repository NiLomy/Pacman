package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.server.GameServer;
import ru.kpfu.itis.lobanov.server.PacmanServer;
import ru.kpfu.itis.lobanov.server.Server;

public abstract class AbstractEventListener implements EventListener {
    protected boolean isInit = false;
    protected PacmanServer server;

    @Override
    public void init(PacmanServer server) {
        this.server = server;
        this.isInit = true;
    }
}
