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
    public static final String MAIN_MENU_BACKGROUND_TEXTURE_PATH;
    public static final String WALL_TEXTURE_PATH;
    public static final String EDITABLE_WALL_TEXTURE_PATH;

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

    static {
        try {
            JSONObject paths = readJson(new File("paths.json"));
            MAIN_MENU_BACKGROUND_TEXTURE_PATH = (String) paths.get("mainMenuBackground");
            WALL_TEXTURE_PATH = (String) paths.get("wallTexture");
            EDITABLE_WALL_TEXTURE_PATH = (String) paths.get("editableWallTexture");
        } catch (Exception e) {throw new RuntimeException(e);}
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
}
