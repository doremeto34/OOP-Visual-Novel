package story;

import java.io.Serializable;

public class Scene implements Serializable {
    private static final long serialVersionUID = 1L;

    private String backgroundPath;
    private DialogueEntry[] dialogues; 

    public Scene(String backgroundPath, DialogueEntry[] dialogues) {
        this.backgroundPath = backgroundPath;
        this.dialogues = dialogues;
    }
    
    public String getBackgroundPath() {
    	return backgroundPath;
    }
    
    public DialogueEntry[] getDialogue() {
    	return dialogues;
    }
}
