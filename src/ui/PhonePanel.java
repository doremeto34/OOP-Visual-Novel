package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhonePanel extends StackPane {

	private TextArea promptArea;
    private TextArea responseArea;
    private Button sendButton;
    private Button backButton;

    public PhonePanel(Pane parentPane) {
        setPrefSize(1280, 720);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        // Outer "phone" shell
        VBox phoneFrame = new VBox();
        phoneFrame.setPrefSize(360, 640);
        phoneFrame.setMaxSize(360, 640);
        phoneFrame.setMinSize(360, 640);
        phoneFrame.setStyle("-fx-background-color: #222; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, black, 15, 0, 0, 0);");
        phoneFrame.setAlignment(Pos.TOP_CENTER);
        phoneFrame.setPadding(new Insets(20));
        phoneFrame.setSpacing(15);

        Label title = new Label("Apple");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        responseArea = new TextArea();
        responseArea.setEditable(false);
        responseArea.setWrapText(true);
        responseArea.setPrefHeight(400);
        responseArea.setStyle("-fx-control-inner-background: #f4f4f4; -fx-font-size: 16px;");

        promptArea = new TextArea();
        promptArea.setPromptText("Ask something...");
        promptArea.setPrefRowCount(3);
        promptArea.setWrapText(true);
        promptArea.setStyle("-fx-control-inner-background: #ffffff; -fx-font-size: 16px;");

        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.CENTER);

        sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        sendButton.setOnAction(e -> sendPrompt());

        backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #888; -fx-text-fill: white;");
        backButton.setOnAction(e -> this.setVisible(false));

        buttonRow.getChildren().addAll(sendButton, backButton);

        phoneFrame.getChildren().addAll(title, responseArea, promptArea, buttonRow);
        getChildren().add(phoneFrame);
        setAlignment(Pos.CENTER);

        setVisible(false); // Start hidden
    }
    
    public static void stylePhoneButton(Button phoneButton) {
        // Use classpath-friendly resource path
        Image icon = new Image(PhonePanel.class.getResource("/buttons/phone.png").toExternalForm());
        ImageView iconView = new ImageView(icon);
        iconView.setFitHeight(80);
        iconView.setPreserveRatio(true);

        phoneButton.setGraphic(iconView);
        phoneButton.setStyle("-fx-background-color: transparent;");
    }
    
    private void sendPrompt() {
        String prompt = promptArea.getText().trim();
        if (prompt.isEmpty()) return;

        responseArea.setText("Thinking...");

        new Thread(() -> {
            try {
                String apiKey = "AIzaSyCBP5j7A7wDcsiOXtS3rx6wr3eWvQZqlqg"; // Replace with your Gemini API key
                URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject textPrompt = new JSONObject();
                textPrompt.put("text", prompt);

                JSONObject requestBody = new JSONObject();
                requestBody.put("contents", new JSONArray()
                    .put(new JSONObject()
                        .put("parts", new JSONArray().put(textPrompt))
                    )
                );

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] inputBytes = requestBody.toString().getBytes("utf-8");
                    os.write(inputBytes, 0, inputBytes.length);
                }

                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                String output = candidates.getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

                Platform.runLater(() -> responseArea.setText(output));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> responseArea.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    public void showIn(Pane parent) {
        if (!parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }
        setVisible(true);
        toFront();
    }
}

