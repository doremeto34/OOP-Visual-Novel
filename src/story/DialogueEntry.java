package story;

import main.DialogueController;

public abstract class DialogueEntry {
	final String characterPath;
	final String characterAnimation;
	
	public DialogueEntry(String characterPath) {
		this.characterPath = characterPath;
		this.characterAnimation = null;
	}
	public DialogueEntry(String characterPath,String characterAnimation) {
		this.characterPath = characterPath;
		this.characterAnimation = characterAnimation;
	}
	public String getCharacterPath() {
		return characterPath;
	};
	public String getCharacterAnimation() {
		return characterAnimation;
	}
	
    public abstract void display(DialogueController controller);
}
