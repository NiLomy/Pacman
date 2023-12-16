package ru.kpfu.itis.lobanov.model.entity.db;

import java.util.Objects;

public class ServerModel {
    private long id;
    private String host;
    private int port;
    private boolean isGameHeld;
    private String gameMap;

    public ServerModel(String host, int port) {
        this.host = host;
        this.port = port;
        this.isGameHeld = false;
    }

    public ServerModel(long id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.isGameHeld = false;
    }

    public ServerModel(long id, String host, int port, boolean isGameHeld) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.isGameHeld = isGameHeld;
    }

    public ServerModel(long id, String host, int port, boolean isGameHeld, String gameMap) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.isGameHeld = isGameHeld;
        this.gameMap = gameMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerModel that = (ServerModel) o;

        if (id != that.id) return false;
        if (port != that.port) return false;
        if (isGameHeld != that.isGameHeld) return false;
        if (!host.equals(that.host)) return false;
        return Objects.equals(gameMap, that.gameMap);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + host.hashCode();
        result = 31 * result + port;
        result = 31 * result + (isGameHeld ? 1 : 0);
        result = 31 * result + (gameMap != null ? gameMap.hashCode() : 0);
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isGameHeld() {
        return isGameHeld;
    }

    public void setGameHeld(boolean gameHeld) {
        isGameHeld = gameHeld;
    }

    public String getGameMap() {
        return gameMap;
    }

    public void setGameMap(String gameMap) {
        this.gameMap = gameMap;
    }
}
