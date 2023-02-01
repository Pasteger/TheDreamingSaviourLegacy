package ru.thedreamingsaviour.game.gameobject.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Box;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.resourceloader.TextureLoader;

import java.util.ArrayList;
import java.util.List;

public class Ilya extends Character {
    private final int saveSpeed;
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
        saveSpeed = 20;
        speed = saveSpeed;

        legs.width = width;
        legs.height = height;
    }

    public void move(List<Surface> surfaces, List<Enemy> enemies, List<Box> boxes) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            shot("GOOD");
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = gravitated ? "LEFT" : "WEST";
            moveRender(surfaces, enemies, boxes);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = gravitated ? "RIGHT" : "EAST";
            moveRender(surfaces, enemies, boxes);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && gravitated) {
            jump(surfaces);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && !gravitated) {
            direction = "NORTH";
            moveRender(surfaces, enemies, boxes);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !gravitated) {
            direction = "SOUTH";
            moveRender(surfaces, enemies, boxes);
        }
    }


    private void moveRender(List<Surface> surfaces, List<Enemy> enemies, List<Box> boxes) {
        speed = 20;
        List<Character> characters = new ArrayList<>();
        characters.add(this);
        characters.addAll(enemies);

        switch (direction) {
            case "NORTH" -> texture = sprites.get(UP);
            case "SOUTH" -> texture = sprites.get(DOWN);
            case "EAST" -> texture = sprites.get(RIGHT);
            case "WEST" -> texture = sprites.get(LEFT);
            case "RIGHT" -> texture = sprites.get("rightSpriteP");
            case "LEFT" -> texture = sprites.get("leftSpriteP");
        }

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
            if (legs.overlaps(box)) {
                box.move(this, surfaces, characters, boxes);
                backLegsDirection(box);
            }
        }
        for (Enemy enemy : enemies) {
            if (legs.overlaps(enemy.legs)) {
                backLegsSpeed();
                break;
            }
        }
        y = legs.y;
        x = legs.x;
    }

    private void backLegs() {
        switch (direction) {
            case "NORTH" -> legs.y--;
            case "SOUTH" -> legs.y++;
            case "EAST", "RIGHT" -> legs.x--;
            case "WEST", "LEFT" -> legs.x++;
        }
    }

    private void backLegsDirection(Box box) {
        if (box.northBlocked && direction.equals("NORTH")) legs.y -= speed;
        if (box.southBlocked && direction.equals("SOUTH")) legs.y += speed;
        if (box.eastBlocked && direction.equals("EAST")) legs.x -= speed;
        if (box.westBlocked && direction.equals("WEST")) legs.x += speed;
    }

    private void backLegsSpeed() {
        switch (direction) {
            case "NORTH" -> legs.y -= speed;
            case "SOUTH" -> legs.y += speed;
            case "EAST", "RIGHT" -> legs.x -= speed;
            case "WEST", "LEFT" -> legs.x += speed;
        }
    }
}
