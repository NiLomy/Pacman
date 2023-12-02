package ru.kpfu.itis.lobanov.server;

public class ApplicationServer {
    public static void main(String[] args) {
        PacmanServer pacmanServer = new PacmanServer(5555);
        pacmanServer.start();
    }
}
