package ru.gachigame.game.shooter.gameobject.character;

import com.badlogic.gdx.math.Rectangle;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class Slave extends Character {
    public byte recharge;
    public Rectangle shotDistanceHitBox = new Rectangle();
    public Rectangle fieldOfView = new Rectangle();
    public float slaveX = 0;
    public float slaveY = 0;
    public float speed;
    public String type;

    public Slave(){
        sprites = SHOOTER_SLAVE_TEXTURES;
        texture = sprites.get(UP);

        width = 8;
        height = 8;
        HP = 2;
        recharge = 20;
        fieldOfView.width = 200;
        fieldOfView.height = 200;
        speed = 0.8f;
        type = "slave";
    }

    public void moveUp(){
        texture = sprites.get(UP);
        y += speed;
        direction = "NORTH";
    }
    public void moveDown(){
        texture = sprites.get(DOWN);
        y -= speed;
        direction = "SOUTH";
    }
    public void moveRight(){
        texture = sprites.get(RIGHT);
        x += speed;
        direction = "EAST";
    }
    public void moveLeft(){
        texture = sprites.get(LEFT);
        x -= speed;
        direction = "WEST";
    }

    public void sightCalibration(){
        shotDistanceHitBox.x = x;
        shotDistanceHitBox.y = y;
        switch (direction) {
            case "NORTH" -> {
                shotDistanceHitBox.width = 8;
                shotDistanceHitBox.height = 200;
            }
            case "SOUTH" -> {
                shotDistanceHitBox.y = y - 200;
                shotDistanceHitBox.width = 8;
                shotDistanceHitBox.height = 200;
            }
            case "EAST" -> {
                shotDistanceHitBox.width = 200;
                shotDistanceHitBox.height = 8;
            }
            case "WEST" -> {
                shotDistanceHitBox.x = x - 200;
                shotDistanceHitBox.width = 200;
                shotDistanceHitBox.height = 8;
            }
        }
    }
}
