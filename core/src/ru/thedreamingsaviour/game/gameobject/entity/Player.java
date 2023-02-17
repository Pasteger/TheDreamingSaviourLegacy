package ru.thedreamingsaviour.game.gameobject.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.thedreamingsaviour.game.gameobject.Surface;

import java.util.ArrayList;
import java.util.List;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.PLAYER_TEXTURES;

public class Player extends Entity {
    public int hubLevel;
    public int balance;
    public String currentLevel;

    public Player() {
        super();
        type = "Player";
        bulletType = "GOOD";
        sprites = PLAYER_TEXTURES;
        animatedObject.setTextures(sprites.get("NORTH"));
        setX(3000);
        setY(3000);
        width = 300;
        height = 300;

        legs.width = width;
        legs.height = height;
    }

    @Override
    public void move(List<Surface> surfaces, List<Entity> entities) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            bulletAim = saveBulletAim;
            List<Entity> enemies = new ArrayList<>();
            entities.stream().filter(entity -> entity.type.contains("Enemy")).forEach(enemies::add);
            float distance = 999999999;
            for (Entity enemy : enemies) {
                float thisDistance = (float) Math.abs(Math.sqrt(
                        Math.abs(x - enemy.x) * Math.abs(x - enemy.x) +
                                Math.abs(y - enemy.y) * Math.abs(y - enemy.y)));
                if (thisDistance < distance) {
                    distance = thisDistance;
                }
            }

            Entity entity = new ShortAttackEnemy();

            for (Entity enemy : enemies) {
                float thisDistance = (float) Math.abs(Math.sqrt(
                        Math.abs(x - enemy.x) * Math.abs(x - enemy.x) +
                                Math.abs(y - enemy.y) * Math.abs(y - enemy.y)));
                if (thisDistance == distance) {
                    entity = enemy;
                    break;
                }
            }

            if (entity.equals(new ShortAttackEnemy()) || distance > 3000) {
                bulletAim = "direction";
            }

            shot(entity);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = gravitated ? "LEFT" : "WEST";
            super.move(surfaces, entities);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = gravitated ? "RIGHT" : "EAST";
            super.move(surfaces, entities);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && gravitated) {
            jump(surfaces, entities);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && !gravitated) {
            direction = "NORTH";
            super.move(surfaces, entities);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !gravitated) {
            direction = "SOUTH";
            super.move(surfaces, entities);
        }
    }
}
