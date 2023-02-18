package ru.thedreamingsaviour.game.gameobject.entity;

import static com.badlogic.gdx.math.MathUtils.random;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.SHOT_ATTACK_ENEMY;

public class ShotAttackEnemy extends Enemy{
    public ShotAttackEnemy() {
        type = "ShortAttackEnemy";
        sprites = SHOT_ATTACK_ENEMY;
        animatedObject.setTextures(sprites.get("STAND/NORTH"));
        width = 300;
        height = 300;
        HP = 2;
        recharge = 0;
        damage = 1;
        fieldOfView.width = 5000;
        fieldOfView.height = 5000;
        fieldOfView.x = x - fieldOfView.width / 2;
        fieldOfView.y = y - fieldOfView.height / 2;

        generateCells();

        saveSpeed = 2;
        speed = saveSpeed;
        type = "ShotAttackEnemy";
        bulletType = "BAD";
        bulletAim = "moveTo";

        legs.width = width;
        legs.height = height;
    }

    @Override
    public void attack(Player player) {
        if (fieldOfView.overlaps(player)) {
            recharge--;
            if (recharge <= 0) {
                recharge = (byte) ((byte) 20 + random.nextInt(60));
                shot(player);
            }
        }
    }
}
