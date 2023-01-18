package ru.thedreamingsaviour.game.resourceloader;

import org.json.simple.JSONObject;
import java.io.File;

public class ShooterCRUD extends JSONReader {
    private static JSONObject ilyaSprites;
    private static JSONObject shortAttackEnemySprites;
    private static String shooterDeathBackgroundTexturePath;

    public static void load() throws Exception {
        JSONObject paths = readJson(new File("paths.json"));
        JSONObject shooterResources = (JSONObject) paths.get("shooterResources");

        shooterDeathBackgroundTexturePath = (String) shooterResources.get("deathBackground");

        ilyaSprites = (JSONObject) shooterResources.get("ilyaSprites");
        shortAttackEnemySprites = (JSONObject) shooterResources.get("shortAttackEnemySprites");
    }

    public static String getShooterDeathBackgroundTexturePath() {
        return shooterDeathBackgroundTexturePath;
    }

    public static JSONObject getIlyaSprites() {
        return ilyaSprites;
    }

    public static JSONObject getShortAttackEnemySprites() {
        return shortAttackEnemySprites;
    }
}
