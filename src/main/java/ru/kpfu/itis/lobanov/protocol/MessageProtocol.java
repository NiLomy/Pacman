package ru.kpfu.itis.lobanov.protocol;

import ru.kpfu.itis.lobanov.exceptions.*;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MessageProtocol {
    public static final byte[] VERSION_BYTES = {0x0, 0x1};
    public static final int MAX_MESSAGE_LENGTH = 100 * 1024;
    private static final int INTEGER_BYTES = 4;

    public static Message readMessage(InputStream in) throws MessageReadException {
        byte[] buffer = new byte[VERSION_BYTES.length + INTEGER_BYTES * 2];
        try {
            in.read(buffer, 0, VERSION_BYTES.length);
            int counter = 0;
            for (int i = 0; i < VERSION_BYTES.length; i++) {
                if (buffer[i] == 0) counter++;
            }
            if (counter == VERSION_BYTES.length) return null;
            if (!Arrays.equals(Arrays.copyOfRange(buffer, 0, VERSION_BYTES.length), VERSION_BYTES)) {
                throw new InvalidProtocolVersionException("Versions of the protocols don't match. Message first bytes must be: " + Arrays.toString(VERSION_BYTES));
            }

            in.read(buffer, 0, INTEGER_BYTES);
            int messageType = ByteBuffer.wrap(buffer, 0, INTEGER_BYTES).getInt();
            if (!MessageType.getAllTypes().contains(messageType)) {
                throw new InvalidMessageTypeException("Wrong message type: " + messageType + ".");
            }

            in.read(buffer, 0, INTEGER_BYTES);
            int messageLength = ByteBuffer.wrap(buffer, 0, INTEGER_BYTES).getInt();
            if (messageLength > MAX_MESSAGE_LENGTH) {
                throw new InvalidMessageLengthException("Protocol doesn't support this message length: " + messageLength + ". Message length can't be greater than " + MAX_MESSAGE_LENGTH + " bytes length.");
            }

            buffer = new byte[messageLength];
            in.read(buffer, 0, messageLength);
            return GameMessageProvider.createMessage(messageType, buffer);
        } catch (IOException e) {
            try {
                in.close();
            } catch (IOException ex) {
                throw new MessageReadException("Can't read message.", e);
            }
        }
        return null;
    }

    public static void writeMessage(OutputStream out, Message message) throws MessageWriteException {
        try {
            out.write(MessageProtocol.getBytes(message));
            out.flush();
        } catch (IOException e) {
            throw new MessageWriteException("Can't write message.", e);
        }
    }

    public static byte[] getBytes(Message message) {
        byte[] bytes = new byte[VERSION_BYTES.length + INTEGER_BYTES * 2 + message.getData().length];
        int index = 0;
        for (byte versionByte : VERSION_BYTES) {
            bytes[index++] = versionByte;
        }
        byte[] messageType = ByteBuffer.allocate(INTEGER_BYTES).putInt(message.getType()).array();
        for (byte typeByte : messageType) {
            bytes[index++] = typeByte;
        }
        byte[] messageLength = ByteBuffer.allocate(INTEGER_BYTES).putInt(message.getData().length).array();
        for (byte lengthByte : messageLength) {
            bytes[index++] = lengthByte;
        }
        byte[] messageData = message.getData();
        for (byte dataByte : messageData) {
            bytes[index++] = dataByte;
        }
        return bytes;
    }
}
