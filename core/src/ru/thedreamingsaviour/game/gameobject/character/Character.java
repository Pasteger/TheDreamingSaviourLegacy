package ru.thedreamingsaviour.game.gameobject.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Bullet;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.logics.LevelsLogic;

import java.util.List;
import java.util.Map;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.BULLET_ILYA;

public abstract class Character extends Rectangle {
    final String UP = "upSprite";
    final String DOWN = "downSprite";
    final String LEFT = "leftSprite";
    final String RIGHT = "rightSprite";
    Map<String, Texture> sprites;
    public byte HP;
    public String direction = "NORTH";
    public Texture texture;
    Rectangle legs;
    int speed;
    public boolean gravitated;
    public int jumped;
    public int timeFall;
    public long deltaTime;


    public void moveUp(List<Surface> surfaces, List<Enemy> enemies) {
        texture = sprites.get(UP);
        direction = "NORTH";
        for (int step = 0; step < speed; step++) {
            legs.y++;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.y--;
                    break;
                }
            }
            for (Enemy enemy : enemies) {
                if (legs.overlaps(enemy.legs) && !legs.equals(enemy.legs)) {
                    legs.y--;
                    break;
                }
            }
        }
        y = legs.y;
    }

    public void moveDown(List<Surface> surfaces, List<Enemy> enemies) {
        texture = sprites.get(DOWN);
        direction = "SOUTH";
        for (int step = 0; step < speed; step++) {
            legs.y--;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.y++;
                    break;
                }
            }
            for (Enemy enemy : enemies) {
                if (legs.overlaps(enemy.legs) && !legs.equals(enemy.legs)) {
                    legs.y++;
                    break;
                }
            }
        }
        y = legs.y;
    }

    public void moveRight(List<Surface> surfaces, List<Enemy> enemies) {
        if (gravitated) {
            texture = sprites.get("rightSpriteP");
            direction = "RIGHT";
        } else {
            texture = sprites.get(RIGHT);
            direction = "EAST";
        }
        for (int step = 0; step < speed; step++) {
            legs.x++;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.x--;
                    break;
                }
            }
            for (Enemy enemy : enemies) {
                if (legs.overlaps(enemy.legs) && !legs.equals(enemy.legs)) {
                    legs.x--;
                    break;
                }
            }
        }
        x = legs.x;
    }

    public void moveLeft(List<Surface> surfaces, List<Enemy> enemies) {
        if (gravitated) {
            texture = sprites.get("leftSpriteP");
            direction = "LEFT";
        } else {
            texture = sprites.get(LEFT);
            direction = "WEST";
        }
        for (int step = 0; step < speed; step++) {
            legs.x--;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.x++;
                    break;
                }
            }
            for (Enemy enemy : enemies) {
                if (legs.overlaps(enemy.legs) && !legs.equals(enemy.legs)) {
                    legs.x++;
                    break;
                }
            }
        }
        x = legs.x;
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

    public void fall(List<Surface> surfaces, List<Enemy> enemies) {
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
                for (Enemy enemy : enemies) {
                    if (legs.overlaps(enemy.legs) && !legs.equals(enemy.legs)) {
                        legs.y++;
                        break;
                    }
                }
            }
            y = legs.y;
        }
    }

    public void jump(List<Surface> surfaces) {
        for (Surface floor : surfaces) {
            legs.y--;
            if (floor.overlaps(legs) && floor.y < y && floor.getEffect().equals("solid")) {
                if (jumped == 0)
                    jumped = 50;
            }
            legs.y++;
        }
    }

    public void jumpRender(List<Surface> surfaces) {
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

            for (int step = 0; step < speed * delta * 1.1; step++) {
                legs.y++;
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
}
