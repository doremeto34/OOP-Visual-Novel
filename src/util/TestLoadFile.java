package util;

import java.io.*;

import main.PlayerStats;

public class TestLoadFile {

    public static void main(String[] args) {
        // Try to load the saved stats
        PlayerStats stats = SaveManager.loadGame("save1.dat");

        if (stats != null) {
            System.out.println("Loaded Save:");
            System.out.println("Scene ID: " + stats.sceneId);
            System.out.println("Dialogue Index: " + stats.dialogueIndex);
            System.out.println("Stat1: " + stats.stat1);
            System.out.println("Stat2: " + stats.stat2);
            System.out.println("Stat3: " + stats.stat3);
            System.out.println("Stat4: " + stats.stat4);
            System.out.println("Stat5: " + stats.stat5);
        } else {
            System.out.println("No save file found or failed to load.");
        }
    }
}
