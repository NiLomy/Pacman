package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.model.entity.net.Message;

public interface Server extends Runnable {
    void sendMessage(int connectionId, Message message);

    void sendBroadCastMessage(Message message);

    void closeServer();
}
