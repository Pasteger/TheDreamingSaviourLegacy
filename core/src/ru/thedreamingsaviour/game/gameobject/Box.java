package ru.thedreamingsaviour.game.gameobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.character.Character;

import java.util.List;

public class Box extends Rectangle {
    public int HP;
    public final AnimatedObject textures;
    public final Rectangle legs = new Rectangle();
    public boolean gravitated;
    public int timeFall;
    public long deltaTime;
    public boolean northBlocked;
    public boolean southBlocked;
    public boolean eastBlocked;
    public boolean westBlocked;
    private long blockTime;

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

    public void move(Character summoner, List<Surface> surfaces, List<Character> characters, List<Box> boxes){
        if (System.currentTimeMillis() - blockTime > 1000) {
            northBlocked = false;
            southBlocked = false;
            eastBlocked = false;
            westBlocked = false;
        }
        for (int step = 0; step < summoner.getSpeed(); step++) {
            switch (summoner.direction) {
                case "NORTH" -> legs.y++;
                case "SOUTH" -> legs.y--;
                case "EAST", "RIGHT" -> legs.x++;
                case "WEST", "LEFT" -> legs.x--;
            }
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    backLegs(summoner);
                    blockDirection(summoner);
                    break;
                }
            }
            for (Character character : characters) {
                if (legs.overlaps(character) && !character.equals(summoner)) {
                    backLegs(summoner);
                    blockDirection(summoner);
                    break;
                }
            }
            for (Box box : boxes) {
                if (legs.overlaps(box) && !legs.equals(box.legs)) {
                    backLegs(summoner);
                    blockDirection(summoner);
                    break;
                }
            }
        }
        y = legs.y;
        x = legs.x;
    }

    private void blockDirection(Character summoner){
        switch (summoner.direction) {
            case "NORTH" -> northBlocked = true;
            case "SOUTH" -> southBlocked = true;
            case "EAST", "RIGHT" -> eastBlocked = true;
            case "WEST", "LEFT" -> westBlocked = true;
        }
        blockTime = System.currentTimeMillis();
    }

    private void backLegs(Character summoner){
        switch (summoner.direction) {
            case "NORTH" -> legs.y--;
            case "SOUTH" -> legs.y++;
            case "EAST", "RIGHT" -> legs.x--;
            case "WEST", "LEFT" -> legs.x++;
        }
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
