package ru.thedreamingsaviour.game.resourceloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;
import java.util.*;

public class TextureLoader {
    private static Texture nullTexture;
    private static Texture mainMenuBackground;
    private static final Map<String, Texture> shooterIlyaTextures = new HashMap<>();
    private static final Map<String, Texture> shooterShortAttackEnemyTextures = new HashMap<>();
    public static final List<Texture> BULLET_ILYA = new ArrayList<>();
    public static final List<Texture> DEATH_BACKGROUND = new ArrayList<>();

    public static void load() {
        nullTexture = new Texture("sprites/nullTexture.png");
        mainMenuBackground = new Texture("sprites/main_menu_background.jpg");
        fillSpritesMap(shooterShortAttackEnemyTextures, "short_attack_enemy");
        fillSpritesMap(shooterIlyaTextures, "ilya");
        readAnimationTextures(DEATH_BACKGROUND, "sprites/death_background/");
        readAnimationTextures(BULLET_ILYA, "sprites/bullet/bullet_ilya/");
    }

    private static void fillSpritesMap(Map<String, Texture> sprites, String path) {
        sprites.put("upSprite", new Texture("sprites/" + path + "/up_sprite.png"));
        sprites.put("downSprite", new Texture("sprites/" + path + "/down_sprite.png"));
        sprites.put("leftSprite", new Texture("sprites/" + path + "/left_sprite.png"));
        sprites.put("rightSprite", new Texture("sprites/" + path + "/right_sprite.png"));
        sprites.put("rightSpriteP", new Texture("sprites/" + path + "/right_sprite_p.png"));
        sprites.put("leftSpriteP", new Texture("sprites/" + path + "/left_sprite_p.png"));
    }

    private static void readAnimationTextures(List<Texture> textures, String path) {
        File directory = new File(Gdx.files.getLocalStoragePath() + "core/assets/" + path);
        int count = Objects.requireNonNull(directory.listFiles()).length;
        for (int i = 1; i <= count; i++) {
            textures.add(new Texture(path + i + ".png"));
        }
    }

    public static Map<String, Texture> getShooterIlyaTextures() {
        return shooterIlyaTextures;
    }

    public static Map<String, Texture> getShooterShortAttackEnemyTextures() {
        return shooterShortAttackEnemyTextures;
    }

    public static Texture getMainMenuBackground() {
        return mainMenuBackground;
    }

    public static Texture getNullTexture() {
        return nullTexture;
    }
}
