package ru.gachigame.game.resourceloader;

import com.badlogic.gdx.math.Rectangle;
import org.json.simple.JSONObject;
import ru.gachigame.game.gameobject.Floor;
import ru.gachigame.game.gameobject.Wall;
import ru.gachigame.game.shooter.gameobject.character.Master;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelSaver {
    public static void save(List<Wall> wallsList, List<Floor> floorList, List<Slave> slaveList,
                            List<Master> masterList, String nextLevel, String levelName){
        JSONObject level = new JSONObject();
        level.put("wallsList", saveWallList(wallsList));
        level.put("floorList", saveFloorList(floorList));
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
    private static List<JSONObject> saveWallList(List<Wall> walls) {
        List<Rectangle> rectangles = new ArrayList<>(walls);
        return saveObjectWithSize(rectangles);
    }
    private static List<JSONObject> saveFloorList(List<Floor> floors) {
        List<Rectangle> rectangles = new ArrayList<>(floors);
        return saveObjectWithSize(rectangles);
    }
    private static List<JSONObject> saveSlaveList(List<Slave> slaves) {
        List<Rectangle> rectangles = new ArrayList<>(slaves);
        return saveObjectWithSize(rectangles);
    }
    private static List<JSONObject> saveMasterList(List<Master> masters) {
        List<Rectangle> rectangles = new ArrayList<>(masters);
        return saveObjectWithSize(rectangles);
    }
    private static List<JSONObject> saveObjectWithSize(List<Rectangle> rectangles){
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Rectangle rectangle : rectangles) {
            JSONObject json = new JSONObject();
            json.put("x", rectangle.getX());
            json.put("y", rectangle.getY());
            json.put("width", rectangle.getWidth());
            json.put("height", rectangle.getHeight());
            jsonObjectList.add(json);
        }
        return jsonObjectList;
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
