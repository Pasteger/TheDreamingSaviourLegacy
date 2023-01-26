package ru.thedreamingsaviour.game.resourceloader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONReader {
    protected static JSONObject readJson(File file) throws Exception {
        FileReader reader = new FileReader(file);
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(reader);
    }

    protected static void writeJson(File file, JSONObject jsonObject) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(jsonObject.toString());
        writer.close();
    }

    public static JSONObject getLevel(String level) throws Exception {
        return readJson(new File("levels/" + level + ".json"));
    }
}
