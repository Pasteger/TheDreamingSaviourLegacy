package ru.thedreamingsaviour.game.guiobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.getBossBarBody;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.getBossBarFrame;

public class BossBar {
    private final Texture frame;
    private final Sprite body;
    private final float minWidth;

    public BossBar(float minWidth) {
        this.minWidth = minWidth;
        frame = getBossBarFrame();
        body = new Sprite(getBossBarBody());
    }

    public void draw(SpriteBatch batch, float x, float y, float bossHP) {
        batch.draw(frame, x, y);
        body.setBounds(x + 10, y + 10, minWidth * bossHP, 80);
        body.draw(batch);
    }
}
