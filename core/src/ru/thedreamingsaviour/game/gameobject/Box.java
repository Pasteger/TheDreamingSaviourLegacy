package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.character.Character;

import java.util.List;

public class Box extends Rectangle {
    public int HP;
    public final AnimatedObject textures;
    private final Rectangle legs = new Rectangle();
    public boolean gravitated;
    public int timeFall;
    public long deltaTime;
    public boolean blocked;

    public Box(float x, float y, int width, int height, List<Texture> textures, int hp){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textures = new AnimatedObject(textures);
        this.HP = hp;

        legs.x = x;
        legs.y = y;
        legs.width = width;
        legs.height = height;
    }

    public void move(Character character, List<Surface> surfaces, List<Character> characters, List<Box> boxes){
        switch (character.direction) {
            case "NORTH" -> moveUp(character, surfaces, characters, boxes);
            case "SOUTH" -> moveDown(character, surfaces, characters, boxes);
            case "EAST", "RIGHT" -> moveRight(character, surfaces, characters, boxes);
            case "WEST", "LEFT" -> moveLeft(character, surfaces, characters, boxes);
        }
    }

    public void moveUp(Character summoner, List<Surface> surfaces, List<Character> characters, List<Box> boxes) {
        for (int step = 0; step < summoner.getSpeed(); step++) {
            legs.y++;
            blocked = false;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.y--;
                    blocked = true;
                    break;
                }
            }
            for (Character character : characters) {
                if (legs.overlaps(character) && !character.equals(summoner)) {
                    legs.y--;
                    blocked = true;
                    break;
                }
            }
            for (Box box : boxes) {
                if (legs.overlaps(box) && !legs.equals(box.legs)) {
                    legs.y--;
                    blocked = true;
                    break;
                }
            }
        }
        y = legs.y;
    }

    public void moveDown(Character summoner, List<Surface> surfaces, List<Character> characters, List<Box> boxes) {
        for (int step = 0; step < summoner.getSpeed(); step++) {
            legs.y--;
            blocked = false;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.y++;
                    blocked = true;
                    break;
                }
            }
            for (Character character : characters) {
                if (legs.overlaps(character) && !character.equals(summoner)) {
                    legs.y++;
                    blocked = true;
                    break;
                }
            }
            for (Box box : boxes) {
                if (legs.overlaps(box) && !legs.equals(box.legs)) {
                    legs.y++;
                    blocked = true;
                    break;
                }
            }
        }
        y = legs.y;
    }

    public void moveRight(Character summoner, List<Surface> surfaces, List<Character> characters, List<Box> boxes) {
        for (int step = 0; step < summoner.getSpeed(); step++) {
            legs.x++;
            blocked = false;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.x--;
                    blocked = true;
                    break;
                }
            }
            for (Character character : characters) {
                if (legs.overlaps(character) && !character.equals(summoner)) {
                    legs.x--;
                    blocked = true;
                    break;
                }
            }
            for (Box box : boxes) {
                if (legs.overlaps(box) && !legs.equals(box.legs)) {
                    legs.x--;
                    blocked = true;
                    break;
                }
            }
        }
        x = legs.x;
    }

    public void moveLeft(Character summoner, List<Surface> surfaces, List<Character> characters, List<Box> boxes) {
        for (int step = 0; step < summoner.getSpeed(); step++) {
            legs.x--;
            blocked = false;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.x++;
                    blocked = true;
                    break;
                }
            }
            for (Character character : characters) {
                if (legs.overlaps(character) && !character.equals(summoner)) {
                    legs.x++;
                    blocked = true;
                    break;
                }
            }
            for (Box box : boxes) {
                if (legs.overlaps(box) && !legs.equals(box.legs)) {
                    legs.x++;
                    blocked = true;
                    break;
                }
            }
        }
        x = legs.x;
    }


    public void fall(List<Surface> surfaces, List<Character> characters, List<Box> boxes) {
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
                for (Character character : characters) {
                    if (legs.overlaps(character)) {
                        legs.y++;
                        timeFall = 0;
                        break;
                    }
                }
                for (Box box : boxes) {
                    if (legs.overlaps(box) && !legs.equals(box)) {
                        legs.y++;
                        timeFall = 0;
                        break;
                    }
                }
            }
            y = legs.y;
        }
    }
}
