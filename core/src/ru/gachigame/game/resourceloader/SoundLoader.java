package ru.gachigame.game.resourceloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundLoader {
    private static Sound sometimesIRipTheSkin;

    public static void load() {
        sometimesIRipTheSkin = Gdx.audio.newSound(Gdx.files.internal("sounds/sometimes_i_rip_the_skin.wav"));
    }

    public static Sound getSometimesIRipTheSkin() {
        return sometimesIRipTheSkin;
    }
}
