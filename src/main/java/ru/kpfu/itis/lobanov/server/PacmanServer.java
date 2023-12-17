package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.exceptions.MessageReadException;
import ru.kpfu.itis.lobanov.exceptions.MessageWriteException;
import ru.kpfu.itis.lobanov.exceptions.ServerException;
import ru.kpfu.itis.lobanov.listener.EventListener;
import ru.kpfu.itis.lobanov.model.dao.ServerDao;
import ru.kpfu.itis.lobanov.model.dao.impl.ServerDaoImpl;
import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;
import ru.kpfu.itis.lobanov.model.entity.environment.Cell;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Ghost;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Pacman;
import ru.kpfu.itis.lobanov.protocol.MessageProtocol;
import ru.kpfu.itis.lobanov.updater.ScreenUpdater;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.repository.UpdatersRepository;

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
    private final int playersCount;
    private ServerSocket serverSocket;
    private final List<Client> clients;
    private final List<EventListener> listeners;
    private Maze maze = new Maze();
    private boolean isGameStarted;
    private Pacman pacman;
    private final List<Ghost> ghosts;
    private List<Pellet> pellets;
    private List<Bonus> bonuses;
    private ByteBuffer wallsBuffer;
    private final ServerDao serverDao;

    public PacmanServer(int port, int playersCount) {
        this.port = port;
        this.playersCount = playersCount;
        this.clients = new ArrayList<>();
        this.ghosts = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.isGameStarted = false;
        this.serverDao = new ServerDaoImpl();
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeServer));
    }

    @Override
    public void registerListener(EventListener listener) {
        listener.init(this);
        listeners.add(listener);
    }

    @Override
    public void sendMessage(int connectionId, Message message) {
        Client client = clients.get(connectionId);
        try {
            MessageProtocol.writeMessage(client.getOutput(), message);
        } catch (MessageWriteException e) {
            client.stop();
        }
    }

    @Override
    public void sendBroadCastMessage(Message message) {
        for (Client c : clients) {
            try {
                MessageProtocol.writeMessage(c.getOutput(), message);
            } catch (MessageWriteException e) {
                c.stop();
            }
        }
    }

    public String generateWalls() {
        StringBuilder sb = new StringBuilder();
        byte[] b = new byte[maze.getWalls().size() * 2 * GameSettings.INTEGER_BYTES];
        wallsBuffer = ByteBuffer.wrap(b);
        Cell[][] cells = maze.getData();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].isWall()) {
                    wallsBuffer.putInt(i);
                    wallsBuffer.putInt(j);
                    sb.append(1);
                } else sb.append(0);
            }
        }
        return sb.toString();
    }

    public void createPacman() {
        pacman = new Pacman(maze);
    }

    public void createGhosts() {
        for (int i = 0; i < clients.size() - 1; i++) {
            Ghost ghost = new Ghost(maze, GameResources.RED_GHOST_PACKAGE);
            double x = GameSettings.CELL_SIZE + GameSettings.CELL_SIZE * ((GameSettings.MAZE_SIZE - 3) * (i % 2)) + 3;
            double y = GameSettings.CELL_SIZE + GameSettings.CELL_SIZE * ((GameSettings.MAZE_SIZE - 3) * (i / 2)) + 3;
            ghost.setSpawnPoint(x, y);
            ghosts.add(ghost);
        }
    }

    public int getPort() {
        return port;
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

    @Override
    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public ByteBuffer getWallsBuffer() {
        return wallsBuffer;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public List<Pellet> getPellets() {
        return pellets;
    }

    @Override
    public void closeServer() {
        serverDao.remove(AppConfig.CURRENT_HOST, port);
        List<ServerModel> servers = serverDao.getAllFromServer(AppConfig.CURRENT_HOST);
        if (servers.isEmpty()) {
            System.exit(0);
        }
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    @Override
    public void run() {
        try {
            String gameMap = generateWalls();
            serverSocket = new ServerSocket(port);
            serverDao.save(new ServerModel(AppConfig.CURRENT_HOST, port, false, gameMap, playersCount));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                Client client = new Client(input, output, this, clientSocket);
                clients.add(client);

                new Thread(client).start();

                if (!isGameStarted && clients.size() == playersCount) {
                    createPacman();
                    createGhosts();
                    generateBonuses();
                    generatePellets();
                    startGame();
                    isGameStarted = true;
                }
            }
        } catch (IOException e) {
            throw new ServerException(LogMessages.ESTABLISH_CONNECTION_SERVER_EXCEPTION, e);
        }
    }

    private void startGame() {
        for (ScreenUpdater updater : UpdatersRepository.getScreenUpdaters()) {
            updater.init(this);
            new Thread(updater).start();
        }
    }

    public void endGame() {
        for (ScreenUpdater updater : UpdatersRepository.getScreenUpdaters()) {
            updater.setGameAlive(false);
        }
    }

    static class Client implements Runnable {
        private final InputStream input;
        private final OutputStream output;
        private final PacmanServer server;
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
                }
            } catch (MessageReadException e) {
                throw new ServerException(LogMessages.READ_SERVER_EXCEPTION, e);
            } catch (EventListenerException e) {
                throw new ServerException(LogMessages.LISTENER_SERVER_EXCEPTION, e);
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
                throw new ServerException(LogMessages.LOST_CONNECTION_SERVER_EXCEPTIONS, e);
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
