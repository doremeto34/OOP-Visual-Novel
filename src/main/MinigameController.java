package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import story.Exam;
import story.MultipleChoiceQuestion;
import story.Question;
import story.QuestionBank;
import story.QuestionBankManager;
import story.ShortAnswerQuestion;
import ui.PhonePanel;
import util.FXUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MinigameController {
	
	final GameScreenController gsController;
	final PlayerStats playerStats;
	private String subject;
	private int numberOfQuestion;
	private int examDuration;
	private int returnSceneId;
	private Exam exam;
	private Timeline countdownTimeline;
	private List<Node> questionNodes = new ArrayList<>();
	
	public MinigameController(GameScreenController gsController,PlayerStats playerStats,String subject,int numberOfQuestion,int examDuration,int returnSceneId) {
		this.gsController = gsController;
		this.playerStats = playerStats;
		this.subject = subject;
		this.numberOfQuestion = numberOfQuestion;
		this.examDuration = examDuration;
		this.returnSceneId = returnSceneId;
	}
	@FXML
	private StackPane backgroundPane;
	
	@FXML
    private VBox questionContainer;

    @FXML
    private GridPane questionGrid;

    @FXML
    private ScrollPane questionScroll;

    @FXML
    private Button returnButton;

    @FXML
    private VBox rightPanel;

    @FXML
    private Button submitButton;

    @FXML
    private Label timerLabel;
    
    @FXML
    private Pane timeoutPane;

    @FXML
    private Button phoneButton;
    
    private PhonePanel phonePanel;
    
    @FXML
    void initialize() {
    	createPhonePanel();
        PhonePanel.stylePhoneButton(phoneButton);
        setupUIVisibility();
        initExam();
        renderQuestions();
        setupQuestionNavigation();
        startCountdown(examDuration);
    }
    
    private void setupQuestionNavigation() {
    	questionNodes.clear();
        questionNodes.addAll(questionContainer.getChildren());
        int totalQuestions = exam.getQuestions().size();
        int cols = 5;
        int rows = (int) Math.ceil((double) totalQuestions / cols);
        for (int i = 0; i < totalQuestions; i++) {
            Button btn = new Button(String.valueOf(i + 1));
            btn.setPrefSize(40, 40);
            int index = i;

            btn.setOnAction(e -> {
                Node targetNode = questionNodes.get(index);
                scrollToNodeInVBox(questionContainer, questionScroll, targetNode);
            });

            questionGrid.add(btn, i % cols, i / cols);
        }
    }
    private void setupUIVisibility() {
    	timeoutPane.setVisible(false);
    	returnButton.setVisible(false);
    }
    
    private void initExam() {
    	QuestionBankManager qbManager = QuestionBankManager.getInstance();
    	QuestionBank bank = qbManager.getBank(subject);
        exam = Exam.createExam(bank, numberOfQuestion);
    }
    
    private void renderQuestions() {
        questionContainer.getChildren().clear();
        List<Question> questions = exam.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            Node questionNode = createQuestionNode(q, i);
            questionContainer.getChildren().add(questionNode);
        }
    }

    private Node createQuestionNode(Question q, int index) {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: gray; -fx-border-radius: 5;");
        box.setSpacing(10);

        Label label = new Label("Câu " + (index + 1) + ": " + q.getQuestionText());
        label.setWrapText(true);
        label.setFont(Font.font("Verdana", 20));
        box.getChildren().add(label);

        // Gọi phương thức polymorphic render của từng câu hỏi
        q.render(box, exam);

        return box;
    }
    
    private void scrollToNodeInVBox(VBox container, ScrollPane scrollPane, Node target) {
        Platform.runLater(() -> {
            // Đảm bảo VBox và con của nó đã layout xong
            container.applyCss();
            container.layout();
            // Lấy vị trí của target bên trong container
            Bounds targetBounds = target.getBoundsInParent();
            double targetY = targetBounds.getMinY();
            double containerHeight = container.getHeight();
            double viewportHeight = scrollPane.getViewportBounds().getHeight();
            // Tính tỉ lệ để cuộn đến
            double scrollHeight = containerHeight - viewportHeight;
            double scrollValue = targetY / scrollHeight;
            scrollValue = Math.max(0, Math.min(scrollValue, 1));

            scrollPane.setVvalue(scrollValue);
        });
    }
    
    private void startCountdown(int totalSeconds) {
        IntegerProperty secondsLeft = new SimpleIntegerProperty(totalSeconds);
        final Timeline[] timelineRef = new Timeline[1]; // mánh để giữ timeline

        countdownTimeline  = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                int seconds = secondsLeft.get() - 1;
                secondsLeft.set(seconds);

                int minutes = seconds / 60;
                int secs = seconds % 60;
                timerLabel.setText(String.format("%02d:%02d", minutes, secs));

                if (seconds <= 0) {
                    timelineRef[0].stop(); // dùng biến đã lưu
                    onTimeUp();
                }
            })
        );

        countdownTimeline .setCycleCount(totalSeconds);
        timelineRef[0] = countdownTimeline ; // lưu lại để dùng trong lambda
        countdownTimeline .play();
    }

    private void onTimeUp() {
        timeoutPane.setVisible(true); // hiện pane chặn chỉnh sửa
        timerLabel.setText("Hết giờ!");
    }


    @FXML
    void returnButtonPressed(ActionEvent event) {
        gsController.dialogueController.transitionToScene(returnSceneId);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/GameScreen.fxml"));
            loader.setController(gsController);
            gsController.getPlayerStats().dialogueIndex=0;
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VNFX");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void submitButtonPressed(ActionEvent event) {
    	if (countdownTimeline != null) {
            countdownTimeline.stop();
            timeoutPane.setVisible(true);
        }
        returnButton.setVisible(true);
        timerLabel.setText("Submitted");
        submitButton.setDisable(true);
        
        int score = exam.gradeTest();
        int total = exam.getQuestions().size();

        // 4. In kết quả
        System.out.println("Score: " + score + " / " + total);

    }
    
    @FXML
    void phoneButtonPressed(ActionEvent event) {
    	if (phonePanel != null) {
            phonePanel.showIn(backgroundPane);
        }
    }
    
    public void createPhonePanel() {
        if (phonePanel == null) {
            phonePanel = new PhonePanel(backgroundPane);
            backgroundPane.getChildren().add(phonePanel);
            phonePanel.setVisible(false);
        }
    }
}


