package ru.kpfu.itis.lobanov.utils.repository;

import ru.kpfu.itis.lobanov.listener.EventListener;
import ru.kpfu.itis.lobanov.listener.impl.*;

public class ListenersRepository {
    public static EventListener[] getEventListeners() {
        return new EventListener[]{
                new SendClientsCountEventListener(),
                new MoveEventListener(),
                new CreatePelletsEventListener(),
                new CreateBonusesEventListener(),
                new CreatePacmanEventListener(),
                new SendIdEventListener(),
                new CreateWallsEventListener(),
                new CreateGhostEventListener(),
                new EatPlayerEventListener(),
                new RushModeEventListener(),
                new GameEndEventListener(),
                new PacmanDeathEventListener()
        };
    }

    private ListenersRepository() {
    }
}
