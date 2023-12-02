package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.exceptions.MessageReadException;
import ru.kpfu.itis.lobanov.exceptions.ServerException;
import ru.kpfu.itis.lobanov.listener.EventListener;
import ru.kpfu.itis.lobanov.listener.MoveEventListener;
import ru.kpfu.itis.lobanov.listener.SendClientsCountEventListener;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.protocol.MessageProtocol;
import ru.kpfu.itis.lobanov.utils.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PacmanServer implements Server {
    private final int port;
    private ServerSocket serverSocket;
    private final List<Client> clients;
    private final List<EventListener> listeners;

    public PacmanServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            SendClientsCountEventListener listener = new SendClientsCountEventListener();
            MoveEventListener listener1 = new MoveEventListener();
            listener.init(this);
            listener1.init(this);
            listeners.add(listener);
            listeners.add(listener1);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                Client client = new Client(input, output, this, clientSocket);
                clients.add(client);
                AppConfig.usersCount.set(clients.size());

                new Thread(client).start();
            }
        } catch (IOException e) {
            throw new ServerException("Can not establish a connection.", e);
        }
    }

    @Override
    public void sendMessage(int connectionId, Message message) {
        Client client = clients.get(connectionId - 1);
        try {
            client.getOutput().write(MessageProtocol.getBytes(message));
            client.getOutput().flush();
        } catch (IOException e) {
            client.stop();
        }
    }

    @Override
    public void sendBroadCastMessage(Message message) {
        for (Client c : clients) {
            try {
                c.getOutput().write(MessageProtocol.getBytes(message));
                c.getOutput().flush();
            } catch (IOException e) {
                c.stop();
            }
        }
    }

    @Override
    public void sendBroadCastMessage(Message message, Client client) {
        for (Client c : clients) {
            try {
                byte[] currentData = message.getData();
                ByteBuffer buffer = ByteBuffer.allocate(currentData.length + 4);
                buffer.putInt(client.id);
                buffer.put(currentData);
                message.setData(buffer.array());
                c.getOutput().write(MessageProtocol.getBytes(message));
                c.getOutput().flush();
            } catch (IOException e) {
                c.stop();
            }
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    static class Client implements Runnable {
        private InputStream input;
        private OutputStream output;
        private PacmanServer server;
        private final Socket clientSocket;
        private boolean alive = true;
        private final int id;

        public Client(InputStream input, OutputStream output, PacmanServer server, Socket clientSocket) {
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
                    Message message = MessageProtocol.readMessage(input);
                    if (message != null) {
                        for (EventListener listener : server.listeners) {
                            if (message.getType() == listener.getType()) {
                                listener.handle(message, id, server.clients.size());
                            }
                        }
                    }
//                    server.sendBroadCastMessage(message, this);
                }
            } catch (MessageReadException e) {
                throw new RuntimeException(e);
            } catch (EventListenerException e) {
                throw new RuntimeException(e);
            }
        }

        public void stop() {
            try {
                input.close();
                output.close();
                clientSocket.close();
                server.clients.remove(this);
                AppConfig.usersCount.set(server.clients.size());
                alive = false;
            } catch (IOException e) {
                throw new ServerException("Connection to the client is lost.", e);
            }
        }

        public InputStream getInput() {
            return input;
        }

        public OutputStream getOutput() {
            return output;
        }

        public PacmanServer getServer() {
            return server;
        }

        public int getId() {
            return id;
        }
    }
}
