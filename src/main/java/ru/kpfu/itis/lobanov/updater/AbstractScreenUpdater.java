package ru.kpfu.itis.lobanov.updater;

import ru.kpfu.itis.lobanov.server.Server;

public abstract class AbstractScreenUpdater implements ScreenUpdater {
    protected Server server;
    protected boolean isGameAlive;
    protected boolean isInit;

    @Override
    public void init(Server server) {
        this.server = server;
        this.isGameAlive = true;
        this.isInit = true;
    }
}
