package ru.kpfu.itis.lobanov.client;

import ru.kpfu.itis.lobanov.model.net.Message;

public interface Client {
    void connect();
    void sendMessage(Message message);
}
