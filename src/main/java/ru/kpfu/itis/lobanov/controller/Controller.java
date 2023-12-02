package ru.kpfu.itis.lobanov.controller;

import ru.kpfu.itis.lobanov.model.net.Message;

public interface Controller {
    void receiveMessage(Message message);
}
