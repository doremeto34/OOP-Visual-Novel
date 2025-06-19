package story;

import java.util.List;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MultipleChoiceQuestion extends Question {
	private final List<String> options;
    private final int correctIndex;
    
    public MultipleChoiceQuestion(String questionText, List<String> options, int correctIndex) {
        super(questionText);
        this.options = options;
        this.correctIndex = correctIndex;
    }
    
    @Override
    public boolean checkAnswer(String userAnswer) {   
        int choice = Integer.parseInt(userAnswer);
        return choice == correctIndex;
    }
    
    @SuppressWarnings("unused")
	@Override
    public void render(VBox box, Exam exam) {
        ToggleGroup group = new ToggleGroup();
        List<String> options = getOptions();  // assuming getOptions() exists

        for (int i = 0; i < options.size(); i++) {
            RadioButton rb = new RadioButton(options.get(i));
            int finalI = i;
            rb.setToggleGroup(group);
            rb.setFont(Font.font("Verdana", 20));
            rb.setOnAction(e -> exam.answerQuestion(this, String.valueOf(finalI)));
            box.getChildren().add(rb);
        }
    }

    
    public List<String> getOptions() {
        return options;
    }
}
