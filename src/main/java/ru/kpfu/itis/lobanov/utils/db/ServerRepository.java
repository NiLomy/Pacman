package ru.kpfu.itis.lobanov.utils.db;

import ru.kpfu.itis.lobanov.model.dao.ServerDao;
import ru.kpfu.itis.lobanov.model.dao.impl.ServerDaoImpl;
import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;

import java.util.List;

public class ServerRepository {
    public static final ServerDao serverDao = new ServerDaoImpl();

    public static ServerModel get(int id) {
        return serverDao.get(id);
    }

    public static ServerModel get(String host, int port) {
        return serverDao.get(host, port);
    }

    public static List<ServerModel> getAll() {
        return serverDao.getAll();
    }

    public static List<ServerModel> getAllFromServer(String host) {
        return serverDao.getAllFromServer(host);
    }

    public static void save(ServerModel serverModel) {
        serverDao.save(serverModel);
    }

    public static void update(ServerModel serverModel, int id) {
        serverDao.update(serverModel, id);
    }

    public static void updateGameStatus(String host, int port, boolean isGameHeld) {
        serverDao.updateGameStatus(host, port, isGameHeld);
    }

    public static void remove(ServerModel serverModel) {
        serverDao.remove(serverModel);
    }

    public static void remove(String host, int port) {
        serverDao.remove(host, port);
    }
}
