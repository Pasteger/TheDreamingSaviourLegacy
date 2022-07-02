package ru.gachigame.game;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.gachigame.game.gameobject.characters.Slave;
import ru.gachigame.game.gameobject.Wall;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    public static final String MAIN_MENU_BACKGROUND_TEXTURE_PATH;
    public static final String DEATH_BACKGROUND_TEXTURE_PATH;
    public static final String SHOOTER_WALLS_PATH;
    public static final String SHOOTER_SLAVES_PATH;
    public static final String SHOOTER_BACKGROUND_TEXTURE_PATH;
    public static final JSONObject BILLY_SPRITES;
    public static final JSONObject SLAVE_SPRITES;
    public static final JSONObject MASTER_SPRITES;
    public static final JSONObject CUM_SPRITES;

    private static JSONObject readJson(File file) throws Exception {
        FileReader reader = new FileReader(file);
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(reader);
    }

    static {
        try {
            JSONObject paths = readJson(new File("paths.json"));

            MAIN_MENU_BACKGROUND_TEXTURE_PATH = (String) paths.get("mainMenuBackground");
            JSONObject shooterResources = (JSONObject) paths.get("shooterResources");

            SHOOTER_BACKGROUND_TEXTURE_PATH = (String) shooterResources.get("dungeon");
            DEATH_BACKGROUND_TEXTURE_PATH = (String) shooterResources.get("deathBackground");

            SHOOTER_WALLS_PATH = (String) shooterResources.get("wallsPath");
            SHOOTER_SLAVES_PATH = (String) shooterResources.get("slavesPath");
            BILLY_SPRITES = (JSONObject) shooterResources.get("billySprites");
            SLAVE_SPRITES = (JSONObject) shooterResources.get("slaveSprites");
            MASTER_SPRITES = (JSONObject) shooterResources.get("masterSprites");
            CUM_SPRITES = (JSONObject) shooterResources.get("cumSprites");
        } catch (Exception e) {throw new RuntimeException(e);}
    }

    public static String getSpritePath(JSONObject gameObject, String sprite){
        return (String) gameObject.get(sprite);
    }

    public static List<Wall> readWalls(String path) {
        List<Wall> wallList = new ArrayList<>();
        try {
            JSONObject jsonObject =  readJson(new File(path));
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONWallsList = (List<JSONObject>) jsonObject.get("wallsList");
            for (JSONObject thisObject : JSONWallsList) {
                int x = Integer.parseInt(String.valueOf(thisObject.get("x")));
                int y = Integer.parseInt(String.valueOf(thisObject.get("y")));
                int width = Integer.parseInt(String.valueOf(thisObject.get("width")));
                int height = Integer.parseInt(String.valueOf(thisObject.get("height")));
                wallList.add(new Wall(x, y, width, height));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return wallList;
    }

    public static List<Slave> readShooterSlave(String path){
        List<Slave> slaveList = new ArrayList<>();
        try {
            JSONObject jsonObject =  readJson(new File(path));
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
}
