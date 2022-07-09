package ru.gachigame.game.resourceloader;

import org.json.simple.JSONObject;
import ru.gachigame.game.shooter.gameobject.character.Master;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShooterCRUD extends JSONReader {
    private static String shooterWallsPath;
    private static String shooterSlavesPath;
    private static String shooterMasterPath;
    private static String shooterBackgroundTexturePath;
    private static JSONObject billySprites;
    private static JSONObject slaveSprites;
    private static JSONObject masterSprites;
    private static JSONObject cumSprites;
    private static String shooterDeathBackgroundTexturePath;

    public static void load() throws Exception {
        JSONObject paths = readJson(new File("paths.json"));
        JSONObject shooterResources = (JSONObject) paths.get("shooterResources");

        shooterBackgroundTexturePath = (String) shooterResources.get("dungeon");
        shooterDeathBackgroundTexturePath = (String) shooterResources.get("deathBackground");

        shooterWallsPath = (String) shooterResources.get("wallsPath");
        shooterSlavesPath = (String) shooterResources.get("slavesPath");
        shooterMasterPath = (String) shooterResources.get("masterPath");

        billySprites = (JSONObject) shooterResources.get("billySprites");
        slaveSprites = (JSONObject) shooterResources.get("slaveSprites");
        masterSprites = (JSONObject) shooterResources.get("masterSprites");
        cumSprites = (JSONObject) shooterResources.get("cumSprites");
    }

    public static List<Slave> readSlave(String path){
        List<Slave> slaveList = new ArrayList<>();
        try {
            JSONObject jsonObject = readJson(new File(path));
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONSlavesList = (List<JSONObject>) jsonObject.get("slavesList");
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

    public static boolean saveSlaveList(List<Slave> slaves){
        try {
            JSONObject jsonObject = JSONReader.readJson(new File(shooterSlavesPath));
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (Slave slave : slaves){
                JSONObject jsonSlave = new JSONObject();
                jsonSlave.put("x", slave.getX());
                jsonSlave.put("y", slave.getY());
                jsonObjectList.add(jsonSlave);
            }
            jsonObject.put("slaveList", jsonObjectList);
            JSONReader.writeJson(new File(shooterSlavesPath), jsonObject);
            return true;
        }
        catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    public static List<Master> readMaster(String path){
        List<Master> masterList = new ArrayList<>();
        try {
            JSONObject jsonObject = readJson(new File(path));
            @SuppressWarnings("unchecked")
            List<JSONObject> jsonMastersList = (List<JSONObject>) jsonObject.get("mastersList");
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

    public static boolean saveMasterList(List<Master> masters){
        try {
            JSONObject jsonObject = JSONReader.readJson(new File(shooterMasterPath));
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (Master master : masters){
                JSONObject jsonMaster = new JSONObject();
                jsonMaster.put("x", master.getX());
                jsonMaster.put("y", master.getY());
                jsonObjectList.add(jsonMaster);
            }
            jsonObject.put("mastersList", jsonObjectList);
            JSONReader.writeJson(new File(shooterMasterPath), jsonObject);
            return true;
        }
        catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    public static String getShooterWallsPath() {
        return shooterWallsPath;
    }

    public static String getShooterSlavesPath() {
        return shooterSlavesPath;
    }

    public static String getShooterMasterPath() {
        return shooterMasterPath;
    }

    public static String getShooterBackgroundTexturePath() {
        return shooterBackgroundTexturePath;
    }

    public static JSONObject getBillySprites() {
        return billySprites;
    }

    public static JSONObject getSlaveSprites() {
        return slaveSprites;
    }

    public static JSONObject getMasterSprites() {
        return masterSprites;
    }

    public static JSONObject getCumSprites() {
        return cumSprites;
    }

    public static String getShooterDeathBackgroundTexturePath() {
        return shooterDeathBackgroundTexturePath;
    }
}
