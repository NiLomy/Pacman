package ru.kpfu.itis.lobanov.client;

import ru.kpfu.itis.lobanov.controller.GameScreenController;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PacmanClient {
    private Socket socket;
    private ClientThread thread;
    private GameScreenController controller;

    public PacmanClient(GameScreenController controller) {
        this.controller = controller;
    }

    public void sendMessage(String message) {
        try {
            thread.getOut().write(message);
            thread.getOut().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        String host = "127.0.0.1";
        int port = 5555;

        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader input;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter output;
        try {
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        thread = new ClientThread(input, output, this);
        new Thread(thread).start();
    }

    public ClientThread getThread() {
        return thread;
    }

    public static class ClientThread implements Runnable {

        private BufferedReader in;
        private BufferedWriter out;
        private PacmanClient pacmanClient;
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
                throw new RuntimeException(e);
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
                throw new RuntimeException(e);
            }
        }
    }
}
