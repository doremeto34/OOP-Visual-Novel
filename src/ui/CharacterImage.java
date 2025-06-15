package ui;

import java.net.URL;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class CharacterImage {
    private ImageView imageView;

    public CharacterImage(String imagePath, double width, double height) {
    	URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            imageView = new ImageView(); 
            return;
        }
        imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setOpacity(0); // Start invisible for fade-in effect
    }

    public ImageView getImageView() {
        return imageView;
    }
    
    public void setOpacity(int opacity) {
    	imageView.setOpacity(opacity);
    }

    public void setPosition(double x, double y) {
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    public void fadeIn(double durationMillis) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), imageView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void fadeOut(double durationMillis) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), imageView);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();
    }
}
