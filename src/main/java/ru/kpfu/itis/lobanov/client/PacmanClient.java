package ru.kpfu.itis.lobanov.client;

import ru.kpfu.itis.lobanov.controller.GameScreenController;
import ru.kpfu.itis.lobanov.exceptions.ClientException;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.protocol.MessageProtocol;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PacmanClient implements Client {
    private final String host;
    private final int port;
    private Socket socket;
    private ClientThread thread;
    private final GameScreenController controller;

    public PacmanClient(String host, int port, GameScreenController controller) {
        this.host = host;
        this.port = port;
        this.controller = controller;
    }

    @Override
    public void connect() {
        try {
            socket = new Socket(host, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            thread = new ClientThread(input, output, this);
            new Thread(thread).start();
        } catch (IOException e) {
            throw new ClientException("Can't connect to the server.", e);
        }
    }

    public void sendMessage(String message) {
        try {
            thread.getOut().write(message);
            thread.getOut().flush();
        } catch (IOException e) {
            throw new ClientException("Can't send data to the server.", e);
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
//            thread.getOut().write(MessageProtocol.getBytes(message));
            thread.getOut().flush();
        } catch (IOException e) {
            throw new ClientException("Can't send data to the server.", e);
        }
    }

    public ClientThread getThread() {
        return thread;
    }

    public static class ClientThread implements Runnable {

        private BufferedReader in;
        private BufferedWriter out;
        private final PacmanClient pacmanClient;
        private boolean alive = true;

        public ClientThread(BufferedReader in, BufferedWriter out, PacmanClient pacmanClient) {
            this.in = in;
            this.out = out;
            this.pacmanClient = pacmanClient;
        }

        @Override
        public void run() {
            try {
                while (alive) {
                    String message = in.readLine();
                    pacmanClient.controller.receiveMessage(message);
                }
            } catch (IOException e) {
                throw new ClientException("Can't read data from server.", e);
            }
        }

        public BufferedReader getIn() {
            return in;
        }

        public void setIn(BufferedReader in) {
            this.in = in;
        }

        public BufferedWriter getOut() {
            return out;
        }

        public void setOut(BufferedWriter out) {
            this.out = out;
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
