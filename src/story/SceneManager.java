package story;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import main.DialogueController;
import main.PlayerStats;

import java.util.List;
import java.io.*;
import java.util.*;

public class SceneManager {
    private static Map<Integer, Scene> scenes = new HashMap<>();
    private PlayerStats playerStats;
    private DialogueController dController;

    // Map action keys (from code:) to actual Runnable logic
    private Map<String, Runnable> codeMap = new HashMap<>();

    public SceneManager(PlayerStats playerStats, DialogueController handler) {
        this.dController = handler;
        this.playerStats = playerStats;
        registerCodeActions();
        loadAllScenes();
    }

    private void registerCodeActions() {
        codeMap.put("exploreForest", () -> {
            if (playerStats.stat1 > -1) {
            	dController.transitionToScene(2);
            } else {
                System.out.println("You are too weak to explore!");
            }
        });

        codeMap.put("restHere", () -> {
            playerStats.stat1 += 20;
            dController.transitionToMinigame(dController.gsController.getPrimaryStage(),"math",10,10, 2);
        });

        codeMap.put("thirdChoice", () -> {
            playerStats.stat1 += 20;
            dController.transitionToMinigame(dController.gsController.getPrimaryStage(),"database",10,10, 2);
        });

        codeMap.put("fourthChoice", () -> {
            playerStats.stat1 += 20;
            System.out.println("You regained health by resting.");
        });
        
        codeMap.put("gotoScene1", () -> {
        	dController.transitionToScene(1);
        });
        codeMap.put("gotoScene2", () -> {
        	dController.transitionToScene(2);
        });
        codeMap.put("gotoScene3", () -> {
        	dController.transitionToScene(3);
        });
        codeMap.put("gotoScene4", () -> {
        	dController.transitionToScene(4);
        });
    }

    private BooleanSupplier createConditionFromExpression(String expr) {
        switch (expr) {
            case "stat1>-1":
                return () -> playerStats.stat1 > -1;
            case "stat2>=10":
                return () -> playerStats.stat2 >= 10;
            case "stat1<=50":
                return () -> playerStats.stat1 <= 50;
            default:
                System.err.println("Unknown condition: " + expr);
                return () -> true;
        }
    }
    
    private void loadAllScenes() {
        int maxScenes = 4; 
        for (int i = 1; i <= maxScenes; i++) {
            Scene scene = parseSceneFromResource("/scenes/scene" + i + ".txt");
            if (scene != null) {
                scenes.put(i, scene);
            }
        }
    }

    private Scene parseSceneFromResource(String resourcePath) {
    	try (
        	InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String backgroundPath = br.readLine();
            if (backgroundPath == null) {
                System.err.println("Empty scene file: " + resourcePath);
                return null;
            }

            List<DialogueEntry> entries = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith("D|")) {
                    String[] parts = line.split("\\|", 5);  // D|speaker|text|image|animation
                    String speaker = parts[1];
                    String text = parts.length > 2 ? parts[2].replace("\\\\n", "\n") : null;
                    String image = parts.length > 3 ? "/characters/" + parts[3] : null;
                    String animation = parts.length > 4 ? parts[4] : null;

                    entries.add(new TextDialogueEntry(speaker, text, image, animation));
                } else if (line.startsWith("C|")) {
                    String[] headerParts = line.split("\\|", 3); // max 3 phần tử: "C", "characterPath", "animation?"
                    String characterPath = headerParts[1].trim();
                    String animation = ""; // mặc định không có animation
                    if (headerParts.length == 3) {
                        animation = headerParts[2].trim(); // nếu có animation thì gán
                    }
                    if (!characterPath.isEmpty()) {
                        characterPath = "/characters/" + characterPath;
                    }
                    List<Choice> choices = new ArrayList<>();

                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        if (!line.startsWith("O|")) break;

                        String[] parts = line.split("\\|", 4);
                        String text = parts[1];
                        String cmd = parts[2].trim();
                        Runnable action = createRunnableFromCommand(cmd);

                        BooleanSupplier condition = () -> true;
                        if (parts.length >= 4 && parts[3].startsWith("cond:")) {
                            String expr = parts[3].substring(5).trim();
                            condition = createConditionFromExpression(expr);
                        }

                        choices.add(new Choice(text, action, condition));
                    }

                    entries.add(new ChoiceDialogueEntry(characterPath,animation, choices));

                    if (line == null) break;
                    else continue;
                }
            }

            return new Scene(backgroundPath, entries.toArray(new DialogueEntry[0]));

        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to load scene resource: " + resourcePath);
            e.printStackTrace();
            return null;
        }
    }


    private Runnable createRunnableFromCommand(String cmd) {
        if (cmd.startsWith("code:")) {
            String key = cmd.substring(5);
            Runnable action = codeMap.get(key);
            if (action != null) return action;
            else return () -> System.err.println("Unknown code: " + key);
        } else {
            return () -> System.out.println("No action for command: " + cmd);
        }
    }

    public static Scene getScene(int sceneIndex) {
        return scenes.get(sceneIndex);
    }

}

