package ru.thedreamingsaviour.game.resourceloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;
import java.util.*;

public class TextureLoader {
    private static final String[] directionals = new String[]
            {"NORTH", "SOUTH", "WEST", "EAST", "LEFT", "RIGHT"};
    private static Texture nullTexture;
    private static Texture mainMenuBackground;
    public static final Map<String, List<Texture>> ILYA = new HashMap<>();
    public static final Map<String, List<Texture>> SHORT_ATTACK_ENEMY = new HashMap<>();
    public static final Map<String, List<Texture>> BOX = new HashMap<>();
    public static final List<Texture> BULLET_ILYA = new ArrayList<>();
    public static final Map<String, List<Texture>> COINS = new HashMap<>();
    public static final List<Texture> DEATH_BACKGROUND = new ArrayList<>();

    public static void load() {
        nullTexture = new Texture("sprites/nullTexture.png");
        mainMenuBackground = new Texture("sprites/main_menu_background.jpg");

        readAnimationTextures(DEATH_BACKGROUND, "sprites/death_background/");
        readAnimationTextures(BULLET_ILYA, "sprites/bullet/bullet_ilya/");

        fillAnimatedObjectMap(BOX, "sprites/box/", "WOODEN");
        fillAnimatedObjectMap(BOX, "sprites/box/", "STEEL");

        fillAnimatedObjectMap(COINS, "sprites/coin/", "1");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "10");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "100");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "1000");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "5000");

        fillAnimatedObjectMapForCharacter(ILYA, "sprites/ilya/");
        fillAnimatedObjectMapForCharacter(SHORT_ATTACK_ENEMY, "sprites/short_attack_enemy/");
    }

    private static void fillAnimatedObjectMap(Map<String, List<Texture>> map, String path, String key) {
        List<Texture> textures = new ArrayList<>();
        readAnimationTextures(textures, path + key.toLowerCase() + "/");
        map.put(key, textures);
    }

    private static void fillAnimatedObjectMapForCharacter(Map<String, List<Texture>> map, String path) {
        for (int i = 0; i < 6; i++) {
            List<Texture> textures = new ArrayList<>();
            readAnimationTextures(textures, path + directionals[i].toLowerCase() + "/");
            map.put(directionals[i], textures);
        }
    }

    private static void readAnimationTextures(List<Texture> textures, String path) {
        File directory = new File(Gdx.files.getLocalStoragePath() + "core/assets/" + path);
        int count = Objects.requireNonNull(directory.listFiles()).length;
        for (int i = 1; i <= count; i++) {
            textures.add(new Texture(path + i + ".png"));
        }
    }

    public static Texture getMainMenuBackground() {
        return mainMenuBackground;
    }

    public static Texture getNullTexture() {
        return nullTexture;
    }
}
