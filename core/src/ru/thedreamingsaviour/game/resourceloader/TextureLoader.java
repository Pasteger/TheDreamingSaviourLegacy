package ru.thedreamingsaviour.game.resourceloader;

import com.badlogic.gdx.graphics.Texture;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static ru.thedreamingsaviour.game.resourceloader.ShooterCRUD.*;

public class TextureLoader {
    private static Texture nullTexture;
    private static Texture wallTexture;
    private static Texture editWallTexture;
    private static Texture floorTexture;
    private static Texture editFloorTexture;
    private static final Map<String, Texture> shooterIlyaTextures = new HashMap<>();
    private static final Map<String, Texture> shooterShortAttackEnemyTextures = new HashMap<>();
    private static Texture bulletIlyaTexture;
    private static Texture mainMenuBackground;
    private static Texture shooterDeathBackground;

    public static void load() {
        nullTexture = new Texture(getNullTexturePath());
        wallTexture = new Texture(getWallTexturePath());
        editWallTexture = new Texture(getEditableWallTexturePath());
        floorTexture = new Texture(getFloorTexturePath());
        editFloorTexture = new Texture(getEditableFloorTexturePath());
        fillSpritesMap(shooterIlyaTextures, getIlyaSprites());
        fillSpritesMap(shooterShortAttackEnemyTextures, getShortAttackEnemySprites());
        bulletIlyaTexture = new Texture(getBulletIlyaPath());
        mainMenuBackground = new Texture(getMainMenuBackgroundTexturePath());
        shooterDeathBackground = new Texture(getShooterDeathBackgroundTexturePath());
    }

    private static void fillSpritesMap(Map<String, Texture> sprites, JSONObject spriteJSONObject){
        sprites.put("upSprite", new Texture(getSpritePath(spriteJSONObject, "upSprite")));
        sprites.put("downSprite", new Texture(getSpritePath(spriteJSONObject, "downSprite")));
        sprites.put("leftSprite", new Texture(getSpritePath(spriteJSONObject, "leftSprite")));
        sprites.put("rightSprite", new Texture(getSpritePath(spriteJSONObject, "rightSprite")));
    }

    public static Texture getWallTexture() {
        return wallTexture;
    }

    public static Texture getEditWallTexture() {
        return editWallTexture;
    }

    public static Map<String, Texture> getShooterIlyaTextures() {
        return shooterIlyaTextures;
    }

    public static Map<String, Texture> getShooterShortAttackEnemyTextures() {
        return shooterShortAttackEnemyTextures;
    }

    public static Texture getBulletIlyaTexture() {
        return bulletIlyaTexture;
    }

    public static Texture getMainMenuBackground() {
        return mainMenuBackground;
    }

    public static Texture getShooterDeathBackground() {
        return shooterDeathBackground;
    }

    public static Texture getFloorTexture() {
        return floorTexture;
    }
    public static Texture getEditFloorTexture() {
        return editFloorTexture;
    }

    public static Texture getNullTexture() {
        return nullTexture;
    }
}
