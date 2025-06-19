package util;

import java.io.*;

import main.PlayerStats;

public class TestSaveFile {

    public static void main(String[] args) {
        // Create dummy stats
        PlayerStats stats = new PlayerStats();
        stats.sceneId = 2;
        stats.stat1 = 10;
        stats.stat2 = 20;
        stats.stat3 = 30;
        stats.stat4 = 40;
        stats.stat5 = 50;

        // Save to save1.dat
        SaveManager.saveGame(stats, "save1.dat");

        // Confirm save path
        System.out.println("Save complete at: " + new File("save1.dat").getAbsolutePath());
    }
}
