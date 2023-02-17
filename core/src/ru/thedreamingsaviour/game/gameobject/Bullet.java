package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.BULLET_ILYA;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.BULLET_SHOT_ATTACK_ENEMY;

public class Bullet extends Rectangle {
    public String type;
    public String direction;
    private float targetX;
    private float targetY;
    private Rectangle targetAim;
    private final float speed;
    private float speedX;
    private float speedY;
    public int damage;
    private boolean moved;
    private float oldX;
    private float oldY;
    public AnimatedObject textures = new AnimatedObject(new ArrayList<>());

    public Bullet(String type) {
        this.type = type;
        switch (type) {
            case "GOOD" -> textures.setTextures(BULLET_ILYA);
            case "BAD" -> textures.setTextures(BULLET_SHOT_ATTACK_ENEMY);
        }
        speed = 25;
        speedX = speed;
        speedY = speed;
    }

    public void move(Rectangle aim) {
        oldX = x;
        oldY = y;
        switch (direction) {
            case "NORTH" -> y += speed;
            case "SOUTH" -> y -= speed;
            case "WEST", "LEFT" -> x -= speed;
            case "EAST", "RIGHT" -> x += speed;
            case "moveTo" -> moveTo();
            case "moveToTarget" -> moveToTarget(aim);
        }
    }

    public void moveTo() {
        if (!moved) {
            calculateSpeedX();
            calculateSpeedY();
            moved = true;
        } else {
            if (x > targetX) {
                x -= speedX;
                targetX -= speedX;
            }
            if (y > targetY) {
                y -= speedY;
                targetY -= speedY;
            }
            if (x < targetX) {
                x += speedX;
                targetX += speedX;
            }
            if (y < targetY) {
                y += speedY;
                targetY += speedY;
            }
        }
    }

    public void moveToTarget(Rectangle aim) {
        if (aim.x + aim.width / 2 >= x) {
            x += speed;
        }
        if (aim.y + aim.height / 2 >= y) {
            y += speed;
        }
        if (aim.x <= x) {
            x -= speed;
        }
        if (aim.y <= y) {
            y -= speed;
        }
    }

    private void calculateSpeedX() {
        double tan = Math.abs(x - targetX) / Math.abs(y - targetY);

        float angle = (float) Math.toDegrees(Math.atan(tan));

        if (angle >= 45) {
            speedX = speed;
        } else {
            float percent = 100 / (45 / angle);
            speedX = speed / 100 * percent;
        }
    }

    private void calculateSpeedY() {
        double tan = Math.abs(y - targetY) / Math.abs(x - targetX);

        float angle = (float) Math.toDegrees(Math.atan(tan));
        angle = 90 - angle;

        if (angle <= 45) {
            speedY = speed;
        } else {
            float percent = 100 / (45 / (angle - 45));
            speedY = speed - (speed / 100 * percent);
        }
    }

    public void setTargetAim(Rectangle targetAim) {
        this.targetAim = targetAim;
        targetX = targetAim.x + targetAim.width / 2;
        targetY = targetAim.y + targetAim.height / 2;
    }

    public Rectangle getTargetAim() {
        return targetAim;
    }

    public float getOldX() {
        return oldX;
    }

    public float getOldY() {
        return oldY;
    }
}
