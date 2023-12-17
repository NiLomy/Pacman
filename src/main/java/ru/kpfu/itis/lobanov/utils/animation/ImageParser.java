package ru.kpfu.itis.lobanov.utils.animation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageParser {
    public static final String FORMAT_NAME = "png";

    public static void parse(String spriteSheetPath, String subImagePath, int x, int y, int width, int height) {
        try {
            BufferedImage spriteSheet = ImageIO.read(new File(spriteSheetPath));
            BufferedImage image = spriteSheet.getSubimage(x, y, width, height);
            ImageIO.write(image, FORMAT_NAME, new File(subImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
