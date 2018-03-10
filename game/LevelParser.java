package game;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LevelParser {
    private String mFilePath;

    public LevelParser(String resourceLocation) {
        mFilePath = resourceLocation;
    }

    public int[][] parseLevelFile() {
        Path path = FileSystems.getDefault().getPath(mFilePath);
        int[][] levelMap = new int[0][0];

        try {
            List<String> levelRows = Files.readAllLines(path);
            levelMap = new int[levelRows.size()][levelRows.get(0).length()];

            for (int i = 0; i < levelRows.size(); i++) {
                String row = levelRows.get(i);

                for (int j = 0; j < row.length(); j++) {
                    String currentCharacter = String.valueOf(row.charAt(j));
                    int value;

                    if (currentCharacter.matches("\\d+")) {
                        value = Integer.parseInt(currentCharacter);
                    } else if (currentCharacter.matches("\\S+")) {
                        value = currentCharacter.charAt(0);
                    } else {
                        value = 0;
                    }
                    levelMap[i][j] = value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return levelMap;
    }
}
