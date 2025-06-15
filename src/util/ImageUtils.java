package util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {

    // Cache to store already loaded and scaled images
    private static final Map<String, ImageIcon> cache = new HashMap<>();

    public static ImageIcon loadScaledImage(String path, int width, int height) {
        String key = path + "@" + width + "x" + height; // Unique key

        if (cache.containsKey(key)) {
            return cache.get(key); // Return already loaded
        }
 
        ImageIcon rawIcon = new ImageIcon(path);
        Image scaledImage = rawIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        cache.put(key, scaledIcon); // Save to cache
        return scaledIcon;
    }

    
    public static void clearCache() {
        cache.clear(); // If you ever want to clear memory manually
    }
}
