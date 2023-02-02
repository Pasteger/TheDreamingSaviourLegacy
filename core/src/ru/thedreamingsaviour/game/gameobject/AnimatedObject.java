package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.List;

public class AnimatedObject extends Rectangle {
    private List<Texture> textures;
    private int currentFrame;
    private int speed;

    public AnimatedObject(List<Texture> textures) {
        this.textures = textures;
    }
    public AnimatedObject() {}

    public void draw(SpriteBatch batch, float x, float y, int speed) {
        if (currentFrame > textures.size() - 1) {
            currentFrame = 0;
        }
        batch.draw(textures.get(currentFrame), x, y);
        if (this.speed == speed) {
            currentFrame++;
            this.speed = 0;
        } else {
            this.speed++;
        }
    }

    public void setTextures(List<Texture> textures) {
        this.textures = textures;
    }
}
