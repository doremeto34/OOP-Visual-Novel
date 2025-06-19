package util;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageUtils {
    private static final Map<String, Image> cache = new HashMap<>();

    // Load and scale image to specific dimensions
    public static Image loadScaledImage(String path, int width, int height) {
        String key = path + "@" + width + "x" + height;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        Image image = new Image(path, width, height, false, true);
        cache.put(key, image);
        return image;
    }

    public static Image loadImage(String path) {
        String key = path + "@original";
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        Image image = new Image(path, false); // Load with no background loading
        cache.put(key, image);
        return image;
    }

    public static ImageView getImageView(String path, int width, int height) {
        Image image = loadScaledImage(path, width, height);
        return new ImageView(image);
    }
    public static ImageView getImageView(String path) {
        Image image = loadImage(path);
        return new ImageView(image);
    }

    public static void clearCache() {
        cache.clear();
    }
}


