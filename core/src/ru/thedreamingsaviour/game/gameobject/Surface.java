package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.*;

public class Surface extends Rectangle {
    private String standardColor;
    private Color currentColor;
    public Sprite sprite;
    private String effect;
    public Surface(float x, float y, float width, float height, String effect, String standardColor){
        sprite = new Sprite();
        sprite.setTexture(getNullTexture());
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.effect = effect;
        this.standardColor = standardColor;
        setStandardColor();
    }
    public void draw(SpriteBatch batch){
        sprite.setBounds(x, y, width, height);
        sprite.setColor(currentColor);
        sprite.draw(batch);
    }

    public void setStandardColor() {
        setColor(standardColor);
    }
    public void setEditableColor(){
        String[] rgbaS = standardColor.split(";");
        Float[] rgba = new Float[4];
        for (int i = 0; i < 4; i++){
            rgba[i] = Float.parseFloat(rgbaS[i]);
        }
        rgba[0] += 0.2f;
        rgba[1] += 0.2f;
        rgba[2] += 0.2f;
        String color = rgba[0] + ";" + rgba[1] + ";" + rgba[2] + ";" + rgba[3];
        setColor(color);
    }

    private void setColor(String color) {
        String[] rgbaS = color.split(";");
        Float[] rgba = new Float[4];
        for (int i = 0; i < 4; i++){
            rgba[i] = Float.parseFloat(rgbaS[i]);
        }
        currentColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public void setStandardColor(String standardColor) {
        this.standardColor = standardColor;
        setStandardColor();
    }

    public void editExtension(int width, int height){
        this.width = width;
        this.height = height;
    }
    public void editMove(float x, float y){
        this.x = x;
        this.y = y;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getStandardColor() {
        return standardColor;
    }
}
