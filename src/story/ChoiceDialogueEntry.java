package story;

import java.util.List;

import main.DialogueController;

public class ChoiceDialogueEntry extends DialogueEntry {
	private final String text;
    private final List<Choice> choices;

    public ChoiceDialogueEntry(String characterPath,String animation,List<Choice> choices) {
    	super(characterPath,animation);
		this.text = null;
        this.choices = choices;
    }

    public String getText() {
    	return text;
    }
    public String getCharacterPath() {
    	return characterPath;
    }
    public List<Choice> getChoices() {
        return choices;
    }
    
    public void display(DialogueController controller) {
    	controller.updateChoicePanel(this);
    	controller.updateCharacter(this);
    }

}
