package ru.kpfu.itis.lobanov.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageType {
    public static final int MOVEMENT = 1;
    public static final int POWER_UP = 2;
    public static final int EAT_PLAYER = 3;
    public static final int USER_COUNT_INFO_REQUEST = 4;
    public static final int USER_COUNT_INFO_RESPONSE = 5;
    public static final int USER_ID_REQUEST = 6;
    public static final int USER_ID_RESPONSE = 7;
    public static final int CREATE_WALLS_REQUEST = 8;
    public static final int CREATE_WALLS_RESPONSE = 9;
    public static final int CREATE_PACMAN_REQUEST = 10;
    public static final int CREATE_PACMAN_RESPONSE = 11;
    public static final int CREATE_GHOST_REQUEST = 12;
    public static final int CREATE_GHOST_RESPONSE = 13;
    public static final int CREATE_BONUSES_REQUEST = 14;
    public static final int CREATE_BONUSES_RESPONSE = 15;
    public static final int CREATE_PELLETS_REQUEST = 16;
    public static final int CREATE_PELLETS_RESPONSE = 17;

    public static List<Integer> getAllTypes() {
        return Arrays.stream(MessageType.class.getFields()).map(field -> {
            try {
                return field.getInt(MessageType.class.getDeclaredConstructor());
            } catch (IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
//        return Arrays.copyOf(temp, temp.length, Integer[].class);
    }

    private MessageType() {}
}
