package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.client.PacmanClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PacmanServer {
    private ServerSocket serverSocket;
    private List<Client> clients = new ArrayList<>();

    public void start() {
        try {
            serverSocket = new ServerSocket(5555);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));

                Client client = new Client(input, output, this, clientSocket);
                clients.add(client);

                new Thread(client).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message, Client client) {
        for (Client c : clients) {
            try {
                c.getOutput().write(client.id + message);
                c.getOutput().flush();
            } catch (IOException e) {
                c.stop();
            }
        }
    }

    public static void main(String[] args) {
        PacmanServer pacmanServer = new PacmanServer();
        pacmanServer.start();
    }

    static class Client implements Runnable {
        private BufferedReader input;
        private BufferedWriter output;
        private PacmanServer server;
        private Socket clientSocket;
        private boolean alive = true;
        private int id;

        public Client(BufferedReader input, BufferedWriter output, PacmanServer server, Socket clientSocket) {
            this.id = server.clients.size();
            this.input = input;
            this.output = output;
            this.server = server;
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                while (alive) {
                    String message = input.readLine();
                    server.sendMessage(message, this);
                }
            } catch (IOException e) {
                this.stop();
            }
        }

        public void stop() {
            try {
                input.close();
                output.close();
                clientSocket.close();
                server.clients.remove(this);
                alive = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BufferedReader getInput() {
            return input;
        }

        public void setInput(BufferedReader input) {
            this.input = input;
        }

        public BufferedWriter getOutput() {
            return output;
        }

        public void setOutput(BufferedWriter output) {
            this.output = output;
        }

        public PacmanServer getServer() {
            return server;
        }

        public void setServer(PacmanServer server) {
            this.server = server;
        }

        public int getId() {
            return id;
        }
    }
}
