package ru.gachigame.game.resourceloader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.gachigame.game.gameobject.Wall;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    private static String mainMenuBackgroundTexturePath;
    private static String wallTexturePath;
    private static String editableWallTexturePath;
    private static String sometimesIRipTheSkinSoundPath;

    public static void load() throws Exception {
        JSONObject paths = readJson(new File("paths.json"));
        mainMenuBackgroundTexturePath = (String) paths.get("mainMenuBackground");
        wallTexturePath = (String) paths.get("wallTexture");
        editableWallTexturePath = (String) paths.get("editableWallTexture");
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

    public static String getSpritePath(JSONObject gameObject, String sprite){
        return (String) gameObject.get(sprite);
    }

    public static List<Wall> readWalls(String path) {
        List<Wall> wallList = new ArrayList<>();
        try {
            JSONObject jsonObject = readJson(new File(path));
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONWallsList = (List<JSONObject>) jsonObject.get("wallsList");
            for (JSONObject thisObject : JSONWallsList) {
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                float width = Float.parseFloat(String.valueOf(thisObject.get("width")));
                float height = Float.parseFloat(String.valueOf(thisObject.get("height")));
                wallList.add(new Wall((int) x, (int) y, (int) width, (int) height));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return wallList;
    }

    public static boolean saveWallList(List<Wall> walls, String path) {
        try {
            JSONObject jsonObject = JSONReader.readJson(new File(path));
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (Wall wall : walls) {
                JSONObject jsonWall = new JSONObject();
                jsonWall.put("x", wall.getX());
                jsonWall.put("y", wall.getY());
                jsonWall.put("width", wall.getWidth());
                jsonWall.put("height", wall.getHeight());
                jsonObjectList.add(jsonWall);
            }
            jsonObject.put("wallsList", jsonObjectList);
            JSONReader.writeJson(new File(path), jsonObject);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
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
}
