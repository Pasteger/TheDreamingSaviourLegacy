package ru.thedreamingsaviour.game.resourceloader;

import org.json.simple.JSONObject;
import ru.thedreamingsaviour.game.gameobject.*;
import ru.thedreamingsaviour.game.gameobject.entity.Box;
import ru.thedreamingsaviour.game.gameobject.entity.Enemy;
import ru.thedreamingsaviour.game.gameobject.entity.ShortAttackEnemy;
import ru.thedreamingsaviour.game.utility.SwitchHandler;

import java.util.ArrayList;
import java.util.List;

import static ru.thedreamingsaviour.game.utility.SurfaceListSorter.sortSurfaceList;

public class LevelLoader {
    private static JSONObject level;
    private static List<Enemy> enemyList;
    private static List<Surface> surfaceList;
    private static List<Coin> coinList;
    private static List<Box> boxList;
    private static List<DecorObject> decorList;
    private static List<SwitchHandler> switchHandlerList;
    private static float startX;
    private static float startY;
    private static Exit exit;
    private static String nextLevel;

    public static void load(String levelName) throws Exception {
        level = JSONReader.getLevel(levelName);
        surfaceList = convertingToSurface();
        coinList = convertingToCoin();
        boxList = convertingToBox();
        decorList = convertingToDecorObject();
        switchHandlerList = convertingToSwitchHandler();
        exit = readExit();
        startX = Float.parseFloat(String.valueOf(level.get("startX")));
        startY = Float.parseFloat(String.valueOf(level.get("startY")));
        nextLevel = (String) level.get("nextLevel");

        List<ShortAttackEnemy> shortAttackEnemyList = convertingToShortAttackEnemy();
        enemyList = new ArrayList<>();
        enemyList.addAll(shortAttackEnemyList);
    }

    private static List<SwitchHandler> convertingToSwitchHandler() {
        List<SwitchHandler> switchHandlers = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONSwitchHandlerList = (List<JSONObject>) level.get("switchHandlerList");
            for (JSONObject jsonSwitchHandler : JSONSwitchHandlerList) {
                List<Switch> switches = new ArrayList<>();
                List<JSONObject> JSONSwitchList = (List<JSONObject>) jsonSwitchHandler.get("switchList");

                for (JSONObject jsonSwitch : JSONSwitchList) {
                    String texture = String.valueOf(jsonSwitch.get("texture"));
                    float y = Float.parseFloat(String.valueOf(jsonSwitch.get("y")));
                    float x = Float.parseFloat(String.valueOf(jsonSwitch.get("x")));
                    float width = Float.parseFloat(String.valueOf(jsonSwitch.get("width")));
                    float height = Float.parseFloat(String.valueOf(jsonSwitch.get("height")));
                    int speed = Integer.parseInt(String.valueOf(jsonSwitch.get("speed")));
                    boolean active = Boolean.parseBoolean(String.valueOf(jsonSwitch.get("active")));

                    switches.add(new Switch(texture, x, y, width, height, speed, active));
                }
                long surfacesId = Integer.parseInt(String.valueOf(jsonSwitchHandler.get("surfacesId")));

                switchHandlers.add(new SwitchHandler(switches, surfaceList, surfacesId));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return switchHandlers;
    }

    private static Exit readExit() {
        Exit thisExit = null;
        try {
            JSONObject thisObject = (JSONObject) level.get("exit");
            String texture = String.valueOf(thisObject.get("texture"));
            float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
            float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
            float width = Float.parseFloat(String.valueOf(thisObject.get("width")));
            float height = Float.parseFloat(String.valueOf(thisObject.get("height")));
            int speed = Integer.parseInt(String.valueOf(thisObject.get("speed")));

            thisExit = new Exit(texture, x, y, width, height, speed);
        }
        catch (Exception ignored) {
        }
        return thisExit;
    }

    private static List<DecorObject> convertingToDecorObject() {
        List<DecorObject> decorObjects = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONList = (List<JSONObject>) level.get("decorList");
            for (JSONObject thisObject : JSONList) {
                String texture = String.valueOf(thisObject.get("texture"));
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                float width = Float.parseFloat(String.valueOf(thisObject.get("width")));
                float height = Float.parseFloat(String.valueOf(thisObject.get("height")));
                int speed = Integer.parseInt(String.valueOf(thisObject.get("speed")));
                decorObjects.add(new DecorObject(texture, x, y, width, height, speed));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return decorObjects;
    }

    private static List<Box> convertingToBox() {
        List<Box> boxes = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONFloorsList = (List<JSONObject>) level.get("boxList");
            for (JSONObject thisObject : JSONFloorsList) {
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                String material = String.valueOf(thisObject.get("material"));
                byte hp = Byte.parseByte(String.valueOf(thisObject.get("hp")));
                boxes.add(new Box(x, y, 300, 300, material, hp));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return boxes;
    }

    private static List<ShortAttackEnemy> convertingToShortAttackEnemy() {
        List<ShortAttackEnemy> shortAttackEnemyList = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONShortAttackEnemyList = (List<JSONObject>) level.get("shortAttackEnemyList");
            for (JSONObject thisObject : JSONShortAttackEnemyList) {
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                ShortAttackEnemy shortAttackEnemy = new ShortAttackEnemy();
                shortAttackEnemy.setX(x);
                shortAttackEnemy.setY(y);
                shortAttackEnemyList.add(shortAttackEnemy);
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return shortAttackEnemyList;
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
                String textureName = String.valueOf(thisObject.get("standardColor"));
                long id = Long.parseLong(String.valueOf(thisObject.get("id")));
                surfaceList.add(new Surface(x, y, width, height, effect, textureName, id));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return sortSurfaceList(surfaceList);
    }

    private static List<Coin> convertingToCoin() {
        List<Coin> coins = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<JSONObject> JSONFloorsList = (List<JSONObject>) level.get("coinList");
            for (JSONObject thisObject : JSONFloorsList) {
                int value = Integer.parseInt(String.valueOf(thisObject.get("value")));
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                coins.add(new Coin(x, y, value));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return coins;
    }

    public static List<Surface> getSurfaceList() {
        return surfaceList;
    }
    public static String getNextLevel() {
        return nextLevel;
    }
    public static List<Enemy> getEnemyList() {
        return enemyList;
    }

    public static List<Coin> getCoinList() {
        return coinList;
    }

    public static List<Box> getBoxList() {
        return boxList;
    }

    public static List<DecorObject> getDecorList() {
        return decorList;
    }

    public static float getStartX() {
        return startX;
    }

    public static float getStartY() {
        return startY;
    }

    public static Exit getExit() {
        return exit;
    }

    public static List<SwitchHandler> getSwitchHandlerList() {
        return switchHandlerList;
    }
}
