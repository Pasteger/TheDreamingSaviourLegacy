package ru.gachigame.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import ru.gachigame.game.characters.parts.Cum;
import ru.gachigame.game.characters.parts.CumArray;

public class Slave extends Rectangle {
    final Array<Texture> slaveSprites = new Array<>();
    public byte recharge;
    public byte HP;
    public Rectangle shotDistanceHitBox = new Rectangle();
    public Rectangle fieldOfView = new Rectangle();
    public String direction = "NORTH";
    public Texture texture;
    public float slaveX = 0;
    public float slaveY = 0;
    public float speed;
    public String type;

    public Slave(){
        slaveSprites.add(
                new Texture("sprites/slave_up_sprite.png"),
                new Texture("sprites/slave_down_sprite.png"),
                new Texture("sprites/slave_right_sprite.png"),
                new Texture("sprites/slave_left_sprite.png")
        );
        texture = slaveSprites.get(0);
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
        texture = slaveSprites.get(0);
        y += speed;
        direction = "NORTH";
    }
    public void moveDown(){
        texture = slaveSprites.get(1);
        y -= speed;
        direction = "SOUTH";
    }
    public void moveRight(){
        texture = slaveSprites.get(2);
        x += speed;
        direction = "EAST";
    }
    public void moveLeft(){
        texture = slaveSprites.get(3);
        x -= speed;
        direction = "WEST";
    }


    public void shot(){
        Cum cum = new Cum();
        switch (direction){
            case "NORTH":
                cum.x = x+2;
                cum.y = y+6;
                break;
            case "SOUTH":
                cum.x = x+2;
                cum.y = y;
                break;
            case "WEST":
                cum.x = x+1;
                cum.y = y+2;
                break;
            case "EAST":
                cum.x = x+6;
                cum.y = y+2;
                break;
        }
        cum.height = 2;
        cum.width = 2;
        cum.direction = direction;
        cum.type = "BAD";
        cum.texture = new Texture("sprites/bad_cum.png");
        CumArray.cumArray.add(cum);
    }

    public void sightCalibration(){
        shotDistanceHitBox.x = x;
        shotDistanceHitBox.y = y;
        switch (direction) {
            case "NORTH":
                shotDistanceHitBox.width = 8;
                shotDistanceHitBox.height = 200;
                break;
            case "SOUTH":
                shotDistanceHitBox.y = y - 200;
                shotDistanceHitBox.width = 8;
                shotDistanceHitBox.height = 200;
                break;
            case "EAST":
                shotDistanceHitBox.width = 200;
                shotDistanceHitBox.height = 8;
                break;
            case "WEST":
                shotDistanceHitBox.x = x - 200;
                shotDistanceHitBox.width = 200;
                shotDistanceHitBox.height = 8;
                break;
        }
    }
}
