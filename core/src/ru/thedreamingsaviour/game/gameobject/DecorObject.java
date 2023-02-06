package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.DECOR;

public class DecorObject extends Rectangle {
    private final int speed;
    private final AnimatedObject animatedObject;
    private final String texture;
    public DecorObject(String texture, float x, float y, float width, float height, int speed) {
        this.texture = texture;
        animatedObject = new AnimatedObject(DECOR.get(texture));
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }
    public void draw(SpriteBatch batch){
        animatedObject.draw(batch, x, y, width, height, speed);
    }

    public void editExtension(int width, int height){
        this.width = width;
        this.height = height;
    }
    public void editMove(float x, float y){
        this.x = x;
        this.y = y;
    }

    public String getTexture() {
        return texture;
    }

    public int getSpeed() {
        return speed;
    }
}
