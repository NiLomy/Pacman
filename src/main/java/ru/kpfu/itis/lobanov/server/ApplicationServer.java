package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.listener.*;
import ru.kpfu.itis.lobanov.utils.AppConfig;
import ru.kpfu.itis.lobanov.utils.ListenersRepository;

import java.lang.reflect.InvocationTargetException;

public class ApplicationServer {
    public static void main(String[] args) {
        try {
//            ListenersRepository.init();
            startServer(AppConfig.CURRENT_PORT_1);
            startServer(AppConfig.CURRENT_PORT_2);
        } catch (EventListenerException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startServer(int port) throws EventListenerException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PacmanServer pacmanServer = new PacmanServer(port);
        for (EventListener listener : ListenersRepository.get()) {
            pacmanServer.registerListener(listener);
        }
        new Thread(pacmanServer).start();
    }
}
