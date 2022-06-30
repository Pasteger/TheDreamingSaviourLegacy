package ru.gachigame.game.parts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Wall extends Rectangle {
    public TextureRegion texture;
    public Wall(int x, int y, int width, int height){
        texture = new TextureRegion(new Texture("sprites/wall.png"), x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
