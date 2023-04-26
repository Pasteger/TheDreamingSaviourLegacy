package ru.thedreamingsaviour.game.resourceloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;
import java.util.*;

public class TextureLoader {
    private static final String[] directionals = new String[]
            {"STAND/SIDE", "STAND/TOP", "MOVE/SIDE", "MOVE/TOP", "JUMP", "FALL"};
    private static Texture nullTexture;
    private static Texture mainMenuBackground;
    private static Texture hubBackground;
    private static Texture traderTexture;
    private static Texture bossBarFrame;
    private static Texture bossBarBody;
    public static final Map<String, Texture> KAHAOUSIL_TEXTURES = new HashMap<>();
    public static final Map<String, List<Texture>> PLAYER_TEXTURES = new HashMap<>();
    public static final Map<String, List<Texture>> SHORT_ATTACK_ENEMY = new HashMap<>();
    public static final Map<String, List<Texture>> SHOT_ATTACK_ENEMY = new HashMap<>();
    public static final Map<String, List<Texture>> BOX = new HashMap<>();
    public static final Map<String, List<Texture>> COINS = new HashMap<>();
    public static final Map<String, List<Texture>> DECOR = new HashMap<>();
    public static final Map<String, Map<String, List<Texture>>> SWITCH = new HashMap<>();
    public static final Map<String, Map<String, List<Texture>>> PICK_UP_PACKAGE = new HashMap<>();
    public static final List<Texture> BULLET_ILYA = new ArrayList<>();
    public static final List<Texture> BULLET_SHOT_ATTACK_ENEMY = new ArrayList<>();
    public static final List<Texture> DEATH_BACKGROUND = new ArrayList<>();
    public static final List<Texture> EXPLOSION = new ArrayList<>();

    public static void load() {
        nullTexture = new Texture("sprites/nullTexture.png");
        mainMenuBackground = new Texture("sprites/arts/main_menu_background.jpg");
        hubBackground = new Texture("sprites/arts/hub_background.jpeg");
        traderTexture = new Texture("sprites/trader/trader.png");
        bossBarFrame = new Texture("sprites/boss_bar/frame.png");
        bossBarBody = new Texture("sprites/boss_bar/body.png");

        readAnimationTextures(DEATH_BACKGROUND, "sprites/death_background/");
        readAnimationTextures(BULLET_ILYA, "sprites/bullet/bullet_ilya/");
        readAnimationTextures(BULLET_SHOT_ATTACK_ENEMY, "sprites/bullet/shot_attack_enemy/");
        readAnimationTextures(EXPLOSION, "sprites/explosion/");

        SWITCH.put("lever", new HashMap<>());
        fillAnimatedObjectMap(SWITCH.get("lever"), "sprites/switch/lever/", "able");
        fillAnimatedObjectMap(SWITCH.get("lever"), "sprites/switch/lever/", "able_up");
        fillAnimatedObjectMap(SWITCH.get("lever"), "sprites/switch/lever/", "disable");
        fillAnimatedObjectMap(SWITCH.get("lever"), "sprites/switch/lever/", "disable_up");
        SWITCH.put("button", new HashMap<>());
        fillAnimatedObjectMap(SWITCH.get("button"), "sprites/switch/button/", "able");
        fillAnimatedObjectMap(SWITCH.get("button"), "sprites/switch/button/", "able_up");
        fillAnimatedObjectMap(SWITCH.get("button"), "sprites/switch/button/", "disable");
        fillAnimatedObjectMap(SWITCH.get("button"), "sprites/switch/button/", "disable_up");


        fillAnimatedObjectMap(BOX, "sprites/box/", "WOODEN");
        fillAnimatedObjectMap(BOX, "sprites/box/", "STEEL");
        fillAnimatedObjectMap(BOX, "sprites/box/", "BARREL");

        fillAnimatedObjectMap(COINS, "sprites/coin/", "1");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "10");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "100");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "1000");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "5000");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "1_up");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "10_up");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "100_up");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "1000_up");
        fillAnimatedObjectMap(COINS, "sprites/coin/", "5000_up");

        fillAnimatedObjectMap(DECOR, "sprites/decor/", "STEAM_HAMMER");
        fillAnimatedObjectMap(DECOR, "sprites/decor/", "CISTERN");
        fillAnimatedObjectMap(DECOR, "sprites/decor/", "PIPE");
        fillAnimatedObjectMap(DECOR, "sprites/decor/", "PORTAL");

        PICK_UP_PACKAGE.put("heal", new HashMap<>());
        fillAnimatedObjectMapForCharacter(PICK_UP_PACKAGE.get("heal"), "sprites/pick_up_package/heal/");

        fillAnimatedObjectMapForCharacter(PLAYER_TEXTURES, "sprites/ilya/");
        fillAnimatedObjectMapForCharacter(SHORT_ATTACK_ENEMY, "sprites/short_attack_enemy/");
        fillAnimatedObjectMapForCharacter(SHOT_ATTACK_ENEMY, "sprites/shot_attack_enemy/");

        KAHAOUSIL_TEXTURES.put("skull", new Texture("sprites/boss/kahaousil/skull.png"));
        KAHAOUSIL_TEXTURES.put("jaw", new Texture("sprites/boss/kahaousil/jaw.png"));
        KAHAOUSIL_TEXTURES.put("eyes", new Texture("sprites/boss/kahaousil/eyes.png"));
        KAHAOUSIL_TEXTURES.put("skull_broken", new Texture("sprites/boss/kahaousil/skull_broken.png"));
        KAHAOUSIL_TEXTURES.put("jaw_broken", new Texture("sprites/boss/kahaousil/jaw_broken.png"));
        KAHAOUSIL_TEXTURES.put("eyes_broken_damage", new Texture("sprites/boss/kahaousil/eyes_broken_damage.png"));
        KAHAOUSIL_TEXTURES.put("eyes_damage", new Texture("sprites/boss/kahaousil/eyes_damage.png"));
        KAHAOUSIL_TEXTURES.put("bullet", new Texture("sprites/boss/kahaousil/bullet.png"));
        KAHAOUSIL_TEXTURES.put("duodecagon", new Texture("sprites/boss/kahaousil/duodecagon.png"));
        KAHAOUSIL_TEXTURES.put("duodecagon_light", new Texture("sprites/boss/kahaousil/duodecagon_light.png"));
        KAHAOUSIL_TEXTURES.put("fireball", new Texture("sprites/boss/kahaousil/fireball.png"));
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

    public static Texture getHubBackground() {
        return hubBackground;
    }

    public static Texture getNullTexture() {
        return nullTexture;
    }

    public static Texture getTraderTexture() {
        return traderTexture;
    }

    public static Texture getBossBarFrame() {
        return bossBarFrame;
    }

    public static Texture getBossBarBody() {
        return bossBarBody;
    }
}
