package ru.gachigame.game.gameobject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class Floor extends Rectangle {
    public Sprite sprite;
    private String effect;
    public Floor(int x, int y, int width, int height){
        sprite = new Sprite();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setStandardTexture();
    }
    public void draw(SpriteBatch batch){
        sprite.setBounds(x, y, width, height);
        sprite.draw(batch);
    }
    public void setStandardTexture(){
        sprite.setTexture(getFloorTexture());
    }
    public void setEditableTexture(){
        sprite.setTexture(getEditFloorTexture());
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}
