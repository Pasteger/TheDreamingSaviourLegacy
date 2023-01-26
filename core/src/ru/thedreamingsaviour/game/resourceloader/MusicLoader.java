package ru.thedreamingsaviour.game.resourceloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicLoader {
    private static Music deathMusic;
    private static Music menuMusic;
    private static Music factoryMusic;
    private static Music introductionMusic;
    public static void load() {
        deathMusic = Gdx.audio.newMusic(Gdx.files.internal("music/eternity_served_cold.mp3"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/biophosphoradelecrystalluminescence.mp3"));
        factoryMusic = Gdx.audio.newMusic(Gdx.files.internal("music/another_medium.mp3"));
        introductionMusic = Gdx.audio.newMusic(Gdx.files.internal("music/overture.mp3"));
    }

    public static Music getDeathMusic() {
        return deathMusic;
    }

    public static Music getMenuMusic() {
        return menuMusic;
    }

    public static Music getFactoryMusic() {
        return factoryMusic;
    }

    public static Music getIntroductionMusic() {
        return introductionMusic;
    }
}
