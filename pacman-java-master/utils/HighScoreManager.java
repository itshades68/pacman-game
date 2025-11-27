package utils;

import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String HIGHSCORE_FILE = "highscores.txt";
    private List<Integer> highScores;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    private void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    int score = Integer.parseInt(line.trim());
                    highScores.add(score);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid score format: " + line);
                }
            }
            Collections.sort(highScores, Collections.reverseOrder());
            trimToTop3();
        } catch (IOException e) {
            System.err.println("No existing high score file, creating new one.");
        }
    }

    public void saveHighScore(int newScore) {
        highScores.add(newScore);
        Collections.sort(highScores, Collections.reverseOrder());
        trimToTop3();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCORE_FILE))) {
            for (int score : highScores) {
                writer.write(String.valueOf(score));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    private void trimToTop3() {
        if (highScores.size() > 3) {
            highScores = new ArrayList<>(highScores.subList(0, 3));
        }
    }

    public List<Integer> getHighScores() {
        return new ArrayList<>(highScores);
    }
}