package ru.thedreamingsaviour.game.gameobject.shooter.character;

import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.resourceloader.TextureLoader;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class ShortAttackEnemy extends Enemy {
    public byte recharge;
    public Rectangle fieldOfView = new Rectangle();

    public ShortAttackEnemy() {
        legs = new Rectangle();
        sprites = TextureLoader.getShooterShortAttackEnemyTextures();
        texture = sprites.get(UP);
        width = 300;
        height = 300;
        HP = 3;
        recharge = 20;
        fieldOfView.width = 3000;
        fieldOfView.height = 3000;
        speed = 10;
        type = "ShortAttackEnemy";

        legs.width = width;
        legs.height = height;
    }

    public void attack(Ilya ilya) {
        if (this.overlaps(ilya)) {
            recharge--;
            if (recharge <= 0) {
                recharge = (byte) ((byte) 12 + random.nextInt(50));
                ilya.HP--;
            }
        }
    }

    public void moveToPlayer(Ilya ilya, List<Surface> surfaceList, List<Enemy> enemies) {
        fieldOfView.x = x - fieldOfView.width / 2;
        fieldOfView.y = y - fieldOfView.height / 2;
        if (fieldOfView.overlaps(ilya)) {
            if (ilya.x > x && ilya.x - x >= width) {
                moveRight(surfaceList, enemies);
            }
            if (ilya.x < x && x - ilya.x >= width) {
                moveLeft(surfaceList, enemies);
            }
            if (ilya.y > y && ilya.y - y >= width) {
                moveUp(surfaceList, enemies);
            }
            if (ilya.y < y && y - ilya.y >= width) {
                moveDown(surfaceList, enemies);
            }
        }
    }
}
