package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bullet extends Rectangle {
    public String type;
    public String direction;
    public Texture texture;
    public void move() {
        switch (direction) {
            case "NORTH" -> y += 25;
            case "SOUTH" -> y -= 25;
            case "WEST" -> x -= 25;
            case "EAST" -> x += 25;
        }
    }
}
