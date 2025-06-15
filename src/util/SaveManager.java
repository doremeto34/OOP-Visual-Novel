package util;

import java.io.*;

import main.PlayerStats;

public class SaveManager {

    public static void saveGame(PlayerStats stats, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(stats);
            System.out.println("Game saved successfully to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerStats loadGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            PlayerStats stats = (PlayerStats) ois.readObject();
            System.out.println("Game loaded successfully from " + filename);
            return stats;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No save file found.");
            return null;
        }
    }
}
