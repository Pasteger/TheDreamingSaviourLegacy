package ru.thedreamingsaviour.game.resourceloader;

import com.badlogic.gdx.math.Rectangle;
import org.json.simple.JSONObject;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.shooter.character.ShortAttackEnemy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelSaver {
    public static void save(List<Surface> surfaceList, List<ShortAttackEnemy> shortAttackEnemyList,
                            String nextLevel, String levelName, String levelType) {
        JSONObject level = new JSONObject();
        level.put("surfaceList", saveSurfaceList(surfaceList));
        level.put("shortAttackEnemyList", saveShortAttackEnemyList(shortAttackEnemyList));
        level.put("nextLevel", nextLevel);
        level.put("type", levelType);
        File levelFile = new File("levels/" + levelName + ".json");
        if (!levelFile.exists()){
            try {
                levelFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

    private static List<JSONObject> saveShortAttackEnemyList(List<ShortAttackEnemy> enemies) {
        List<Rectangle> rectangles = new ArrayList<>(enemies);
        return saveObjectWithoutSize(rectangles);
    }

    private static List<JSONObject> saveObjectWithoutSize(List<Rectangle> rectangles) {
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
