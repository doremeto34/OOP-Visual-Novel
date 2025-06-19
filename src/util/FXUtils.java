package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class FXUtils {
	
	public static void setBackgroundImage(Pane pane, String imagePath) {
		URL imageUrl = FXUtils.class.getResource(imagePath);
	    if (imageUrl == null) {
	        System.err.println("Image resource not found: " + imagePath);
	        return;
	    }
	    Image image = new Image(imageUrl.toExternalForm(), 1280, 720, false, true);
	    BackgroundImage backgroundImage = new BackgroundImage(
	        image,
	        BackgroundRepeat.NO_REPEAT,
	        BackgroundRepeat.NO_REPEAT,
	        BackgroundPosition.CENTER,
	        new BackgroundSize(1280, 720, false, false, false, false)
	    );

	    // Set the background to the pane
	    pane.setBackground(new Background(backgroundImage));
	}
	
	public static String getBackgroundPathFromSceneFile(String sceneId) {
		String resourcePath = "/scenes/" + sceneId + ".txt";
	    InputStream inputStream = FXUtils.class.getResourceAsStream(resourcePath);

	    if (inputStream == null) {
	        System.err.println("Scene file not found: " + resourcePath);
	        return null;
	    }    
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.readLine(); // first line = background path
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
