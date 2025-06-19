package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.TitleScreenController;

public class TitleScreenTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		final String TITLESCREEN_FXML_FILE_PATH = "/ui/TitleScreen.fxml";
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TITLESCREEN_FXML_FILE_PATH));
		
		TitleScreenController tsController = new TitleScreenController();
		fxmlLoader.setController(tsController);
		Parent root	= fxmlLoader.load();
	    //CSS root.getStylesheets().add(getClass().getResource("/ui/styles.css").toExternalForm());

		primaryStage.setTitle("VNFX");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method 
		launch(args);
	}

}
