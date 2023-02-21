package ru.thedreamingsaviour.game.gameobject.entity;

import static com.badlogic.gdx.math.MathUtils.random;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.SHORT_ATTACK_ENEMY;

public class ShortAttackEnemy extends Enemy {
    public ShortAttackEnemy() {
        type = "ShortAttackEnemy";
        sprites = SHORT_ATTACK_ENEMY;
        animatedObject.setTextures(sprites.get("STAND/NORTH"));
        width = 300;
        height = 300;
        HP = 3;
        damage = 1;
        recharge = 0;
        fieldOfView.width = 4000;
        fieldOfView.height = 4000;
        fieldOfView.x = x - fieldOfView.width / 2;
        fieldOfView.y = y - fieldOfView.height / 2;

        generateCells();

        saveSpeed = 10;
        speed = saveSpeed;
        type = "ShortAttackEnemy";

        legs.width = width;
        legs.height = height;
    }

    @Override
    public void attack(Player player) {
        if (Math.abs(x - player.x) < width + speed && Math.abs(y - player.y) <= height + speed) {
            recharge--;
            if (recharge <= 0) {
                recharge = (byte) ((byte) 20 + random.nextInt(60));
                player.takeDamage(damage);
            }
        }
    }
}
