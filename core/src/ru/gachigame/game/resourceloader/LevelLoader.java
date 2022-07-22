package ru.gachigame.game.resourceloader;

import org.json.simple.JSONObject;
import ru.gachigame.game.gameobject.Floor;
import ru.gachigame.game.gameobject.Wall;
import ru.gachigame.game.shooter.gameobject.character.Master;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private static JSONObject level;
    private static List<Slave> slaveList;
    private static List<Master> masterList;
    private static List<Wall> wallsList;
    private static List<Floor> floorList;
    private static String nextLevel;

    public static void load(String levelName) throws Exception {
        level = JSONReader.getLevel(levelName);
        wallsList = convertingToWalls();
        masterList = convertingToMasters();
        slaveList = convertingToSlaves();
        floorList = convertingToFloors();
        nextLevel = (String) level.get("nextLevel");
    }

    private static List<Wall> convertingToWalls() {
        List<Wall> wallList = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONWallsList = (List<JSONObject>) level.get("wallsList");
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

    private static List<Floor> convertingToFloors() {
        List<Floor> floorList = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONFloorsList = (List<JSONObject>) level.get("floorList");
            for (JSONObject thisObject : JSONFloorsList) {
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                float width = Float.parseFloat(String.valueOf(thisObject.get("width")));
                float height = Float.parseFloat(String.valueOf(thisObject.get("height")));
                floorList.add(new Floor((int) x, (int) y, (int) width, (int) height));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return floorList;
    }

    public static List<Slave> convertingToSlaves(){
        List<Slave> slaveList = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONSlavesList = (List<JSONObject>) level.get("slaveList");
            for (JSONObject thisObject : JSONSlavesList) {
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                Slave slave = new Slave();
                slave.setX(x);
                slave.setY(y);
                slaveList.add(slave);
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return slaveList;
    }

    public static List<Master> convertingToMasters(){
        List<Master> masterList = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> jsonMastersList = (List<JSONObject>) level.get("masterList");
            for (JSONObject thisObject : jsonMastersList) {
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                Master master = new Master();
                master.setX(x);
                master.setY(y);
                masterList.add(master);
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return masterList;
    }
    public static List<Slave> getSlaveList() {
        return slaveList;
    }

    public static List<Master> getMasterList() {
        return masterList;
    }

    public static List<Wall> getWallsList() {
        return wallsList;
    }

    public static List<Floor> getFloorList() {
        return floorList;
    }
    public static String getNextLevel() {
        return nextLevel;
    }
}
