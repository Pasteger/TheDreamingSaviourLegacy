package ru.thedreamingsaviour.game.gameobject.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.thedreamingsaviour.game.gameobject.Surface;

import java.util.List;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.PLAYER;

public class Player extends Entity {
    public Player() {
        super();
        type = "Player";
        sprites = PLAYER;
        animatedObject.setTextures(sprites.get("NORTH"));
        HP = 4;
        setX(3000);
        setY(3000);
        width = 300;
        height = 300;
        saveSpeed = 20;
        speed = saveSpeed;

        legs.width = width;
        legs.height = height;
    }

    @Override
    public void move(List<Surface> surfaces, List<Entity> entities) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            shot("GOOD");
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
