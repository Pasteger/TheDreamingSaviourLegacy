package ru.thedreamingsaviour.game.resourceloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.io.File;
import java.util.*;

public class SoundLoader {
    public static final Map<String, Sound> KAHAOUSIL_SOUNDS = new HashMap<>();
    public static final Map<String, List<Sound>> DAMAGE = new HashMap<>();
    public static final Map<String, List<Sound>> DEATH = new HashMap<>();
    private static Sound shotPlayer;
    private static Sound shotShotAttackEnemy;
    private static Sound agrShortAttackEnemy;
    private static Sound agrShotAttackEnemy;
    private static Sound pickCoin;
    private static Sound switchToggle;
    private static Sound pickUpHeal;

    public static void load() {
        fillMap(DAMAGE, "sounds/damage/", "BOX/WOODEN");
        fillMap(DAMAGE, "sounds/damage/", "BOX/STEEL");
        fillMap(DAMAGE, "sounds/damage/", "BOX/BARREL");
        fillMap(DAMAGE, "sounds/damage/", "PLAYER");
        fillMap(DAMAGE, "sounds/damage/", "ENEMY");

        fillMap(DEATH, "sounds/death/", "BOX/WOODEN");
        fillMap(DEATH, "sounds/death/", "BOX/STEEL");
        fillMap(DEATH, "sounds/death/", "BOX/BARREL");
        fillMap(DEATH, "sounds/death/", "ENEMY/SHORT_ATTACK_ENEMY");
        fillMap(DEATH, "sounds/death/", "ENEMY/SHOT_ATTACK_ENEMY");

        KAHAOUSIL_SOUNDS.put("shot", Gdx.audio.newSound(Gdx.files.internal("sounds/boss/kahaousil/shot.wav")));
        KAHAOUSIL_SOUNDS.put("damage", Gdx.audio.newSound(Gdx.files.internal("sounds/boss/kahaousil/damage.wav")));
        KAHAOUSIL_SOUNDS.put("explosions", Gdx.audio.newSound(Gdx.files.internal("sounds/boss/kahaousil/explosions.wav")));

        shotPlayer = Gdx.audio.newSound(Gdx.files.internal("sounds/shot_player.wav"));
        shotShotAttackEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/shot_shot_attack_enemy.wav"));
        agrShortAttackEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/agr_short_attack_enemy.wav"));
        agrShotAttackEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/agr_shot_attack_enemy.wav"));
        pickCoin = Gdx.audio.newSound(Gdx.files.internal("sounds/pick_coin.wav"));
        switchToggle = Gdx.audio.newSound(Gdx.files.internal("sounds/switch.wav"));
        pickUpHeal = Gdx.audio.newSound(Gdx.files.internal("sounds/heal.wav"));
    }

    private static void fillMap(Map<String, List<Sound>> map, String path, String key) {
        List<Sound> sounds = new ArrayList<>();
        fillSoundList(sounds, path + key.toLowerCase() + "/");
        map.put(key, sounds);
    }

    private static void fillSoundList(List<Sound> sounds, String path) {
        File directory = new File(Gdx.files.getLocalStoragePath() + "core/assets/" + path);
        int count = Objects.requireNonNull(directory.listFiles()).length;
        for (int i = 1; i <= count; i++) {
            sounds.add(Gdx.audio.newSound(Gdx.files.internal(path + i + ".wav")));
        }
    }

    public static Sound getShotPlayer() {
        return shotPlayer;
    }

    public static Sound getAgrShortAttackEnemy() {
        return agrShortAttackEnemy;
    }

    public static Sound getAgrShotAttackEnemy() {
        return agrShotAttackEnemy;
    }

    public static Sound getPickCoin() {
        return pickCoin;
    }

    public static Sound getShotShotAttackEnemy() {
        return shotShotAttackEnemy;
    }

    public static Sound getSwitchToggle() {
        return switchToggle;
    }

    public static Sound getPickUpHeal() {
        return pickUpHeal;
    }
}
