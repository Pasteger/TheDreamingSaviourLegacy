package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.*;

public class Coin extends Rectangle {
    public final int value;
    public final AnimatedObject textures = new AnimatedObject(new ArrayList<>());

    public Coin(float x, float y, int value){
        this.x = x;
        this.y = y;
        this.value = value;
        height = 120;
        width = 120;
        switch (value) {
            case 1 -> textures.setTextures(COIN_1);
            case 10 -> textures.setTextures(COIN_10);
            case 100 -> textures.setTextures(COIN_100);
            case 1000 -> textures.setTextures(COIN_1000);
            case 5000 -> textures.setTextures(COIN_5000);
        }
    }
}
