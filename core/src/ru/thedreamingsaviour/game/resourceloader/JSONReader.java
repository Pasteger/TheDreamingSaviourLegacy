package ru.thedreamingsaviour.game.resourceloader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONReader {
    private static String mainMenuBackgroundTexturePath;
    private static String nullTexturePath;
    private static String bulletIlyaPath;
    private static String wallTexturePath;
    private static String editableWallTexturePath;
    private static String floorTexturePath;
    private static String editableFloorTexturePath;
    private static String sometimesIRipTheSkinSoundPath;

    public static void load() throws Exception {
        JSONObject paths = readJson(new File("paths.json"));
        mainMenuBackgroundTexturePath = (String) paths.get("mainMenuBackground");
        nullTexturePath = (String) paths.get("nullTexture");
        bulletIlyaPath = (String) paths.get("bulletIlya");
        wallTexturePath = (String) paths.get("wallTexture");
        editableWallTexturePath = (String) paths.get("editableWallTexture");
        floorTexturePath = (String) paths.get("floorTexture");
        editableFloorTexturePath = (String) paths.get("editableFloorTexture");
        sometimesIRipTheSkinSoundPath = (String) paths.get("sometimesIRipTheSkinSound");
    }

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
    public static String getSpritePath(JSONObject gameObject, String sprite){
        return (String) gameObject.get(sprite);
    }

    public static String getMainMenuBackgroundTexturePath() {
        return mainMenuBackgroundTexturePath;
    }

    public static String getWallTexturePath() {
        return wallTexturePath;
    }

    public static String getEditableWallTexturePath() {
        return editableWallTexturePath;
    }

    public static String getSometimesIRipTheSkinSoundPath() {
        return sometimesIRipTheSkinSoundPath;
    }

    public static String getFloorTexturePath() {
        return floorTexturePath;
    }

    public static String getEditableFloorTexturePath() {
        return editableFloorTexturePath;
    }

    public static String getBulletIlyaPath() {
        return bulletIlyaPath;
    }

    public static String getNullTexturePath() {
        return nullTexturePath;
    }
}
