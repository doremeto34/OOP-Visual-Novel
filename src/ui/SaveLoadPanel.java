package ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.GameScreenController;
import main.PlayerStats;
import story.SceneManager;
import util.FXUtils;
import util.SaveManager;

public class SaveLoadPanel extends StackPane {

    private boolean isSaving;
    private GameScreenController gsController; // You can replace this with your controller reference
    private final int SLOT_COUNT = 6;

    private Pane parentPane;
    private Stage primaryStage;
    
    public SaveLoadPanel(boolean isSaving, GameScreenController gsController, Pane parentPane, Stage primaryStage) {
        this.isSaving = isSaving;
        this.gsController = gsController;
        this.parentPane = parentPane;
        this.primaryStage = primaryStage;
        // Set size and background style
        setPrefSize(1280, 720);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(isSaving ? "Save Game" : "Load Game");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 32));

        GridPane slotsGrid = new GridPane();
        slotsGrid.setHgap(20);
        slotsGrid.setVgap(20);
        slotsGrid.setAlignment(Pos.CENTER);

        for (int i = 1; i <= SLOT_COUNT; i++) {
            VBox slotBox = createSlot(i);
            slotsGrid.add(slotBox, (i - 1) % 3, (i - 1) / 3);
        }

        Button backButton = new Button("◀ Back");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        backButton.setTextFill(Color.WHITE);
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setOnMouseEntered(e -> backButton.setTextFill(Color.GOLD));
        backButton.setOnMouseExited(e -> backButton.setTextFill(Color.WHITE));
        backButton.setOnAction(e -> this.setVisible(false));

        mainBox.getChildren().addAll(titleLabel, slotsGrid, backButton);
        getChildren().add(mainBox);

        setVisible(false); // Start hidden
    }

    private VBox createSlot(int slotNumber) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        String filename = "save" + slotNumber + ".dat";
        File file = new File(filename);

        if (file.exists()) {
        	PlayerStats stats = SaveManager.loadGame(filename);
        	if (stats != null) {
        	    try {
        	    	String sceneId = "scene" + stats.sceneId;
        	    	String bgPath = FXUtils.getBackgroundPathFromSceneFile(sceneId);

        	    	if (bgPath != null) {
        	    	    InputStream imageStream = FXUtils.class.getResourceAsStream(bgPath);
        	    	    if (imageStream == null) {
        	    	        System.err.println("Image not found: " + bgPath);
        	    	        return box; // or null
        	    	    }

        	    	    Image image = new Image(imageStream);
        	    	    ImageView imageView = new ImageView(image);
        	    	    imageView.setFitWidth(200);
        	    	    imageView.setFitHeight(120);
        	    	    imageView.setPreserveRatio(false);
        	    	    imageView.setSmooth(true);

        	    	    Rectangle clip = new Rectangle(200, 120);
        	    	    clip.setArcWidth(20);
        	    	    clip.setArcHeight(20);
        	    	    imageView.setClip(clip);

        	    	    imageView.setOnMouseClicked(e -> slotPressed(slotNumber));

        	    	    Label dateLabel = new Label(getSaveFileDate(filename));
        	    	    dateLabel.setTextFill(Color.WHITE);
        	    	    dateLabel.setFont(Font.font("SansSerif", 12));

        	    	    box.getChildren().addAll(imageView, dateLabel);
        	    	    return box;
        	    	}
        	    } catch (Exception e) {
        	        e.printStackTrace();
        	    }
        	}

        }

        // Empty slot fallback
        Label emptyLabel = new Label("Empty Slot");
        emptyLabel.setTextFill(Color.LIGHTGRAY);
        emptyLabel.setFont(Font.font("SansSerif", FontWeight.BOLD, 16));
        emptyLabel.setAlignment(Pos.CENTER);
        emptyLabel.setPrefSize(200, 120);
        emptyLabel.setStyle("-fx-border-color: gray; -fx-border-width: 2px; -fx-background-color: #333333;");
        emptyLabel.setOnMouseClicked(e -> slotPressed(slotNumber));
        box.getChildren().add(emptyLabel);
        return box;
    }

    private void slotPressed(int slotIndex) {
        String filename = "save" + slotIndex + ".dat";

        if (isSaving) {
            if (gsController != null) {
                SaveManager.saveGame(gsController.getPlayerStats(), filename);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game saved to " + filename + "!", ButtonType.OK);
                alert.showAndWait();
                setVisible(false);
            }
        } else {
            PlayerStats stats = SaveManager.loadGame(filename);
            if (stats != null) {
                stats.dialogueIndex--;

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/GameScreen.fxml"));
                    GameScreenController gameScreenController = new GameScreenController(stats);
                    fxmlLoader.setController(gameScreenController);
                    Parent root = fxmlLoader.load();

                    // Set the stage reference for later use (important!)
                    gameScreenController.setPrimaryStage(primaryStage);
                    gameScreenController.createSaveLoadPanel();

                    primaryStage.setScene(new Scene(root));
                    primaryStage.setTitle("VNFX");
                    primaryStage.show();

                    parentPane.getChildren().remove(this); // Close the save/load panel
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No save file found!", ButtonType.OK);
                alert.showAndWait();
            }
        }

    }

    private String getSaveFileDate(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                long lastModified = file.lastModified();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return sdf.format(new Date(lastModified));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No Data";
    }

    // Add this panel to any parent pane
    public void showIn(Pane parent) {
        if (!parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }
        this.setVisible(true);
        this.toFront();
    }
}
