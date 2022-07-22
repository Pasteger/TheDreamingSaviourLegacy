package ru.gachigame.game.gameobject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import static ru.gachigame.game.resourceloader.TextureLoader.getCumTexture;

public class Floor extends Rectangle {
    public Sprite sprite;
    private String effect;
    public Floor(int x, int y, int width, int height){
        sprite = new Sprite();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        sprite.setBounds(x, y, width, height);
        setStandardTexture();
    }
    public void setStandardTexture(){
        sprite.setTexture(getCumTexture());
        sprite.setColor(1, 0, 1, 1);
    }
    public void setEditableTexture(){
        sprite.setColor(1, 1, 0, 1);
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}
