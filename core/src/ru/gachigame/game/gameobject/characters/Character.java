package ru.gachigame.game.gameobject.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import org.json.simple.JSONObject;
import ru.gachigame.game.JSONReader;
import ru.gachigame.game.gameobject.characters.parts.Cum;
import ru.gachigame.game.screen.ShooterLevelScreen;
import java.util.HashMap;
import java.util.Map;

public class Character extends Rectangle {
    final String UP = "upSprite";
    final String DOWN = "downSprite";
    final String LEFT = "leftSprite";
    final String RIGHT = "rightSprite";
    final Map<String, Texture> sprites = new HashMap<>();
    public byte HP;
    public String direction = "NORTH";
    public Texture texture;

    void fillSpritesMap(JSONObject spriteJSONObject){
        sprites.put(UP, new Texture(JSONReader.getSpritePath(spriteJSONObject, UP)));
        sprites.put(DOWN, new Texture(JSONReader.getSpritePath(spriteJSONObject, DOWN)));
        sprites.put(LEFT, new Texture(JSONReader.getSpritePath(spriteJSONObject, LEFT)));
        sprites.put(RIGHT, new Texture(JSONReader.getSpritePath(spriteJSONObject, RIGHT)));
    }

    public void shot(String cumType){
        Cum cum = new Cum();
        switch (direction) {
            case "NORTH" -> {
                cum.x = x + 2;
                cum.y = y + 6;
            }
            case "SOUTH" -> {
                cum.x = x + 2;
                cum.y = y;
            }
            case "WEST" -> {
                cum.x = x + 1;
                cum.y = y + 2;
            }
            case "EAST" -> {
                cum.x = x + 6;
                cum.y = y + 2;
            }
        }
        cum.height = 2;
        cum.width = 2;
        cum.direction = direction;
        cum.type = cumType;
        switch (cumType){
            case "GOOD" -> cum.texture = new Texture(JSONReader.getSpritePath(JSONReader.CUM_SPRITES, "cum"));
            case "BAD" -> cum.texture = new Texture(JSONReader.getSpritePath(JSONReader.CUM_SPRITES, "badCum"));
        }
        ShooterLevelScreen.cumArray.add(cum);
    }
}
