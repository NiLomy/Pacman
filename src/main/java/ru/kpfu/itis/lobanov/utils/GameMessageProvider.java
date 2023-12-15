package ru.kpfu.itis.lobanov.utils;

import ru.kpfu.itis.lobanov.exceptions.InvalidMessageTypeException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.constants.MessageType;

public class GameMessageProvider {
    public static Message createMessage(int type, byte[] data) {
        if (!MessageType.getAllTypes().contains(type)) {
            throw new InvalidMessageTypeException("Wrong message type.");
        }

        return new Message(type, data);
    }
}
