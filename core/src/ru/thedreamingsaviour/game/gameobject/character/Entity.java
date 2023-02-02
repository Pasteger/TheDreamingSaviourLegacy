package ru.thedreamingsaviour.game.gameobject.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.AnimatedObject;
import ru.thedreamingsaviour.game.gameobject.Bullet;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.logics.LevelsLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.BULLET_ILYA;

public abstract class Entity extends Rectangle {
    public String type;
    Map<String, List<Texture>> sprites;
    public byte HP;
    public String direction = "NORTH";
    public AnimatedObject sprite = new AnimatedObject();
    Rectangle legs = new Rectangle();
    int saveSpeed;
    int speed;
    public boolean gravitated;
    public int jumped;
    public int timeFall;
    public long deltaTime;

    public void move(List<Surface> surfaces, List<Enemy> enemies, List<Box> boxes) {
        speed = saveSpeed;
        List<Entity> entities = new ArrayList<>();
        entities.add(this);
        entities.addAll(enemies);

        sprite.setTextures(sprites.get(direction));


        switch (direction) {
            case "NORTH" -> legs.y += saveSpeed;
            case "SOUTH" -> legs.y -= saveSpeed;
            case "EAST", "RIGHT" -> legs.x += saveSpeed;
            case "WEST", "LEFT" -> legs.x -= saveSpeed;
        }
        for (Box box : boxes) {
            if (legs.overlaps(box)) {
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
        for (Box box : boxes) {
            if (legs.overlaps(box) && !this.legs.equals(box.legs)) {
                box.moveBox(this, surfaces, entities, boxes);
                backLegsDirection(box);
            }
        }
        for (Enemy enemy : enemies) {
            if (legs.overlaps(enemy.legs) && !this.legs.equals(enemy.legs)) {
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

    public void shot(String bulletType) {
        Bullet bullet = new Bullet();
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
        bullet.height = 30;
        bullet.width = 30;
        bullet.direction = direction;
        bullet.type = bulletType;
        switch (bulletType) {
            case "GOOD" -> bullet.textures.setTextures(BULLET_ILYA);
            case "BAD" -> bullet.textures.setTextures(BULLET_ILYA);
        }
        LevelsLogic.BULLET_LIST.add(bullet);
    }

    public void fall(List<Surface> surfaces, List<Entity> entities) {
        if (gravitated) {
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
            y = legs.y;
        }
    }

    public void jump(List<Surface> surfaces, List<Box> boxes) {
        for (Surface surface : surfaces) {
            legs.y--;
            if (surface.overlaps(legs) && surface.y < y && surface.getEffect().equals("solid")) {
                if (jumped == 0)
                    jumped = 50;
            }
            legs.y++;
        }
        for (Box box : boxes) {
            legs.y--;
            if (legs.overlaps(box) && y - box.y >= box.width - 1) {
                if (jumped == 0)
                    jumped = 50;
            }
            legs.y++;
        }
    }

    public void jumpRender(List<Surface> surfaces, List<Box> boxes) {
        if (jumped > 0) {
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
}
