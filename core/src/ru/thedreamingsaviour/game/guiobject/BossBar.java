package ru.thedreamingsaviour.game.guiobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.getBossBarBody;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.getBossBarFrame;

public class BossBar {
    private final Texture frame;
    private final Sprite body;
    private final float minWidth;
    private final String name;

    public BossBar(float minWidth, String name) {
        this.minWidth = minWidth;
        frame = getBossBarFrame();
        body = new Sprite(getBossBarBody());
        this.name = name;
    }

    public void draw(SpriteBatch batch, BitmapFont font, float x, float y, float bossHP) {
        font.draw(batch, name, x + 1000, y);
        batch.draw(frame, x, y);
        body.setBounds(x + 10, y + 10, minWidth * bossHP, 80);
        body.draw(batch);
    }
}
