package ru.thedreamingsaviour.game.gameobject.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.AnimatedObject;
import ru.thedreamingsaviour.game.gameobject.Bullet;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.logics.LevelsLogic;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static ru.thedreamingsaviour.game.resourceloader.SoundLoader.*;

public abstract class Entity extends Rectangle {
    public String type;
    Map<String, List<Texture>> sprites;
    public int saveHP;
    public int HP;
    public int damage;
    String bulletType = "";
    String bulletAim = "";
    String saveBulletAim = "";
    public String direction = "NORTH";
    public AnimatedObject animatedObject = new AnimatedObject();
    Rectangle legs = new Rectangle();
    public int saveSpeed;
    public int speed;
    public boolean gravitated;
    public int jumped;
    public int timeFall;
    public long deltaTime;
    boolean isMoved;
    boolean isAgr;
    long timeAgr;

    public void draw(SpriteBatch batch) {
        int drawSpeed = 15 - speed / 10 > 0 ? 15 - speed / 10 : 1;
        float drawX = x;
        float drawY = y;
        animatedObject.rotateSprite(0);
        animatedObject.mirrorSprite(false, false);

        if (gravitated || jumped > 0 || timeFall > 0) {
            switch (direction) {
                case "RIGHT" -> {
                    animatedObject.rotateSprite(0);
                    animatedObject.mirrorSprite(false, false);
                    drawX = x;
                    drawY = y;
                }
                case "LEFT" -> {
                    animatedObject.rotateSprite(0);
                    animatedObject.mirrorSprite(true, false);
                    drawX = x;
                    drawY = y;
                }
            }
        } else {
            switch (direction) {
                case "NORTH" -> {
                    animatedObject.rotateSprite(0);
                    animatedObject.mirrorSprite(false, false);
                    drawX = x;
                    drawY = y;
                }
                case "SOUTH" -> {
                    animatedObject.rotateSprite(0);
                    animatedObject.mirrorSprite(true, true);
                    drawX = x;
                    drawY = y;
                }
                case "EAST" -> {
                    animatedObject.rotateSprite(90);
                    animatedObject.mirrorSprite(true, true);
                    drawX = x + width;
                    drawY = y;
                }
                case "WEST" -> {
                    animatedObject.rotateSprite(90);
                    animatedObject.mirrorSprite(false, false);
                    drawX = x + width;
                    drawY = y;
                }
            }
        }
        animatedObject.draw(batch, drawX, drawY, width, height, drawSpeed);
    }

