package ru.thedreamingsaviour.game.resourceloader;

import com.badlogic.gdx.math.Rectangle;
import org.json.simple.JSONObject;
import ru.thedreamingsaviour.game.gameobject.*;
import ru.thedreamingsaviour.game.gameobject.entity.Box;
import ru.thedreamingsaviour.game.gameobject.entity.ShortAttackEnemy;
import ru.thedreamingsaviour.game.gameobject.entity.ShotAttackEnemy;
import ru.thedreamingsaviour.game.utility.SwitchHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelSaver {
    public static void save(List<Surface> surfaceList, List<ShortAttackEnemy> shortAttackEnemyList,
                            List<ShotAttackEnemy> shotAttackEnemyList,
                            List<Coin> coinList, List<Box> boxList, List<DecorObject> decorList,
                            List<SwitchHandler> switchHandlerList, Exit exit, float startX, float startY,
                            String nextLevel, String levelName) {
        JSONObject level = new JSONObject();
        level.put("coinList", saveCoinList(coinList));
        level.put("boxList", saveBoxList(boxList));
        level.put("decorList", saveDecorList(decorList));
        level.put("surfaceList", saveSurfaceList(surfaceList));
        level.put("shortAttackEnemyList", saveShortAttackEnemyList(shortAttackEnemyList));
        level.put("shotAttackEnemyList", saveShotAttackEnemyList(shotAttackEnemyList));
        level.put("switchHandlerList", saveSwitchHandlerList(switchHandlerList));
        level.put("nextLevel", nextLevel);
        level.put("exit", saveExit(exit));
        level.put("startX", startX);
        level.put("startY", startY);
        File levelFile = new File("levels/" + levelName + ".json");
        if (!levelFile.exists()) {
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

    private static List<JSONObject> saveSwitchHandlerList(List<SwitchHandler> switchHandlers) {
        List<JSONObject> jsonSwitchHandlerList = new ArrayList<>();

        for (SwitchHandler switchHandler : switchHandlers) {
            List<JSONObject> jsonSwitchList = new ArrayList<>();
            JSONObject jsonSwitchHandler = new JSONObject();

            for (Switch swch : switchHandler.getSwitches()) {
                JSONObject jsonSwitch = new JSONObject();
                jsonSwitch.put("x", swch.getX());
                jsonSwitch.put("y", swch.getY());
                jsonSwitch.put("width", swch.getWidth());
                jsonSwitch.put("height", swch.getHeight());
                jsonSwitch.put("texture", swch.getTexture());
                jsonSwitch.put("speed", swch.getSpeed());
                jsonSwitch.put("active", swch.isActive());

                jsonSwitchList.add(jsonSwitch);
            }

            jsonSwitchHandler.put("switchList", jsonSwitchList);
            jsonSwitchHandler.put("surfacesId", switchHandler.getSurfacesId());

            jsonSwitchHandlerList.add(jsonSwitchHandler);
        }
        return jsonSwitchHandlerList;
    }

    private static JSONObject saveExit(Exit exit) {
        JSONObject json = new JSONObject();
        try {
            json.put("x", exit.getX());
            json.put("y", exit.getY());
            json.put("width", exit.getWidth());
            json.put("height", exit.getHeight());
            json.put("texture", exit.getTexture());
            json.put("speed", exit.getSpeed());
        } catch (Exception ignored) {
        }
        return json;
    }

    private static List<JSONObject> saveBoxList(List<Box> boxes) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Box box : boxes) {
            JSONObject json = new JSONObject();
            json.put("x", box.getX());
            json.put("y", box.getY());
            json.put("material", box.getMaterial());
            json.put("hp", box.HP);
            jsonObjectList.add(json);
        }
        return jsonObjectList;
    }

    private static List<JSONObject> saveDecorList(List<DecorObject> decorObjectList) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (DecorObject decor : decorObjectList) {
            JSONObject json = new JSONObject();
            json.put("x", decor.getX());
            json.put("y", decor.getY());
            json.put("width", decor.getWidth());
            json.put("height", decor.getHeight());
            json.put("texture", decor.getTexture());
            json.put("speed", decor.getSpeed());
            jsonObjectList.add(json);
        }
        return jsonObjectList;
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
            json.put("standardColor", surface.getStandardColor());
            json.put("id", surface.id);
            jsonObjectList.add(json);
        }
        return jsonObjectList;
    }

    private static List<JSONObject> saveCoinList(List<Coin> coins) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Coin coin : coins) {
            JSONObject json = new JSONObject();
            json.put("x", coin.getX());
            json.put("y", coin.getY());
            json.put("value", coin.value);
            jsonObjectList.add(json);
        }
        return jsonObjectList;
    }

    private static List<JSONObject> saveShortAttackEnemyList(List<ShortAttackEnemy> enemies) {
        List<Rectangle> rectangles = new ArrayList<>(enemies);
        return saveObjectWithoutSize(rectangles);
    }

    private static List<JSONObject> saveShotAttackEnemyList(List<ShotAttackEnemy> enemies) {
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
