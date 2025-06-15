package story;

import javafx.scene.layout.VBox;

public abstract class Question {
	private String questionText;
	
	public Question(String questionText) {
		this.questionText = questionText;
	}
	
	public String getQuestionText() {
		return questionText;
	}
	
    public abstract boolean checkAnswer(String userAnswer);
    public abstract void render(VBox box, Exam exam);
}
