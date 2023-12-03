package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.exceptions.MessageReadException;
import ru.kpfu.itis.lobanov.exceptions.ServerException;
import ru.kpfu.itis.lobanov.listener.EventListener;
import ru.kpfu.itis.lobanov.model.environment.Cell;
import ru.kpfu.itis.lobanov.model.environment.Maze;
import ru.kpfu.itis.lobanov.model.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.model.player.Ghost;
import ru.kpfu.itis.lobanov.model.player.Pacman;
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
    private final Maze maze = new Maze();
    private Pacman pacman;
    private Ghost ghost;
    private List<Pellet> pellets;
    private List<Bonus> bonuses;
    private ByteBuffer wallsBuffer;

    public PacmanServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void registerListener(EventListener listener) throws EventListenerException {
        listener.init(this);
        listeners.add(listener);
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            generateWalls();
            createPacman();
            createGhosts();
            generateBonuses();
            generatePellets();

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
        Client client = clients.get(connectionId);
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

    public void generateWalls() {
//        Cell[][] cells = maze.getData();
//        for (int i = 0; i < cells.length; i++) {
//            HBox line = new HBox();
//            line.setAlignment(Pos.CENTER);
//            for (int j = 0; j < cells.length; j++) {
//                if (cells[i][j].isWall()) {
//                    Rectangle rectangle = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//                    line.getChildren().addAll(rectangle);
//                    walls.add(rectangle);
//                } else {
//                    Rectangle rectangle = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//                    rectangle.setFill(Color.WHITE);
//                    line.getChildren().addAll(rectangle);
//                }
//            }
//            gameField.getChildren().addAll(line);
//        }
        byte[] b = new byte[maze.getWalls().size() * 2 * 4];
        wallsBuffer = ByteBuffer.wrap(b);
        Cell[][] cells = maze.getData();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].isWall()) {
                    wallsBuffer.putInt(i);
                    wallsBuffer.putInt(j);
                }
            }
        }
    }

    public void createPacman() {
        pacman = new Pacman(maze);
    }

    public void createGhosts() {
        ghost = new Ghost(maze);
    }

    public void generateBonuses() {
        bonuses = maze.generateBonuses(pacman.getX(), pacman.getY());
    }

    public void generatePellets() {
        pellets = maze.generatePellets(pacman.getX(), pacman.getY(), bonuses);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Maze getMaze() {
        return maze;
    }

    public ByteBuffer getWallsBuffer() {
        return wallsBuffer;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public Ghost getGhost() {
        return ghost;
    }

    public List<Pellet> getPellets() {
        return pellets;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
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
                                break;
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
