package main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ui.SaveLoadPanel;
import util.FXUtils;

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
            PlayerStats playerStats = new PlayerStats();
            playerStats.sceneId = 1;
            playerStats.dialogueIndex = 0;
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
    	FXUtils.setBackgroundImage(backgroundPane,"/backgrounds/zzz2.png");
    	
    	
    }

}
