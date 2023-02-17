package ru.thedreamingsaviour.game.resourceloader;

import org.json.simple.JSONObject;
import ru.thedreamingsaviour.game.gameobject.entity.Player;

public class SaveLoader {
    public static final Player PLAYER = new Player();

    public static void load(String player) throws Exception {
        JSONObject saves = JSONReader.getSaves();
        JSONObject save = (JSONObject) saves.get(player);
        PLAYER.saveHP = Integer.parseInt(String.valueOf(save.get("HP")));
        PLAYER.heal();
        PLAYER.damage = Integer.parseInt(String.valueOf(save.get("damage")));
        PLAYER.setBulletAim(String.valueOf(save.get("bulletAim")));
        PLAYER.saveSpeed = Integer.parseInt(String.valueOf(save.get("speed")));
        PLAYER.balance = Integer.parseInt(String.valueOf(save.get("balance")));
        PLAYER.currentLevel = String.valueOf(save.get("level"));
        PLAYER.hubLevel = Integer.parseInt(String.valueOf(save.get("hubLevel")));
    }
}
