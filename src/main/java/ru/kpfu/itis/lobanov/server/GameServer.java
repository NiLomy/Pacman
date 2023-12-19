package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.listener.EventListener;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Ghost;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Pacman;

import java.nio.ByteBuffer;
import java.util.List;

public interface GameServer extends Server {
    void registerListener(EventListener listener);

    Maze getMaze();

    void setMaze(Maze maze);

    ByteBuffer getWallsBuffer();

    Pacman getPacman();

    List<Ghost> getGhosts();

    List<Bonus> getBonuses();

    List<Pellet> getPellets();

    void endGame();
    List<Bonus> generateBonuses();
    List<Pellet> generatePellets();
}
