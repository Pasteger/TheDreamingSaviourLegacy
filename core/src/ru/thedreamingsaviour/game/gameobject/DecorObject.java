package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class DecorObject {
    public int x;
    public int y;
    public int width;
    public int height;
    private final int speed;
    private final AnimatedObject animatedObject;
    public DecorObject(List<Texture> textures, int x, int y, int width, int height, int speed) {
        animatedObject = new AnimatedObject(textures);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }
    public void draw(SpriteBatch batch){
        animatedObject.draw(batch, x, y, width, height, speed);
    }
}
