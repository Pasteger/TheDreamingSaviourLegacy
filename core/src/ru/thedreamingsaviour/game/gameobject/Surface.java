package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.resourceloader.TextureLoader;

public class Surface extends Rectangle {
    private String textureName;
    private Long id;
    public Sprite sprite;
    private String effect;
    public Surface(float x, float y, float width, float height, String effect, String textureName){
        sprite = new Sprite();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.effect = effect;
        this.textureName = textureName;
        setStandardTexture();
    }
    public void draw(SpriteBatch batch){
        sprite.setBounds(x, y, width, height);
        sprite.draw(batch);
    }
    public void setStandardTexture() {
        switch (textureName) {
            case "floorTexture" -> sprite.setTexture(TextureLoader.getFloorTexture());
            case "wallTexture" -> sprite.setTexture(TextureLoader.getWallTexture());
        }
    }
    public void setEditableTexture(){
        switch (textureName) {
            case "floorTexture" -> sprite.setTexture(TextureLoader.getEditFloorTexture());
            case "wallTexture" -> sprite.setTexture(TextureLoader.getEditWallTexture());
        }
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }
}
