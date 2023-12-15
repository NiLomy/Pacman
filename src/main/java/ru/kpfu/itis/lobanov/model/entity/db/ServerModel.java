package ru.kpfu.itis.lobanov.model.entity.db;

public class ServerModel {
    private long id;
    private String host;
    private int port;
    private boolean isGameHeld;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerModel that = (ServerModel) o;

        if (id != that.id) return false;
        if (port != that.port) return false;
        return host.equals(that.host);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + host.hashCode();
        result = 31 * result + port;
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
}
