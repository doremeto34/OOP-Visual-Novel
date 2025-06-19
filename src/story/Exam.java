package story;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exam {
    private List<Question> questions;
    private Map<Question, String> userAnswers = new HashMap<>();

    public Exam(List<Question> questions) {
        this.questions = questions;
    }

    public static Exam createExam(QuestionBank bank, int numQuestions) {
        List<Question> selected = bank.getRandomQuestions(numQuestions);
        return new Exam(selected);
    }

    public void answerQuestion(Question question, String answer) {
        userAnswers.put(question, answer);
    }

    public int gradeTest() {
        int score = 0;
        for (Question q : questions) {
            if (userAnswers.containsKey(q) && q.checkAnswer(userAnswers.get(q))) {
                score++;
            }
        }
        return score;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Map<Question, String> getUserAnswers() {
        return userAnswers;
    }
}


