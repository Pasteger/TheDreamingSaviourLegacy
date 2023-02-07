package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Bullet extends Rectangle {
    public String type;
    public String direction;
    public int damage;
    public AnimatedObject textures = new AnimatedObject(new ArrayList<>());

    public void move() {
        switch (direction) {
            case "NORTH" -> y += 25;
            case "SOUTH" -> y -= 25;
            case "WEST", "LEFT" -> x -= 25;
            case "EAST", "RIGHT" -> x += 25;
        }
    }
}
