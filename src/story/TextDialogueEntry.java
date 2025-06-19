package story;

import main.DialogueController;

public class TextDialogueEntry extends DialogueEntry {
    private final String speaker;
    private final String text;

    public TextDialogueEntry(String speaker, String text, String characterPath, String characterAnimation) {
        super(characterPath,characterAnimation);
    	this.speaker = speaker;
        this.text = text;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getText() {
        return text;
    }

    @Override
    public void display(DialogueController controller) {
        controller.updateSpeaker(this);
        controller.updateText(this);
    	controller.updateCharacter(this);
    }
}
