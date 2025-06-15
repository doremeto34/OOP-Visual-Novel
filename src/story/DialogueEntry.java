package story;

import main.DialogueController;

public abstract class DialogueEntry {
	String characterPath;
	String characterAnimation;
	
	public DialogueEntry(String characterPath) {
		this.characterPath = characterPath;
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
