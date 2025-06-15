package main;

import java.io.Serializable;

public class PlayerStats implements Serializable {
	private static final long serialVersionUID = 1;

    public int sceneId;
    public int dialogueIndex;
    public int stat1;
    public int stat2;
    public int stat3;
    public int stat4;
    public int stat5;

    // Constructor
    public PlayerStats() {
        this.sceneId = 1;
        this.stat1 = 0;
        this.stat2 = 0;
        this.stat3 = 0;
        this.stat4 = 0;
        this.stat5 = 0;
    }
    
    public void copyFrom(PlayerStats other) {
        this.sceneId = other.sceneId;
        this.dialogueIndex = other.dialogueIndex-1;
        this.stat1 = other.stat1;
        this.stat2 = other.stat2;
        // Copy any other player stats you have
    }

}
