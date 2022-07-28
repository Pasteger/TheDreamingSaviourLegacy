package ru.gachigame.game.resourceloader;

import com.badlogic.gdx.math.Rectangle;
import org.json.simple.JSONObject;
import ru.gachigame.game.gameobject.Surface;
import ru.gachigame.game.shooter.gameobject.character.Master;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelSaver {
    public static void save(List<Surface> surfaceList, List<Slave> slaveList,
                            List<Master> masterList, String nextLevel, String levelName){
        JSONObject level = new JSONObject();
        level.put("surfaceList", saveSurfaceList(surfaceList));
        level.put("slaveList", saveSlaveList(slaveList));
        level.put("masterList", saveMasterList(masterList));
        level.put("nextLevel", nextLevel);
        File levelFile = new File("levels/" + levelName + ".json");
        try {
            JSONReader.writeJson(levelFile, level);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static List<JSONObject> saveSurfaceList(List<Surface> surfaces) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Surface surface : surfaces) {
            JSONObject json = new JSONObject();
            json.put("x", surface.getX());
            json.put("y", surface.getY());
            json.put("width", surface.getWidth());
            json.put("height", surface.getHeight());
            json.put("effect", surface.getEffect());
            json.put("textureName", surface.getTextureName());
            jsonObjectList.add(json);
        }
        return jsonObjectList;
    }
    private static List<JSONObject> saveSlaveList(List<Slave> slaves) {
        List<Rectangle> rectangles = new ArrayList<>(slaves);
        return saveObjectWithoutSize(rectangles);
    }
    private static List<JSONObject> saveMasterList(List<Master> masters) {
        List<Rectangle> rectangles = new ArrayList<>(masters);
        return saveObjectWithoutSize(rectangles);
    }

    private static List<JSONObject> saveObjectWithoutSize(List<Rectangle> rectangles){
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Rectangle rectangle : rectangles) {
            JSONObject json = new JSONObject();
            json.put("x", rectangle.getX());
            json.put("y", rectangle.getY());
            jsonObjectList.add(json);
        }
        return jsonObjectList;
    }
}
