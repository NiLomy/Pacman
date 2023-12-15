package ru.kpfu.itis.lobanov.utils.animation;

import java.awt.image.BufferedImage;

public class GifFrame {
    private BufferedImage image;
    private int delay;

    public GifFrame(BufferedImage im, int del) {
        image = im;
        delay = del;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
