package main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import story.DialogueEntry;
import story.Scene;
import story.SceneManager;
import ui.CharacterImage;
import ui.ChoicePanel;
import ui.ClickIndicator;
import ui.PhonePanel;
import ui.SaveLoadPanel;
import util.FXUtils;

public class GameScreenController {
	
	DialogueController dialogueController;
	private SceneManager sceneManager;
	private PlayerStats playerStats;
	private Scene currentScene;
	
	public GameScreenController(PlayerStats playerStats) {
		this.playerStats = playerStats;
		this.dialogueController = new DialogueController(this, playerStats);
	    this.sceneManager = new SceneManager(playerStats, dialogueController);
	    currentScene = SceneManager.getScene(playerStats.sceneId);
        dialogueController.setScene(currentScene);
	}
	
    @FXML
    private Pane backgroundPane;

    @FXML
    private Pane dialoguePane;

    @FXML
    private Text dialogueText;
    
    @FXML
    private Button homeButton;

    @FXML
    private Button loadButton;

    @FXML
    private Pane nameLabelPane;

    @FXML
    private Text nameLabelText;

    @FXML
    private ButtonBar navigationBar;

    @FXML
    private Button saveButton;
    
    @FXML
    private Button phoneButton;

    @FXML
    void dialoguePaneClicked(MouseEvent event) {
    	if (!choicePanel.isVisible()) {
            dialogueController.showNextDialogue();
        }    
    }

    @FXML
    public void homeButtonPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit to Title");
        alert.setHeaderText("Return to Title Screen");
        alert.setContentText("Are you sure you want to quit to the title screen? Unsaved progress will be lost.");

        // Show dialog and wait for user response
        alert.showAndWait().ifPresent(response -> {
        	if (response == ButtonType.OK) {
        	    try {
        	        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/ui/TitleScreen.fxml"));
        	        TitleScreenController controller = new TitleScreenController(); // create controller instance
        	        fxmlloader.setController(controller); // set controller BEFORE load()
        	        Parent titleRoot = fxmlloader.load();
        	        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        	        stage.setScene(new javafx.scene.Scene(titleRoot));
        	        stage.setTitle("VNFX - Title Screen");
        	        stage.show();
        	    } catch (IOException e) {
        	        e.printStackTrace();
        	    }
        	}
        });
    }

    @FXML
    void phoneButtonPressed(ActionEvent event) {
    	if (phonePanel != null) {
            phonePanel.showIn(backgroundPane);
        }
    }
    
    @FXML
    void loadButtonPressed(ActionEvent event) {
    	if (saveLoadPanel != null) {
            // show panel in load mode
    		saveLoadPanel = new SaveLoadPanel(false, this, backgroundPane, primaryStage);
            backgroundPane.getChildren().add(saveLoadPanel);
            saveLoadPanel.setVisible(true);
        }
    }

    @FXML
    void saveButtonPressed(ActionEvent event) {
    	if (saveLoadPanel != null) {
            // optionally recreate SaveLoadPanel in saving mode or reuse
            saveLoadPanel = new SaveLoadPanel(true, this, backgroundPane, primaryStage);
            backgroundPane.getChildren().add(saveLoadPanel);
            saveLoadPanel.setVisible(true);
        }
    }
    
    // DIY Component
    private ClickIndicator clickIndicator;
    private ChoicePanel choicePanel;
    private SaveLoadPanel saveLoadPanel;
    private Stage primaryStage;
    private CharacterImage characterImage;
    private PhonePanel phonePanel;

    public void setCharacterImage(CharacterImage image) {
    	this.characterImage = image;
	}

	public void removeCharacterImage() {
    	if (characterImage != null) {
        	backgroundPane.getChildren().remove(characterImage.getImageView());
        	characterImage = null;
    	}
	}

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    public Stage getPrimaryStage() {
    	return primaryStage;
    }
    
    public void createSaveLoadPanel() {
        if (saveLoadPanel == null && primaryStage != null) {
            saveLoadPanel = new SaveLoadPanel(false, this, backgroundPane, primaryStage);
            backgroundPane.getChildren().add(saveLoadPanel);
            saveLoadPanel.setVisible(false);
        }
    }
    public void createPhonePanel() {
        if (phonePanel == null) {
            phonePanel = new PhonePanel(backgroundPane);
            backgroundPane.getChildren().add(phonePanel);
            phonePanel.setVisible(false);
        }
    }

    public void startup() {
    	clickIndicator = new ClickIndicator(); 
        dialoguePane.getChildren().add(clickIndicator);
        
        DialogueEntry currentEntry = currentScene.getDialogue()[playerStats.dialogueIndex];
        String charPath = currentEntry.getCharacterPath();

        if (charPath != null && !charPath.isEmpty()) {
            characterImage = new CharacterImage(charPath, 400, 533);
            characterImage.getImageView().setLayoutX(440);  // position like old setBounds
            characterImage.getImageView().setLayoutY(100);

            // Add to backgroundPane or any pane you want
            backgroundPane.getChildren().add(0,characterImage.getImageView());
            characterImage.fadeIn(500);
        }
    	
        choicePanel = new ChoicePanel();
	    currentScene = SceneManager.getScene(playerStats.sceneId);
        FXUtils.setBackgroundImage(backgroundPane, currentScene.getBackgroundPath());
        
        createPhonePanel();
        PhonePanel.stylePhoneButton(phoneButton);
        createSaveLoadPanel();
        dialogueController.showNextDialogue();
        
    }
    @FXML
    public void initialize() {
    	startup();
    }
    
    public CharacterImage getCharacterImage() {
    	return characterImage;
    }
    
    public Pane getBackgroundPane() {
    	return backgroundPane;
    }
    
    public ClickIndicator getClickIndicator() {
    	return clickIndicator;
    }
    
    public ChoicePanel getChoicePanel() {
    	return choicePanel;
    }
    
    public Text getNameLabel() {
    	return nameLabelText;
    }
    
    public Text getDialogueTextArea() {
    	return dialogueText;
    }
    
    public PlayerStats getPlayerStats() {
        return playerStats;
    }
}
