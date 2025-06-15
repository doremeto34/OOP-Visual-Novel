package ui;

import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ClickIndicator extends Label {

    private boolean visible = true;
    private Timeline blinkTimeline;

    public ClickIndicator() {
        // Setup the label with default arrow and style
        setText("▶");
        setStyle("-fx-text-fill: #e1c800; -fx-font-size: 32px;");  // Styling the arrow
        setLayoutX(770);
        setLayoutY(120);
        setVisible(false);  // Start with it hidden

        // Setup a blinking effect
        blinkTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> setVisible(visible = !visible))
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);  // Infinite blinking loop
    }

    public void startBlinking() {
        setVisible(true);  // Ensure it's visible
        blinkTimeline.play();  // Start the blinking effect
    }

    public void stopBlinking() {
        blinkTimeline.stop();  // Stop the blinking effect
        setVisible(false);  // Hide the indicator
    }
}
