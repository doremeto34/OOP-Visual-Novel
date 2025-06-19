package story;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuestionBankManager {
    private static QuestionBankManager instance;
    private final Map<String, QuestionBank> loadedBanks = new HashMap<>();

    private QuestionBankManager() {}

    public static QuestionBankManager getInstance() {
        if (instance == null) {
            instance = new QuestionBankManager();
        }
        return instance;
    }

    public QuestionBank getBank(String subjectName) {
        if (loadedBanks.containsKey(subjectName)) {
            return loadedBanks.get(subjectName);
        }

        QuestionBank bank = new QuestionBank(subjectName);
        loadQuestionsFromFile(bank, subjectName);
        loadedBanks.put(subjectName, bank);
        return bank;
    }

    private void loadQuestionsFromFile(QuestionBank bank, String subject) {
        String path = "/exam/"+subject+".txt";
        try (
            InputStream inputStream = getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                String type = parts[0].trim();
                String questionText = parts[1].trim();

                if (type.equals("M")) {
                    int correctIndex = Integer.parseInt(parts[2].trim());
                    List<String> choices = new ArrayList<>();
                    for (int i = 3; i < parts.length; i++) {
                        choices.add(parts[i].trim());
                    }
                    bank.addQuestion(new MultipleChoiceQuestion(questionText, choices, correctIndex));

                } else if (type.equals("S")) {
                    Set<String> correctAnswers = new HashSet<>();
                    for (int i = 2; i < parts.length; i++) {
                        correctAnswers.add(parts[i].trim().toLowerCase());
                    }
                    bank.addQuestion(new ShortAnswerQuestion(questionText, correctAnswers));
                } else {
                    System.err.println("Unknown question type: " + type);
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading questions for subject: " + subject);
            e.printStackTrace();
        }
    }
}



