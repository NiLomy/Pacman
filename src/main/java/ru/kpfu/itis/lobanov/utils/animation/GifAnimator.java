package ru.kpfu.itis.lobanov.utils.animation;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class GifAnimator extends ImageAnimator {
    public GifAnimator(String filename, double durationMs) {
        GifDecoder decoder = new GifDecoder();
        decoder.read(filename);

        Image[] sequence = new Image[decoder.getFrameCount()];
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            BufferedImage bufferedImage = decoder.getFrame(i);
            sequence[i] = SwingFXUtils.toFXImage(bufferedImage, null);
        }
        super.init(sequence, durationMs);
    }
}
