package ru.gachigame.game.shooter.gameobject.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class Billy extends Character {
    public Billy(){
        sprites = SHOOTER_BILLY_TEXTURES;

        texture = sprites.get(UP);
        HP = 4;
        x = 914;
        y = 10;
        width = 8;
        height = 8;
    }

    public void move(){
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            texture = sprites.get(RIGHT);
            x += 2;
            direction = "EAST";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            texture = sprites.get(LEFT);
            x -= 2;
            direction = "WEST";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            texture = sprites.get(UP);
            y += 2;
            direction = "NORTH";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            texture = sprites.get(DOWN);
            y -= 2;
            direction = "SOUTH";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            shot("GOOD");
        }
    }
}
