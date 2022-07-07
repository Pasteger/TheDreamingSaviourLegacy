package ru.gachigame.game.gameobject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class Wall extends Rectangle {
    public TextureRegion texture;
    public Wall(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setStandardTexture();
    }
    public void setStandardTexture(){
        texture = new TextureRegion(WALL_TEXTURE, (int) x, (int) y, (int) width, (int) height);
    }
    public void setEditableTexture(){
        texture = new TextureRegion(EDIT_WALL_TEXTURE, (int) x, (int) y, (int) width, (int) height);
    }
}
