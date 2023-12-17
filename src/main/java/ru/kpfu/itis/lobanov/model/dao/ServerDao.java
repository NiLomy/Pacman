package ru.kpfu.itis.lobanov.model.dao;

import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;

import java.util.List;

public interface ServerDao extends Dao<ServerModel> {
    ServerModel get(String host, int port);

    List<ServerModel> getAllFromServer(String host);

    void updateGameStatus(String host, int port, boolean isGameHeld);

    void remove(String host, int port);
}
