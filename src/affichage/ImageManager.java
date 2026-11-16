package affichage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private static final Map<String, Image> images = new HashMap<>();

    public static Image getImage(String name) {
        if (!images.containsKey(name)) {
            try {
                InputStream is = ImageManager.class.getResourceAsStream( "images/" + name + ".png");
                if (is != null) {
                    images.put(name, ImageIO.read(is));
                } else {
                    images.put(name, ImageIO.read(new File("images/" + name + ".png")));
                }
            } catch (IOException e) {
                System.err.println("Error loading image: " + name);
                BufferedImage blank = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = blank.createGraphics();
                g.setColor(Color.MAGENTA);
                g.fillRect(0, 0, 50, 50);
                g.dispose();
                images.put(name, blank);
            }
        }
        return images.get(name);
    }
}
