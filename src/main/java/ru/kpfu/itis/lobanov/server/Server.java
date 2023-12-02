package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.model.net.Message;

public interface Server {
//    public void registerListener(ServerEventListener listener) throws ServerException;
    void start();
    void sendMessage(int connectionId, Message message);
    void sendBroadCastMessage(Message message);
}
