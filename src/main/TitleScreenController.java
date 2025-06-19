package main;

import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.SaveLoadPanel;
import util.FXUtils;
import util.ImageUtils;

public class TitleScreenController {
	
	@FXML
	private Pane backgroundPane;
	
    @FXML
    private Button aboutButton;

    @FXML
    private Button continueButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button settingsButton;

    @FXML
    void backgroundPaneMoved(MouseEvent event) {
    	
    }
    
    @FXML
    void aboutButtonEntered(MouseEvent event) {
        aboutButton.setTextFill(javafx.scene.paint.Color.GOLD);
    }

    @FXML
    void aboutButtonExited(MouseEvent event) {
        aboutButton.setTextFill(javafx.scene.paint.Color.WHITE);
    }

    @FXML
    void aboutButtonPressed(ActionEvent event) {

    }

    @FXML
    void continueButtonEntered(MouseEvent event) {
        continueButton.setTextFill(javafx.scene.paint.Color.GOLD);
    }

    @FXML
    void continueButtonExited(MouseEvent event) {
        continueButton.setTextFill(javafx.scene.paint.Color.WHITE);
    }

    @FXML
    void continueButtonPressed(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        SaveLoadPanel loadPanel = new SaveLoadPanel(false, null, backgroundPane, stage); // false = loading
        loadPanel.showIn(backgroundPane);  
    }


    @FXML
    void newGameButtonEntered(MouseEvent event) {
        newGameButton.setTextFill(javafx.scene.paint.Color.GOLD);
    }

    @FXML
    void newGameButtonExited(MouseEvent event) {
        newGameButton.setTextFill(javafx.scene.paint.Color.WHITE);
    }

    @FXML
    void newGameButtonPressed(ActionEvent event) {
        try {
            final String GAMESCREEN_FXML_FILE_PATH = "/ui/GameScreen.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(GAMESCREEN_FXML_FILE_PATH));
            // Create new player stats
            PlayerStats playerStats = PlayerStats.createNewPlayerStats();
            GameScreenController gsController = new GameScreenController(playerStats);
            fxmlLoader.setController(gsController);
            
            Parent root = fxmlLoader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            
            // Set the stage reference in controller BEFORE setting scene
            gsController.setPrimaryStage(stage);
            gsController.createSaveLoadPanel();
            stage.setScene(new Scene(root));
            stage.setTitle("VNFX");
            stage.show(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void quitButtonEntered(MouseEvent event) {
        quitButton.setTextFill(javafx.scene.paint.Color.GOLD);
    }

    @FXML
    void quitButtonExited(MouseEvent event) {
        quitButton.setTextFill(javafx.scene.paint.Color.WHITE);
    }

    @FXML
    void quitButtonPressed(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void settingsButtonEntered(MouseEvent event) {
        settingsButton.setTextFill(javafx.scene.paint.Color.GOLD);
    }

    @FXML
    void settingsButtonExited(MouseEvent event) {
        settingsButton.setTextFill(javafx.scene.paint.Color.WHITE);
    }

    @FXML
    void settingsButtonPressed(ActionEvent event) {

    }
    
    @FXML
    public void initialize() {
    	FXUtils.setBackgroundImage(backgroundPane,"/backgrounds/BK.jpg");
    	
    	ImageView myImage = new ImageView();
        myImage = ImageUtils.getImageView("/backgrounds/logo2.png");
        backgroundPane.getChildren().add(myImage);
        myImage.setLayoutX(600);
    	TranslateTransition translate = new TranslateTransition();
        startBounceAnimation(myImage, 200); // 500 = ground Y position
    }
    
    private void startBounceAnimation(ImageView node, double groundY) {
        final double gravity = 1.0;
        double[] velocity = {0};
        double damping = 0.7;
        double[] posY = {node.getLayoutY()};

        Timeline[] timeline = new Timeline[1]; // Use array to access inside lambda

        timeline[0] = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            velocity[0] += gravity;
            posY[0] += velocity[0];

            if (posY[0] >= groundY) {
                posY[0] = groundY;
                velocity[0] *= -damping;
            }

            if (Math.abs(velocity[0]) < 0.5 && posY[0] == groundY) {
                timeline[0].stop(); // Correctly stop the timeline
            }

            node.setLayoutY(posY[0]);
        }));

        timeline[0].setCycleCount(Animation.INDEFINITE);
        timeline[0].play();
    }

}
