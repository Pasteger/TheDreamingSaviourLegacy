package ru.gachigame.game.resourceloader;

import org.json.simple.JSONObject;
import ru.gachigame.game.resourceloader.JSONReader;
import ru.gachigame.game.shooter.gameobject.character.Master;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShooterCRUD extends JSONReader {
    public static final String SHOOTER_WALLS_PATH;
    public static final String SHOOTER_SLAVES_PATH;
    public static final String SHOOTER_MASTER_PATH;
    public static final String SHOOTER_BACKGROUND_TEXTURE_PATH;
    public static final JSONObject BILLY_SPRITES;
    public static final JSONObject SLAVE_SPRITES;
    public static final JSONObject MASTER_SPRITES;
    public static final JSONObject CUM_SPRITES;
    public static final String SHOOTER_DEATH_BACKGROUND_TEXTURE_PATH;

    static {
        try {
            JSONObject paths = readJson(new File("paths.json"));
            JSONObject shooterResources = (JSONObject) paths.get("shooterResources");

            SHOOTER_BACKGROUND_TEXTURE_PATH = (String) shooterResources.get("dungeon");
            SHOOTER_DEATH_BACKGROUND_TEXTURE_PATH = (String) shooterResources.get("deathBackground");

            SHOOTER_WALLS_PATH = (String) shooterResources.get("wallsPath");
            SHOOTER_SLAVES_PATH = (String) shooterResources.get("slavesPath");
            SHOOTER_MASTER_PATH = (String) shooterResources.get("masterPath");

            BILLY_SPRITES = (JSONObject) shooterResources.get("billySprites");
            SLAVE_SPRITES = (JSONObject) shooterResources.get("slaveSprites");
            MASTER_SPRITES = (JSONObject) shooterResources.get("masterSprites");
            CUM_SPRITES = (JSONObject) shooterResources.get("cumSprites");
        } catch (Exception e) {throw new RuntimeException(e);}
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
            JSONObject jsonObject = JSONReader.readJson(new File(SHOOTER_SLAVES_PATH));
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (Slave slave : slaves){
                JSONObject jsonSlave = new JSONObject();
                jsonSlave.put("x", slave.getX());
                jsonSlave.put("y", slave.getY());
                jsonObjectList.add(jsonSlave);
            }
            jsonObject.put("slaveList", jsonObjectList);
            JSONReader.writeJson(new File(SHOOTER_SLAVES_PATH), jsonObject);
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
            JSONObject jsonObject = JSONReader.readJson(new File(SHOOTER_MASTER_PATH));
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (Master master : masters){
                JSONObject jsonMaster = new JSONObject();
                jsonMaster.put("x", master.getX());
                jsonMaster.put("y", master.getY());
                jsonObjectList.add(jsonMaster);
            }
            jsonObject.put("mastersList", jsonObjectList);
            JSONReader.writeJson(new File(SHOOTER_MASTER_PATH), jsonObject);
            return true;
        }
        catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
}
