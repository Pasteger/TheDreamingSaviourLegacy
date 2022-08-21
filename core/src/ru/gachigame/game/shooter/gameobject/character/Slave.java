package ru.gachigame.game.shooter.gameobject.character;

import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.gameobject.Surface;
import java.util.List;
import static com.badlogic.gdx.math.MathUtils.random;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class Slave extends Character {
    public byte recharge;
    public Rectangle shotDistanceHitBox = new Rectangle();
    public Rectangle fieldOfView = new Rectangle();
    public String type;

    public Slave(){
        legs = new Rectangle();
        sprites = getShooterSlaveTextures();
        texture = sprites.get(UP);

        width = 8;
        height = 8;
        HP = 2;
        recharge = 20;
        fieldOfView.width = 200;
        fieldOfView.height = 200;
        speed = 1;
        type = "slave";

        legs.width = width;
        legs.height = height;
    }

    public void slaveShot(Billy billy){
        if (shotDistanceHitBox.overlaps(billy)) {
            recharge--;
            if (recharge <= 0) {
                recharge = (byte) ((byte) 12 + random.nextInt(50));
                shot("BAD");
            }
        }
    }

    public void moveToBilly(Billy billy, List<Surface> surfaceList, List<Slave> slaveList){
        fieldOfView.x = x - fieldOfView.width/2;
        fieldOfView.y = y - fieldOfView.height/2;
        if (fieldOfView.overlaps(billy)) {
            if (billy.x > x && billy.x - x >= width) {
                moveRight(surfaceList, slaveList);
            }
            if (billy.x < x && x - billy.x >= width) {
                moveLeft(surfaceList, slaveList);
            }
            if (billy.y > y && billy.y - y >= width) {
                moveUp(surfaceList, slaveList);
            }
            if (billy.y < y && y - billy.y >= width) {
                moveDown(surfaceList, slaveList);
            }
        }
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
