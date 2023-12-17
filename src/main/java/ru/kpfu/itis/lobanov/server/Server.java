package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.listener.EventListener;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.entity.net.Message;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Ghost;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Pacman;

import java.nio.ByteBuffer;
import java.util.List;

public interface Server extends Runnable {
    void registerListener(EventListener listener);

    void sendMessage(int connectionId, Message message);

    void sendBroadCastMessage(Message message);

    Maze getMaze();

    void setMaze(Maze maze);

    ByteBuffer getWallsBuffer();

    Pacman getPacman();

    List<Ghost> getGhosts();

    List<Bonus> getBonuses();

    List<Pellet> getPellets();

    void closeServer();

    void endGame();
}
