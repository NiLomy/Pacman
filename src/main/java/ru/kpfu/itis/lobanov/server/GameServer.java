package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.listener.EventListener;

public interface GameServer extends Server {
    void registerListener(EventListener listener);

    void endGame();
}
