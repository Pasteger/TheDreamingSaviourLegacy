package ru.thedreamingsaviour.game.gameobject.shooter.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Bullet;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.logics.ShooterLevelsLogic;
import ru.thedreamingsaviour.game.resourceloader.TextureLoader;

import java.util.List;
import java.util.Map;

public class Character extends Rectangle {
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
        texture = sprites.get(RIGHT);
        direction = "EAST";
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
        texture = sprites.get(LEFT);
        direction = "WEST";
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
        }
        bullet.height = 30;
        bullet.width = 30;
        bullet.direction = direction;
        bullet.type = bulletType;
        switch (bulletType) {
            case "GOOD" -> bullet.texture = TextureLoader.getBulletIlyaTexture();
            case "BAD" -> bullet.texture = TextureLoader.getBulletIlyaTexture();
        }
        ShooterLevelsLogic.BULLET_LIST.add(bullet);
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
