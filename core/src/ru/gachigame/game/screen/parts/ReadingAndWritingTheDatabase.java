package ru.gachigame.game.screen.parts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class ReadingAndWritingTheDatabase {

    public static StringBuilder readingFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "core/assets/database/table_of_records.txt"));
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
            return text;
        }
        catch (Exception e){
            return new StringBuilder("Error");
        }
    }

    @SuppressWarnings("NewApi")
    public static void writingFile(String text) throws IOException {
        @SuppressWarnings("NewApi") Path file = Paths.get("core/assets/database/table_of_records.txt");
        Files.write(file, Collections.singleton(text), StandardCharsets.UTF_8);
    }
}
