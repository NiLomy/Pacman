package ru.kpfu.itis.lobanov.server;

import ru.kpfu.itis.lobanov.listener.*;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.ListenersRepository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationServer {
    private static final List<PacmanServer> servers = new ArrayList<>();
    public static void main(String[] args) {
//        System.out.println(InetAddress.getLocalHost().getHostAddress());
        //TODO make a window before launching server to write host and port
//        startServer(AppConfig.CURRENT_PORT_1);
//        startServer(AppConfig.CURRENT_PORT_2);
        startServer(7899);
        System.out.println("EEE");
    }

    public static PacmanServer startServer(int port) {
        PacmanServer pacmanServer = new PacmanServer(port);
        for (EventListener listener : ListenersRepository.getEventListeners()) {
            pacmanServer.registerListener(listener);
        }
        servers.add(pacmanServer);
        new Thread(pacmanServer).start();
        return pacmanServer;
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
