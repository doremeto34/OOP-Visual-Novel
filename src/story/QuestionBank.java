package story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private final String subject;
    private final List<Question> questions = new ArrayList<>();

    public QuestionBank(String subject) {
        this.subject = subject;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getRandomQuestions(int number) {
        Collections.shuffle(questions);
        return questions.subList(0, Math.min(number, questions.size()));
    }

    public String getSubject() {
        return subject;
    }
}