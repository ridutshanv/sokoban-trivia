package client;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GameElement {

    private BufferedImage img;
    public GameElement elementOnTop;

    public GameElement(String imageFileName) {
        try {
            img = ImageIO.read(new File(imageFileName));
        } catch (Exception e) {

        }
    }

    BufferedImage getImage() {
        return img;
    }
}