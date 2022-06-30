package ru.gachigame.game.characters;

import com.badlogic.gdx.graphics.Texture;
import ru.gachigame.game.characters.parts.Cum;
import ru.gachigame.game.characters.parts.CumArray;

public class Master extends Slave {

    public Master(){
        slaveSprites.clear();
        slaveSprites.add(
                new Texture("sprites/master_up_sprite.png"),
                new Texture("sprites/master_down_sprite.png"),
                new Texture("sprites/master_right_sprite.png"),
                new Texture("sprites/master_left_sprite.png")
        );
        texture = slaveSprites.get(0);
        width = 68;
        height = 68;
        HP = 25;
        recharge = 20;
        fieldOfView.width = 500;
        fieldOfView.height = 500;
        type = "master";
    }

    @Override
    public void shot() {
        Cum cum = new Cum();
        switch (direction) {
            case "NORTH":
                cum.x = x + 17;
                cum.y = y + 68;
                break;
            case "SOUTH":
                cum.x = x + 17;
                cum.y = y - 17;
                break;
            case "WEST":
                cum.x = x - 17;
                cum.y = y + 17;
                break;
            case "EAST":
                cum.x = x + 68;
                cum.y = y + 17;
                break;
        }
        cum.height = 17;
        cum.width = 17;
        cum.direction = direction;
        cum.type = "MASTERS";
        cum.texture = new Texture("sprites/master_cum.png");
        CumArray.cumArray.add(cum);
    }

    @Override
    public void sightCalibration(){
        shotDistanceHitBox.x = x;
        shotDistanceHitBox.y = y;
        switch (direction) {
            case "NORTH":
                shotDistanceHitBox.width = 68;
                shotDistanceHitBox.height = 600;
                break;
            case "SOUTH":
                shotDistanceHitBox.y = y - 600;
                shotDistanceHitBox.width = 68;
                shotDistanceHitBox.height = 600;
                break;
            case "EAST":
                shotDistanceHitBox.width = 600;
                shotDistanceHitBox.height = 68;
                break;
            case "WEST":
                shotDistanceHitBox.x = x - 600;
                shotDistanceHitBox.width = 600;
                shotDistanceHitBox.height = 68;
                break;
        }
    }
}
