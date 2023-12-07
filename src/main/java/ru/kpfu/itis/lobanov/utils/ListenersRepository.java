package ru.kpfu.itis.lobanov.utils;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import ru.kpfu.itis.lobanov.listener.*;
import ru.kpfu.itis.lobanov.listener.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ListenersRepository {
    private static final List<EventListener> listeners = new ArrayList<>();

    public static void init() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Reflections r = new Reflections(
                "ru.kpfu.itis.lobanov.listener",
                new SubTypesScanner(false),
                ClasspathHelper.forClassLoader()
        );
        Set<Class<? extends AbstractEventListener>> classes = r.getSubTypesOf(AbstractEventListener.class);
        for (Class<? extends AbstractEventListener> c : classes) {
            listeners.add(c.newInstance());
        }
    }

    public static EventListener[] getEventListeners() {
        EventListener[] result = new EventListener[listeners.size()];
        int index = 0;
        for (EventListener listener : listeners) {
            result[index++] = listener;
        }
        return result;
    }

    public static EventListener[] get() {
        return new EventListener[] {
                new SendClientsCountEventListener(),
                new MoveEventListener(),
                new CreatePelletsEventListener(),
                new CreateBonusesEventListener(),
                new CreatePacmanEventListener(),
                new SendIdEventListener(),
                new CreateWallsEventListener(),
                new CreateGhostEventListener(),
                new EatPlayerEventListener(),
                new GameLooseEndEventListener()
        };
    }

    private ListenersRepository() {}
}
