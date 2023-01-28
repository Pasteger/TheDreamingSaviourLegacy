package ru.thedreamingsaviour.game.gameobject.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.resourceloader.TextureLoader;

import java.util.List;

public class Ilya extends Character {
    public Ilya() {
        super();
        legs = new Rectangle();
        sprites = TextureLoader.getShooterIlyaTextures();

        texture = sprites.get(UP);
        HP = 4;
        setX(3000);
        setY(3000);
        width = 300;
        height = 300;
        speed = 20;

        legs.width = width;
        legs.height = height;
    }

    public void move(List<Surface> surfaces, List<Enemy> enemies) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft(surfaces, enemies);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight(surfaces, enemies);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && gravitated) {
            jump(surfaces);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP) && !gravitated) {
            moveUp(surfaces, enemies);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !gravitated) {
            moveDown(surfaces, enemies);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            shot("GOOD");
        }
    }

    @Override
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
        }
        for (Enemy enemy : enemies) {
            if (legs.overlaps(enemy.legs)) {
                legs.y -= speed;
                break;
            }
        }
        y = legs.y;
    }

    @Override
    public void moveRight(List<Surface> surfaces, List<Enemy> enemies) {
        if (gravitated) {
            texture = sprites.get("rightSpriteP");
            direction = "RIGHT";
        }
        else {
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
        }
        for (Enemy enemy : enemies) {
            if (legs.overlaps(enemy.legs) && !legs.equals(enemy.legs)) {
                legs.x -= speed;
                break;
            }
        }
        x = legs.x;
    }

    @Override
    public void moveLeft(List<Surface> surfaces, List<Enemy> enemies) {
        if (gravitated) {
            texture = sprites.get("leftSpriteP");
            direction = "LEFT";
        }
        else {
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
        }
        for (Enemy enemy : enemies) {
            if (legs.overlaps(enemy.legs) && !legs.equals(enemy.legs)) {
                legs.x += speed;
                break;
            }
        }
        x = legs.x;
    }

    @Override
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
        }
        for (Enemy enemy : enemies) {
            if (legs.overlaps(enemy.legs)) {
                legs.y += speed;
                break;
            }
        }
        y = legs.y;
    }
}
