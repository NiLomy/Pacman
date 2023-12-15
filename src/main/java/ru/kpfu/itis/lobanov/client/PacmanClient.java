package ru.kpfu.itis.lobanov.client;

import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.controller.GameScreenController;
import ru.kpfu.itis.lobanov.controller.MessageReceiverController;
import ru.kpfu.itis.lobanov.exceptions.ClientException;
import ru.kpfu.itis.lobanov.exceptions.MessageReadException;
import ru.kpfu.itis.lobanov.exceptions.MessageWriteException;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.protocol.MessageProtocol;

import java.io.*;
import java.net.Socket;

public class PacmanClient implements Client {
    private final String host;
    private final int port;
    private Socket socket;
    private ClientThread thread;
    private MessageReceiverController controller;

    public PacmanClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connect() throws ClientException {
        try {
            socket = new Socket(host, port);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            thread = new ClientThread(input, output, this);
            new Thread(thread).start();
        } catch (IOException e) {
            throw new ClientException("Can't connect to the server.", e);
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            MessageProtocol.writeMessage(thread.getOut(), message);
        } catch (MessageWriteException e) {
            throw new ClientException("Can't send data to the server.", e);
        }
    }

    public ClientThread getThread() {
        return thread;
    }

    public MessageReceiverController getController() {
        return controller;
    }

    public void setController(MessageReceiverController controller) {
        this.controller = controller;
    }

    public static class ClientThread implements Runnable {

        private final InputStream in;
        private final OutputStream out;
        private final PacmanClient pacmanClient;
        private boolean alive = true;

        public ClientThread(InputStream in, OutputStream out, PacmanClient pacmanClient) {
            this.in = in;
            this.out = out;
            this.pacmanClient = pacmanClient;
        }

        @Override
        public void run() {
            try {
                while (alive) {
                    Message message = MessageProtocol.readMessage(in);
                    if (message != null && pacmanClient.controller != null) pacmanClient.controller.receiveMessage(message);
                }
            } catch (MessageReadException e) {
                throw new ClientException("Client can't read a message.", e);
            }
        }

        public InputStream getIn() {
            return in;
        }

        public OutputStream getOut() {
            return out;
        }

        public void stop() {
            try {
                in.close();
                out.close();
                pacmanClient.socket.close();
                alive = false;
            } catch (IOException e) {
                throw new ClientException("Connection to the server is lost.", e);
            }
        }
    }
}
