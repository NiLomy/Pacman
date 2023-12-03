package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.listener.*;

public class ApplicationServer {
    public static void main(String[] args) {
        try {
            PacmanServer pacmanServer = new PacmanServer(5555);
            pacmanServer.registerListener(new SendClientsCountEventListener());
            pacmanServer.registerListener(new MoveEventListener());
            pacmanServer.registerListener(new SendIdEventListener());
            pacmanServer.registerListener(new CreateWallsEventListener());
            pacmanServer.registerListener(new CreatePacmanEventListener());
            pacmanServer.registerListener(new CreateGhostEventListener());
            pacmanServer.registerListener(new CreateBonusesEventListener());
            pacmanServer.registerListener(new CreatePelletsEventListener());
            pacmanServer.start();
        } catch (EventListenerException e) {
            throw new RuntimeException(e);
        }
    }
}
