package ru.kpfu.itis.lobanov.model.entity.net;

/**
 * Instances of this class should only be created by special classes - MessageProviders, for a proper use in net
 */
public class Message {
    protected int type;
    protected byte[] data;

    public Message(int type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
