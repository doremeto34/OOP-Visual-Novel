package main;

import story.*;
import ui.CharacterImage;
import ui.ChoicePanel;
import util.FXUtils;

import java.util.List;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DialogueController {
    public final GameScreenController gsController;
    public final PlayerStats playerStats;
    private story.Scene currentScene;
    
    private Timeline typewriterTimeline;
    private int charIndex = 0;
    private String fullText = "";
    boolean stopFlag = false;
    
    public DialogueController(GameScreenController gsController, PlayerStats playerStats) {
        this.gsController = gsController;
        this.playerStats = playerStats;
    }
    
    public void setScene(story.Scene scene) {
        this.currentScene = scene;
    }


    public void showNextDialogue() {
        if (playerStats.dialogueIndex < currentScene.getDialogue().length) {
            DialogueEntry entry = currentScene.getDialogue()[playerStats.dialogueIndex];
            entry.display(this); 
            playerStats.dialogueIndex++;

        } else {
            gsController.getDialogueTextArea().setText("The End.");
            gsController.getClickIndicator().stopBlinking();
        }
    }
    public void updateCharacter(DialogueEntry entry) {
    	String characterPath = entry.getCharacterPath();
        String characterAnimation = entry.getCharacterAnimation();
        System.out.println(characterAnimation);
    	if (characterPath != null) {
        	gsController.removeCharacterImage();
            CharacterImage characterImage = new CharacterImage(characterPath);
            characterImage.setPosition(0, 0); // example position
            if(characterAnimation!=null && characterAnimation.equals("fade"))
            	characterImage.fadeIn(500);
            else
            	characterImage.setOpacity(1);
            gsController.setCharacterImage(characterImage);
            gsController.getBackgroundPane().getChildren().add(0,characterImage.getImageView());
        }
    }
    public void updateSpeaker(TextDialogueEntry entry) {
    	String speaker = entry.getSpeaker();
        if (speaker != null && !speaker.isEmpty()) {
            gsController.getNameLabel().setText(speaker);
            gsController.getNameLabel().setVisible(true);
        } else {
        	gsController.getNameLabel().setVisible(false);
        }
    }
    public void updateText(TextDialogueEntry entry) {
        gsController.getClickIndicator().startBlinking();
        //startTypewriterEffect(entry.getText());
        gsController.getDialogueTextArea().setText(entry.getText());
    }
    public void updateChoicePanel(ChoiceDialogueEntry choiceEntry) {
        gsController.getClickIndicator().stopBlinking();
        showChoicesPanel(choiceEntry.getChoices());
    }
    public void showChoicesPanel(List<Choice> choices) {
        ChoicePanel panel = gsController.getChoicePanel();
        panel.showChoices(choices, () -> {
            // Optional callback when choice is made
        });

        double panelWidth = 440;
        panel.setPrefWidth(panelWidth);

        Pane parent = gsController.getBackgroundPane();

        // Add panel if not already added
        if (!parent.getChildren().contains(panel)) {
            parent.getChildren().add(panel);
        }

        // Defer height calculation and positioning until CSS/layout pass is done
        Platform.runLater(() -> {
            panel.applyCss();
            panel.layout();

            double panelHeight = panel.calculateTotalHeight();
            //DEBUG 
            System.out.println("Calculated panel height: " + panelHeight);
            panel.setPrefSize(panelWidth, panelHeight);

            double x = (parent.getWidth() - panelWidth) / 2;
            double y = (parent.getHeight() - panelHeight) / 2;

            panel.relocate(x, y);
            panel.setVisible(true);
        });
    }

    public void transitionToScene(int sceneId) {
        playerStats.sceneId = sceneId;
        playerStats.dialogueIndex = 0;
        loadScene(sceneId, 0);
    }

    public void loadScene(int sceneId, int dialogueIndex) {
        playerStats.sceneId = sceneId;
        this.currentScene = SceneManager.getScene(sceneId);

        if (currentScene == null) {
            System.out.println("Scene not found: " + sceneId);
            return;
        }

        FXUtils.setBackgroundImage(gsController.getBackgroundPane(), currentScene.getBackgroundPath());
        showNextDialogue();
    }
    
    public void transitionToMinigame(Stage primaryStage, String subject,int numberOfQuestion,int examDuration,int returnSceneId) {
    	try {
    		String MINIGAME_FXML_FILE_PATH = "/ui/Minigame.fxml";
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MINIGAME_FXML_FILE_PATH));

    		MinigameController minigameController = new MinigameController(gsController,playerStats,subject,numberOfQuestion,examDuration,returnSceneId);
    		fxmlLoader.setController(minigameController);
        
    		Parent root = fxmlLoader.load();
        
    		primaryStage.setScene(new Scene(root));
    		primaryStage.setTitle("VNFX");
    		primaryStage.show();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    @SuppressWarnings("unused")
    private void startTypewriterEffect(String text) {
        if (typewriterTimeline != null) {
            typewriterTimeline.stop();
        }

        this.fullText = text;
        this.charIndex = 0;
        gsController.getDialogueTextArea().setText("");

        typewriterTimeline = new Timeline();
        typewriterTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(30), event -> {
            if (charIndex < fullText.length()) {
                String current = gsController.getDialogueTextArea().getText();
                gsController.getDialogueTextArea().setText(current + fullText.charAt(charIndex));
                charIndex++;
            } else {
                typewriterTimeline.stop();
                gsController.getClickIndicator().startBlinking();
            }
        });

        typewriterTimeline.getKeyFrames().add(keyFrame);
        typewriterTimeline.play();
    }
    
}
