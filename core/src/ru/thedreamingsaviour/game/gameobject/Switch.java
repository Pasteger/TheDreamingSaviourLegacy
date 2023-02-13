package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;
import java.util.Map;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.SWITCH;

public class Switch extends Rectangle {
    private boolean active;
    public boolean gravitated;
    public final Map<String, List<Texture>> textures;
    private final String texture;
    public final AnimatedObject animatedObject;
    private final int speed;

    public Switch(String texture, float x, float y, float width, float height, int speed, boolean status) {
        this.texture = texture;
        textures = SWITCH.get(texture);
        animatedObject = new AnimatedObject();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        active = status;
    }

    public void draw(SpriteBatch batch) {
        String key = active ? "able" : "disable";
        if (!gravitated) {
            key += "_up";
        }

        if (!animatedObject.getTextures().equals(textures.get(key))) {
            animatedObject.setTextures(textures.get(key));
        }

        animatedObject.draw(batch, x, y, width, height, speed);
    }

    public void toggle() {
        active = !active;
    }

    public boolean isActive() {
        return active;
    }

    public void editExtension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void editMove(float x, float y) {
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
