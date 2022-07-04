package ru.gachigame.game.shooter.gameobject.character.parts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Cum extends Rectangle {
    public String type;
    public String direction;
    public Texture texture;
    public void move() {
        switch (direction) {
            case "NORTH" -> y += 3;
            case "SOUTH" -> y -= 3;
            case "WEST" -> x -= 3;
            case "EAST" -> x += 3;
        }
    }
}
