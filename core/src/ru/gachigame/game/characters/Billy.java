package ru.gachigame.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import ru.gachigame.game.characters.parts.Cum;
import ru.gachigame.game.characters.parts.CumArray;

public class Billy extends Rectangle {
    private final Array<Texture> billySprites = new Array<>();
    public byte HP;
    public String direction = "NORTH";
    public Texture texture;

    public Billy(){
        billySprites.add(
                new Texture("sprites/billy_up_sprite.png"),
                new Texture("sprites/billy_down_sprite.png"),
                new Texture("sprites/billy_right_sprite.png"),
                new Texture("sprites/billy_left_sprite.png")
        );
        texture = billySprites.get(0);
        HP = 4;
        x = 914;
        y = 10;
        width = 8;
        height = 8;
    }

    public void move(){
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            texture = billySprites.get(2);
            x += 2;
            direction = "EAST";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            texture = billySprites.get(3);
            x -= 2;
            direction = "WEST";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            texture = billySprites.get(0);
            y += 2;
            direction = "NORTH";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            texture = billySprites.get(1);
            y -= 2;
            direction = "SOUTH";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            shot();
        }
    }

    public void shot(){
        Cum cum = new Cum();
        switch (direction){
            case "NORTH":
                cum.x = x+2;
                cum.y = y+6;
                break;
            case "SOUTH":
                cum.x = x+2;
                cum.y = y;
                break;
            case "WEST":
                cum.x = x+1;
                cum.y = y+2;
                break;
            case "EAST":
                cum.x = x+6;
                cum.y = y+2;
                break;
        }
        cum.height = 2;
        cum.width = 2;
        cum.direction = direction;
        cum.type = "GOOD";
        cum.texture = new Texture("sprites/cum.png");
        CumArray.cumArray.add(cum);
    }
}
