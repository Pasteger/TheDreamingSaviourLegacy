package ru.gachigame.game.resourceloader;

import com.badlogic.gdx.graphics.Texture;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static ru.gachigame.game.resourceloader.ShooterCRUD.*;

public class TextureLoader {
    private static Texture wallTexture;
    private static Texture editWallTexture;
    private static Texture floorTexture;
    private static Texture editFloorTexture;
    private static final Map<String, Texture> shooterSlaveTextures = new HashMap<>();
    private static final Map<String, Texture> shooterBullyTextures = new HashMap<>();
    private static final Map<String, Texture> shooterMasterTextures = new HashMap<>();
    private static Texture cumTexture;
    private static Texture badCumTexture;
    private static Texture masterCumTexture;
    private static Texture mainMenuBackground;
    private static Texture shooterBackground;
    private static Texture shooterDeathBackground;

    public static void load() {
        wallTexture = new Texture(getWallTexturePath());
        editWallTexture = new Texture(getEditableWallTexturePath());
        floorTexture = new Texture(getFloorTexturePath());
        editFloorTexture = new Texture(getEditableFloorTexturePath());
        fillSpritesMap(shooterBullyTextures, getBillySprites());
        fillSpritesMap(shooterSlaveTextures, getSlaveSprites());
        fillSpritesMap(shooterMasterTextures, getMasterSprites());
        cumTexture = new Texture(getSpritePath(getCumSprites(), "cum"));
        badCumTexture = new Texture(getSpritePath(getCumSprites(), "badCum"));
        masterCumTexture = new Texture(getSpritePath(getCumSprites(), "masterCum"));
        mainMenuBackground = new Texture(getMainMenuBackgroundTexturePath());
        shooterBackground = new Texture(getShooterBackgroundTexturePath());
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

    public static Map<String, Texture> getShooterSlaveTextures() {
        return shooterSlaveTextures;
    }

    public static Map<String, Texture> getShooterBullyTextures() {
        return shooterBullyTextures;
    }

    public static Map<String, Texture> getShooterMasterTextures() {
        return shooterMasterTextures;
    }

    public static Texture getCumTexture() {
        return cumTexture;
    }

    public static Texture getBadCumTexture() {
        return badCumTexture;
    }

    public static Texture getMasterCumTexture() {
        return masterCumTexture;
    }

    public static Texture getMainMenuBackground() {
        return mainMenuBackground;
    }

    public static Texture getShooterBackground() {
        return shooterBackground;
    }

    public static Texture getShooterDeathBackground() {
        return shooterDeathBackground;
    }

    public static Texture getFloorTexture() {
        return floorTexture;
    }

    public static void setFloorTexture(Texture floorTexture) {
        TextureLoader.floorTexture = floorTexture;
    }

    public static Texture getEditFloorTexture() {
        return editFloorTexture;
    }

    public static void setEditFloorTexture(Texture editFloorTexture) {
        TextureLoader.editFloorTexture = editFloorTexture;
    }
}
