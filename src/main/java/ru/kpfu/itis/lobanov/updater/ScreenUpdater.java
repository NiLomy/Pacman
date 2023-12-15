package ru.kpfu.itis.lobanov.updater;

import ru.kpfu.itis.lobanov.server.Server;

public interface ScreenUpdater extends Runnable {
    void init(Server server);
    void setGameAlive(boolean gameAlive);
}
