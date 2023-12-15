package ru.kpfu.itis.lobanov.controller;

import ru.kpfu.itis.lobanov.model.entity.net.Message;

public interface MessageReceiverController extends Controller {
    void receiveMessage(Message message);
}
