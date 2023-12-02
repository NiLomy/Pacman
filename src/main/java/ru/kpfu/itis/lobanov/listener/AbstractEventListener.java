package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.server.Server;

public abstract class AbstractEventListener implements EventListener {
    protected boolean isInit = false;
    protected Server server;

    @Override
    public void init(Server server) {
        this.server = server;
        this.isInit = true;
    }
}
