package ru.gachigame.game.resourceloader;

import org.json.simple.JSONObject;
import ru.gachigame.game.gameobject.Surface;
import ru.gachigame.game.gameobject.shooter.character.Master;
import ru.gachigame.game.gameobject.shooter.character.Slave;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private static JSONObject level;
    private static List<Slave> slaveList;
    private static List<Master> masterList;
    private static List<Surface> surfaceList;
    private static String levelType;
    private static String nextLevel;

    public static void load(String levelName) throws Exception {
        level = JSONReader.getLevel(levelName);
        masterList = convertingToMasters();
        slaveList = convertingToSlaves();
        surfaceList = convertingToSurface();
        nextLevel = (String) level.get("nextLevel");
        levelType = (String) level.get("type");
    }

    private static List<Surface> convertingToSurface() {
        List<Surface> surfaceList = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONFloorsList = (List<JSONObject>) level.get("surfaceList");
            for (JSONObject thisObject : JSONFloorsList) {
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                float width = Float.parseFloat(String.valueOf(thisObject.get("width")));
                float height = Float.parseFloat(String.valueOf(thisObject.get("height")));
                String effect = String.valueOf(thisObject.get("effect"));
                String textureName = String.valueOf(thisObject.get("textureName"));
                surfaceList.add(new Surface(x, y, width, height, effect, textureName));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return sortSurfaceList(surfaceList);
    }
    private static List<Surface> sortSurfaceList(List<Surface> surfaceList){
        List<Surface> newSurfaceList = new ArrayList<>();
        surfaceList.stream().filter(surface -> surface.getEffect().equals("none")).forEach(newSurfaceList::add);
        surfaceList.stream().filter(surface -> !surface.getEffect().equals("none")).forEach(newSurfaceList::add);
        return newSurfaceList;
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

    public static List<Surface> getSurfaceList() {
        return surfaceList;
    }
    public static String getNextLevel() {
        return nextLevel;
    }

    public static String getLevelType() {
        return levelType;
    }

    public static void setLevelType(String levelType) {
        LevelLoader.levelType = levelType;
    }
}
