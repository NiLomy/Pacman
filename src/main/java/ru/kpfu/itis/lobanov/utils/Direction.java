package ru.kpfu.itis.lobanov.utils;

public enum Direction {
    UP((byte) 1), DOWN((byte) 2), LEFT((byte) 3), RIGHT((byte) 4);

    private final byte positionByte;

    Direction(byte positionByte) {
        this.positionByte = positionByte;
    }

    public byte getPositionByte() {
        return positionByte;
    }
}
