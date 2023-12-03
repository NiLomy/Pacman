package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.listener.EventListener;
import ru.kpfu.itis.lobanov.model.environment.Maze;
import ru.kpfu.itis.lobanov.model.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.model.player.Ghost;
import ru.kpfu.itis.lobanov.model.player.Pacman;

import java.nio.ByteBuffer;
import java.util.List;

public interface Server {
    void registerListener(EventListener listener) throws EventListenerException;
    void start();
    void sendMessage(int connectionId, Message message);
    void sendBroadCastMessage(Message message);
    void sendBroadCastMessage(Message message, PacmanServer.Client client);
    Maze getMaze();
    ByteBuffer getWallsBuffer();
    Pacman getPacman();
    Ghost getGhost();
    List<Bonus> getBonuses();
    List<Pellet> getPellets();
}
