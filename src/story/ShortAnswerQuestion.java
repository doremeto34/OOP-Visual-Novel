package story;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ShortAnswerQuestion extends Question {
    private final Set<String> correctAnswers;
 
    public ShortAnswerQuestion(String questionText, Collection<String> correctAnswers) {
        super(questionText);
        this.correctAnswers = correctAnswers.stream()
            .map(ans -> ans.trim().toLowerCase())
            .collect(Collectors.toSet());
    }

    
    @Override
    public boolean checkAnswer(String userAnswer) {
        String normalizedAnswer = userAnswer.trim().toLowerCase();
        return correctAnswers.contains(normalizedAnswer);
    }
    
    @SuppressWarnings("unused")
	@Override
    public void render(VBox box, Exam exam) {
        TextField answerField = new TextField();
        answerField.setPromptText("Nhập câu trả lời...");
        answerField.setFont(Font.font("Verdana", 20));
        answerField.textProperty().addListener((obs, oldText, newText) -> {
            exam.answerQuestion(this, newText);
        });
        box.getChildren().add(answerField);
    }


    public Set<String> getCorrectAnswers() {
        return correctAnswers;
    }
}
