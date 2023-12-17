package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.listener.*;
import ru.kpfu.itis.lobanov.utils.repository.ListenersRepository;

import java.util.ArrayList;
import java.util.List;

public class ApplicationServer {
    private static final List<PacmanServer> servers = new ArrayList<>();
    public static void main(String[] args) {
//        System.out.println(InetAddress.getLocalHost().getHostAddress());
        startServer(createServer(7899, 2));
        System.out.println("EEE");
    }

    public static PacmanServer createServer(int port, int playersCount) {
        PacmanServer pacmanServer = new PacmanServer(port, playersCount);
        for (EventListener listener : ListenersRepository.getEventListeners()) {
            pacmanServer.registerListener(listener);
        }
        servers.add(pacmanServer);
        return pacmanServer;
    }

    public static void startServer(PacmanServer pacmanServer) {
        new Thread(pacmanServer).start();
    }

    public static void closeServer(int port) {
        for (int i = 0; i < servers.size(); i++) {
            PacmanServer server = servers.get(i);
            if (server.getPort() == port) {
                servers.remove(server);
                server.closeServer();
                break;
            }
        }
        /*
        * socket.isConnected in thread or catch IOE or ping-pong in 1 sec and make timer if not received - disconnect
        *
        * */
    }
}
