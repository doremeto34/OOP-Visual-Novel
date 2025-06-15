package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import story.Choice;

import java.util.List;

public class ChoicePanel extends VBox {

    public ChoicePanel() {
        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);
        setVisible(false); // Start hidden
        setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-background-radius: 20;");
    }

    public void showChoices(List<Choice> choices, Runnable onChoiceMade) {
        getChildren().clear();
        setVisible(true);

        for (Choice choice : choices) {
            if (choice.condition.getAsBoolean()) {
                Button btn = new Button(choice.text);
                btn.setWrapText(true);
                btn.setMaxWidth(400);
                btn.setPrefWidth(400);
                btn.setPrefHeight(Region.USE_COMPUTED_SIZE);
                btn.setMinHeight(Region.USE_PREF_SIZE);
                btn.setMaxHeight(Region.USE_COMPUTED_SIZE);
                styleChoiceButton(btn);

                btn.setOnAction(e -> {
                    setVisible(false);
                    choice.action.run();
                    if (onChoiceMade != null) onChoiceMade.run();
                });

                getChildren().add(btn);
            }
        }

    }

    private void styleChoiceButton(Button button) {
        button.setStyle("""
            -fx-background-color: rgba(0,0,0,0.8);
            -fx-border-color: white;
            -fx-border-width: 2;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-text-fill: white;
            -fx-font-family: "Verdana";
            -fx-font-size: 18px;
            -fx-padding: 10 20 10 20;
        """);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.WHITE);
        shadow.setRadius(3);
        button.setEffect(shadow);

        button.setOnMouseEntered(e -> button.setStyle(button.getStyle().replace("rgba(0,0,0,0.8)", "rgba(80,80,80,0.9)")));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("rgba(80,80,80,0.9)", "rgba(0,0,0,0.8)")));
    }
    

    public double calculateTotalHeight() {
    	
        double totalHeight = getPadding().getTop() + getPadding().getBottom();
        double spacing = getSpacing();

        int numButtons = getChildren().size();
        for (Node node : getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                // Ensure CSS is applied so height is accurate
                btn.applyCss();
                btn.layout();
                totalHeight += btn.getHeight();
            }
        }

        // Add spacing between buttons (numButtons - 1) * spacing, if more than 1 button
        if (numButtons > 1) {
            totalHeight += spacing * (numButtons - 1);
        }

        return totalHeight;
    }

}

