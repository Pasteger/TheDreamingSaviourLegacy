package ru.gachigame.game.shooter.gameobject.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.shooter.gameobject.character.parts.Cum;
import ru.gachigame.game.shooter.screen.ShooterLevelScreen;
import java.util.Map;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class Character extends Rectangle {
    final String UP = "upSprite";
    final String DOWN = "downSprite";
    final String LEFT = "leftSprite";
    final String RIGHT = "rightSprite";
    Map<String, Texture> sprites;
    public byte HP;
    public String direction = "NORTH";
    public Texture texture;

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
            case "GOOD" -> cum.texture = CUM_TEXTURE;
            case "BAD" -> cum.texture = BAD_CUM_TEXTURE;
        }
        ShooterLevelScreen.cumArray.add(cum);
    }
}
