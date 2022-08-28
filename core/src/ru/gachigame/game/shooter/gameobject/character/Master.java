package ru.gachigame.game.shooter.gameobject.character;

import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.logics.ShooterLevelsLogic;
import ru.gachigame.game.shooter.gameobject.character.parts.Cum;

import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class Master extends Slave {
    public Master(){
        legs = new Rectangle();
        sprites = getShooterMasterTextures();
        texture = sprites.get(UP);
        width = 68;
        height = 68;
        HP = 25;
        recharge = 20;
        speed = 1;
        fieldOfView.width = 500;
        fieldOfView.height = 500;
        type = "master";

        legs.width = width;
        legs.height = height;
    }

    @Override
    public void shot(String cumType) {
        Cum cum = new Cum();
        switch (direction) {
            case "NORTH" -> {
                cum.x = x + 17;
                cum.y = y + 68;
            }
            case "SOUTH" -> {
                cum.x = x + 17;
                cum.y = y - 17;
            }
            case "WEST" -> {
                cum.x = x - 17;
                cum.y = y + 17;
            }
            case "EAST" -> {
                cum.x = x + 68;
                cum.y = y + 17;
            }
        }
        cum.height = 17;
        cum.width = 17;
        cum.direction = direction;
        cum.type = "MASTERS";
        cum.texture = getMasterCumTexture();
        ShooterLevelsLogic.cumList.add(cum);
    }

    @Override
    public void sightCalibration(){
        shotDistanceHitBox.x = x;
        shotDistanceHitBox.y = y;
        switch (direction) {
            case "NORTH" -> {
                shotDistanceHitBox.width = 68;
                shotDistanceHitBox.height = 600;
            }
            case "SOUTH" -> {
                shotDistanceHitBox.y = y - 600;
                shotDistanceHitBox.width = 68;
                shotDistanceHitBox.height = 600;
            }
            case "EAST" -> {
                shotDistanceHitBox.width = 600;
                shotDistanceHitBox.height = 68;
            }
            case "WEST" -> {
                shotDistanceHitBox.x = x - 600;
                shotDistanceHitBox.width = 600;
                shotDistanceHitBox.height = 68;
            }
        }
    }
}
