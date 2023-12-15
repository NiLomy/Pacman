package ru.kpfu.itis.lobanov.utils.animation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageParser {
    public static void parse(String spriteSheetPath, String subImagePath, int x, int y, int width, int height) {
        try {
            BufferedImage spriteSheet = ImageIO.read(new File(spriteSheetPath));
            BufferedImage image = spriteSheet.getSubimage(x, y, width, height);
            ImageIO.write(image, "png", new File(subImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        parse(
                "src/main/resources/images/spritesheet.png",
                "src/main/resources/images/pellet2.png",
                506,
                611,
                18,
                18
        );
    }
}
