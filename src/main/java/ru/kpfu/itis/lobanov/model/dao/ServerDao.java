package ru.kpfu.itis.lobanov.model.dao;

import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;

public interface ServerDao extends Dao<ServerModel> {
    void remove(String host, int port);
}