    public void move(List<Surface> surfaces, List<Entity> entities) {
        speed = saveSpeed;

        if (jumped <= 0 && timeFall <= 0) {
            if (gravitated) {
                animatedObject.setTextures(sprites.get("MOVE/SIDE"));
            } else {
                animatedObject.setTextures(sprites.get("MOVE/TOP"));
            }
        }
        switch (direction) {
            case "NORTH" -> legs.y += saveSpeed;
            case "SOUTH" -> legs.y -= saveSpeed;
            case "EAST", "RIGHT" -> legs.x += saveSpeed;
            case "WEST", "LEFT" -> legs.x -= saveSpeed;
        }
        for (Entity box : entities) {
            if (box.type.equals("Box") && legs.overlaps(box)) {
                speed -= box.HP;
            }
        }
        switch (direction) {
            case "NORTH" -> legs.y -= saveSpeed;
            case "SOUTH" -> legs.y += saveSpeed;
            case "EAST", "RIGHT" -> legs.x -= saveSpeed;
            case "WEST", "LEFT" -> legs.x += saveSpeed;
        }

        for (int step = 0; step < speed; step++) {
            switch (direction) {
                case "NORTH" -> legs.y++;
                case "SOUTH" -> legs.y--;
                case "EAST", "RIGHT" -> legs.x++;
                case "WEST", "LEFT" -> legs.x--;
            }
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    backLegs();
                    break;
                }
            }
        }
        for (Entity entity : entities) {
            if (entity.type.equals("Box") && legs.overlaps(entity) && !this.legs.equals(entity.legs)) {
                entity.moveBox(this, surfaces, entities);
                backLegsDirection((Box) entity);
            }
            if (legs.overlaps(entity.legs) && !this.legs.equals(entity.legs)) {
                backLegsSpeed();
                break;
            }
        }

        y = legs.y;
        x = legs.x;
    }

    void backLegs() {
        switch (direction) {
            case "NORTH" -> legs.y--;
            case "SOUTH" -> legs.y++;
            case "EAST", "RIGHT" -> legs.x--;
            case "WEST", "LEFT" -> legs.x++;
        }
    }

    void backLegsDirection(Box box) {
        if (box.northBlocked && direction.equals("NORTH")) legs.y -= speed;
        if (box.southBlocked && direction.equals("SOUTH")) legs.y += speed;
        if (box.eastBlocked && direction.equals("EAST")) legs.x -= speed;
        if (box.westBlocked && direction.equals("WEST")) legs.x += speed;
    }

    void backLegsSpeed() {
        switch (direction) {
            case "NORTH" -> legs.y -= speed;
            case "SOUTH" -> legs.y += speed;
            case "EAST", "RIGHT" -> legs.x -= speed;
            case "WEST", "LEFT" -> legs.x += speed;
        }
    }


    public void shot(Rectangle aim) {
        switch (type) {
            case "Player" -> getShotPlayer().play(0.35f);
            case "ShotAttackEnemy" -> getShotShotAttackEnemy().play(0.5f);
        }
        Bullet bullet = createBullet();
        bullet.setTargetAim(aim);

        LevelsLogic.BULLET_LIST.add(bullet);
    }

    private Bullet createBullet() {
        Bullet bullet = new Bullet(bulletType);
        if (type.equals("Player")) {
            switch (direction) {
                case "NORTH" -> {
                    bullet.x = x + 244;
                    bullet.y = y + 285;
                }
                case "SOUTH" -> {
                    bullet.x = x + 28;
                    bullet.y = y + 15;
                }
                case "WEST" -> {
                    bullet.x = x + 15;
                    bullet.y = y + 243;
                }
                case "EAST" -> {
                    bullet.x = x + 285;
                    bullet.y = y + 28;
                }
                case "LEFT" -> {
                    bullet.x = x + 66;
                    bullet.y = y + 102;
                }
                case "RIGHT" -> {
                    bullet.x = x + 237;
                    bullet.y = y + 102;
                }
            }
        } else {
            bullet.x = x + width / 2;
            bullet.y = y + height / 2;
        }
        bullet.height = 30;
        bullet.width = 30;

        switch (bulletAim) {
            case "direction" -> bullet.direction = direction;
            case "moveTo" -> bullet.direction = "moveTo";
            case "moveToTarget" -> bullet.direction = "moveToTarget";
        }

        bullet.damage = damage;

        return bullet;
    }

    public void fall(List<Surface> surfaces, List<Entity> entities) {
        if (gravitated) {
            int oldTimeFall = timeFall;
            timeFall++;
            deltaTime = System.currentTimeMillis();
            for (int step = 0; step < timeFall; step++) {
                legs.y--;
                for (Surface surface : surfaces) {
                    if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                        legs.y++;
                        timeFall = 0;
                        break;
                    }
                }
                for (Entity entity : entities) {
                    if ((entity.type.equals("Box") || type.equals("Box")) && legs.overlaps(entity) && !legs.equals(entity.legs)) {
                        legs.y++;
                        timeFall = 0;
                        break;
                    }
                }
            }
            if (timeFall != oldTimeFall) {
                y = legs.y;
                if (jumped <= 0) {
                    if (!(direction.equals("RIGHT") || direction.equals("LEFT"))) {
                        direction = "RIGHT";
                    }
                    if (!type.equals("Box")) {
                        animatedObject.setTextures(sprites.get("FALL"));
                    }
                }
            }
        }
    }

    public void jump(List<Surface> surfaces, List<Entity> entities) {
        for (Surface surface : surfaces) {
            legs.y--;
            if (surface.overlaps(legs) && surface.y < y && surface.getEffect().equals("solid")) {
                if (jumped == 0)
                    jumped = 50;
            }
            legs.y++;
        }
        for (Entity entity : entities) {
            legs.y--;
            if (entity.type.equals("Box") && legs.overlaps(entity) && y - entity.y >= entity.width - 1) {
                if (jumped == 0)
                    jumped = 50;
            }
            legs.y++;
        }
    }

    public void jumpRender(List<Surface> surfaces, List<Box> boxes) {
        if (jumped > 0) {
            if (!(direction.equals("RIGHT") || direction.equals("LEFT"))) {
                direction = "RIGHT";
            }
            animatedObject.setTextures(sprites.get("JUMP"));
            if (!gravitated) {
                jumped = 0;
                return;
            }
            jumped--;
            float delta;
            if (jumped > 9) {
                delta = Float.parseFloat("1." + jumped);
            } else {
                delta = Float.parseFloat("1.0" + jumped);
            }

            int localSpeed = Math.max(speed, 20);

            for (int step = 0; step < localSpeed * delta * 1.1; step++) {
                legs.y++;
                for (Box box : boxes) {
                    if (legs.overlaps(box) && box.y > y) {
                        jumped = 0;
                        break;
                    }
                }
                for (Surface surface : surfaces) {
                    if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                        legs.y--;
                        jumped = 0;
                        break;
                    }
                    legs.y -= 2;
                    if (jumped < 48 && legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                        legs.y++;
                        jumped = 0;
                        break;
                    }
                    legs.y += 2;
                }
            }
            y = legs.y;
        }
    }

    public void moveBox(Entity summoner, List<Surface> surfaces, List<Entity> entities) {
    }

    @Override
    public Rectangle setX(float x) {
        this.x = x;
        legs.x = x;
        return this;
    }

    @Override
    public Rectangle setY(float y) {
        this.y = y;
        legs.y = y;
        return this;
    }

    public int getSpeed() {
        return speed;
    }

    public void heal() {
        HP = saveHP;
    }

    public void takeHeal(int value) {
        HP += value;
    }

    public void takeDamage(int damage) {
        if (type.contains("Player")) {
            List<Sound> sounds = DAMAGE.get(type.toUpperCase());
            Sound damageSound = sounds.get(new Random().nextInt(sounds.size()));
            damageSound.play(0.8f);
        } else if (type.contains("Enemy")) {
            List<Sound> sounds = DAMAGE.get("ENEMY");
            Sound damageSound = sounds.get(new Random().nextInt(sounds.size()));
            damageSound.play(0.45f);
        }

        HP -= damage;
    }

    public void setBulletAim(String bulletAim) {
        this.bulletAim = bulletAim;
        saveBulletAim = bulletAim;
    }
}
