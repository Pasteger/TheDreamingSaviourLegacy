package ru.gachigame.game.gameobject.shooter.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.gameobject.Surface;
import java.util.List;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class Billy extends Character {
    public Billy(){
        legs = new Rectangle();
        sprites = getShooterBullyTextures();

        texture = sprites.get(UP);
        HP = 4;
        setX(914);
        setY(10);
        width = 8;
        height = 8;

        legs.width = width;
        legs.height = height;
    }

    public void move(List<Surface> surfaces, List<Slave> slaves){
        speed = 2;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            moveRight(surfaces, slaves);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            moveLeft(surfaces, slaves);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            moveUp(surfaces, slaves);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            moveDown(surfaces, slaves);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            shot("GOOD");
        }
    }
}
