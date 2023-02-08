package ru.thedreamingsaviour.game.resourceloader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JSONReader {
    protected static JSONObject readJson(File file) throws Exception {
        FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
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
    public static JSONObject getSaves() throws Exception {
        return readJson(new File("saves.json"));
    }
    public static List<JSONObject> getDialogues(String fileName) throws Exception {
        JSONObject jsonObject = readJson(new File("hub/" + fileName + ".json"));
        return (List<JSONObject>) jsonObject.get("list");
    }
}
