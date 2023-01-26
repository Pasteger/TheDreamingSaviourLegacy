package ru.thedreamingsaviour.game.resourceloader;

import org.json.simple.JSONObject;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.character.Enemy;
import ru.thedreamingsaviour.game.gameobject.character.ShortAttackEnemy;

import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private static JSONObject level;
    private static List<Enemy> enemyList;
    private static List<Surface> surfaceList;
    private static String nextLevel;

    public static void load(String levelName) throws Exception {
        level = JSONReader.getLevel(levelName);
        surfaceList = convertingToSurface();
        nextLevel = (String) level.get("nextLevel");

        List<ShortAttackEnemy> shortAttackEnemyList = convertingToShortAttackEnemy();
        enemyList = new ArrayList<>();
        enemyList.addAll(shortAttackEnemyList);
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

    public static List<Surface> getSurfaceList() {
        return surfaceList;
    }
    public static String getNextLevel() {
        return nextLevel;
    }
    public static List<Enemy> getEnemyList() {
        return enemyList;
    }
}
