package test;

import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.GameScreenController;
import main.PlayerStats;
import story.Exam;
import story.MultipleChoiceQuestion;
import story.QuestionBank;
import story.ShortAnswerQuestion;

public class GameScreenTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		final String GAMESCREEN_FXML_FILE_PATH = "/ui/GameScreen.fxml";
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(GAMESCREEN_FXML_FILE_PATH));
	    PlayerStats playerStats = new PlayerStats();
	    playerStats.sceneId = 1;
	    playerStats.dialogueIndex = 0;
	    GameScreenController gsController = new GameScreenController(playerStats);
	    fxmlLoader.setController(gsController);
	    Parent root = fxmlLoader.load();
	    // Sử dụng trực tiếp primaryStage thay vì lấy từ event
	    gsController.setPrimaryStage(primaryStage);
	    gsController.createSaveLoadPanel();
	    primaryStage.setScene(new Scene(root));
	    primaryStage.setTitle("VNFX");
	    primaryStage.show(); 
	    
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
