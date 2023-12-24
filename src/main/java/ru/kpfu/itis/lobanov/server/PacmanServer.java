package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Bonus;
import ru.kpfu.itis.lobanov.model.entity.environment.pickups.Pellet;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Ghost;
import ru.kpfu.itis.lobanov.model.entity.player.impl.Pacman;

import java.util.List;

public interface PacmanServer extends GameNetServer {
    Maze getMaze();

    void setMaze(Maze maze);

    Pacman getPacman();

    List<Ghost> getGhosts();

    List<Bonus> getBonuses();

    List<Pellet> getPellets();
}
