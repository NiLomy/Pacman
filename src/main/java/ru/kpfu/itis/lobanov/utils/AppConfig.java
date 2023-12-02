package ru.kpfu.itis.lobanov.utils;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

public class AppConfig {
    public static final String CURRENT_HOST = "127.0.0.1";
    public static final int CURRENT_PORT_1 = 5555;
    public static final int CURRENT_PORT_2 = 6666;

    private static String host;
    private static int port;

    public static SimpleIntegerProperty usersCount = new SimpleIntegerProperty(0);
    private static Integer count;

    public static Integer getCount() {
        return count;
    }

    public static void setCount(Integer count) {
        AppConfig.count = count;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        AppConfig.host = host;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        AppConfig.port = port;
    }
}
