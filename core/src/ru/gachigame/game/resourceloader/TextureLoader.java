package ru.gachigame.game.resourceloader;

import com.badlogic.gdx.graphics.Texture;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static ru.gachigame.game.resourceloader.ShooterCRUD.*;

public class TextureLoader {
    public final static Texture WALL_TEXTURE;
    public final static Texture EDIT_WALL_TEXTURE;
    public final static Map<String, Texture> SHOOTER_SLAVE_TEXTURES = new HashMap<>();
    public final static Map<String, Texture> SHOOTER_BILLY_TEXTURES = new HashMap<>();
    public final static Map<String, Texture> SHOOTER_MASTER_TEXTURES = new HashMap<>();
    public final static Texture CUM_TEXTURE;
    public final static Texture BAD_CUM_TEXTURE;
    public final static Texture MASTER_CUM_TEXTURE;
    public final static Texture MAIN_MENU_BACKGROUND;
    public final static Texture SHOOTER_BACKGROUND;
    public final static Texture SHOOTER_DEATH_BACKGROUND;

    static {
        try {
            WALL_TEXTURE = new Texture(WALL_TEXTURE_PATH);
            EDIT_WALL_TEXTURE = new Texture(EDITABLE_WALL_TEXTURE_PATH);
            fillSpritesMap(SHOOTER_BILLY_TEXTURES, BILLY_SPRITES);
            fillSpritesMap(SHOOTER_SLAVE_TEXTURES, SLAVE_SPRITES);
            fillSpritesMap(SHOOTER_MASTER_TEXTURES, MASTER_SPRITES);
            CUM_TEXTURE = new Texture(getSpritePath(CUM_SPRITES, "cum"));
            BAD_CUM_TEXTURE = new Texture(getSpritePath(CUM_SPRITES, "badCum"));
            MASTER_CUM_TEXTURE = new Texture(getSpritePath(CUM_SPRITES, "masterCum"));
            MAIN_MENU_BACKGROUND = new Texture(MAIN_MENU_BACKGROUND_TEXTURE_PATH);
            SHOOTER_BACKGROUND = new Texture(SHOOTER_BACKGROUND_TEXTURE_PATH);
            SHOOTER_DEATH_BACKGROUND = new Texture(SHOOTER_DEATH_BACKGROUND_TEXTURE_PATH);
        }
        catch (Exception exception){
            exception.printStackTrace();
            throw new RuntimeException();
        }
    }

    private static void fillSpritesMap(Map<String, Texture> sprites, JSONObject spriteJSONObject){
        sprites.put("upSprite", new Texture(getSpritePath(spriteJSONObject, "upSprite")));
        sprites.put("downSprite", new Texture(getSpritePath(spriteJSONObject, "downSprite")));
        sprites.put("leftSprite", new Texture(getSpritePath(spriteJSONObject, "leftSprite")));
        sprites.put("rightSprite", new Texture(getSpritePath(spriteJSONObject, "rightSprite")));
    }
}
