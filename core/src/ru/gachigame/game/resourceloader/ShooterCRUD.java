package ru.gachigame.game.resourceloader;

import org.json.simple.JSONObject;
import java.io.File;

public class ShooterCRUD extends JSONReader {
    private static JSONObject billySprites;
    private static JSONObject slaveSprites;
    private static JSONObject masterSprites;
    private static JSONObject cumSprites;
    private static String shooterDeathBackgroundTexturePath;

    public static void load() throws Exception {
        JSONObject paths = readJson(new File("paths.json"));
        JSONObject shooterResources = (JSONObject) paths.get("shooterResources");

        shooterDeathBackgroundTexturePath = (String) shooterResources.get("deathBackground");

        billySprites = (JSONObject) shooterResources.get("billySprites");
        slaveSprites = (JSONObject) shooterResources.get("slaveSprites");
        masterSprites = (JSONObject) shooterResources.get("masterSprites");
        cumSprites = (JSONObject) shooterResources.get("cumSprites");
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
