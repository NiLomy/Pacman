package ru.kpfu.itis.lobanov.client;

import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.controller.GameScreenController;
import ru.kpfu.itis.lobanov.exceptions.ClientException;
import ru.kpfu.itis.lobanov.exceptions.MessageReadException;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.protocol.MessageProtocol;

import java.io.*;
import java.net.Socket;

public class PacmanClient implements Client {
    private final String host;
    private final int port;
    private Socket socket;
    private ClientThread thread;
    private Controller controller;

    public PacmanClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public PacmanClient(String host, int port, GameScreenController controller) {
        this.host = host;
        this.port = port;
        this.controller = controller;
    }

    @Override
    public void connect() {
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
            thread.getOut().write(MessageProtocol.getBytes(message));
            thread.getOut().flush();
        } catch (IOException e) {
            throw new ClientException("Can't send data to the server.", e);
        }
    }

    public ClientThread getThread() {
        return thread;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
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
                    if (pacmanClient.controller != null) pacmanClient.controller.receiveMessage(message);
                }
            } catch (MessageReadException e) {
                throw new RuntimeException(e);
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
