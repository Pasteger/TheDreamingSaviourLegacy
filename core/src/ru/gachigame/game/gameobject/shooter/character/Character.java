package ru.gachigame.game.gameobject.shooter.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.gameobject.Surface;
import ru.gachigame.game.gameobject.Cum;
import ru.gachigame.game.logics.ShooterLevelsLogic;

import java.util.List;
import java.util.Map;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

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

    public void moveUp(List<Surface> surfaces, List<Slave> slaves){
        texture = sprites.get(UP);
        direction = "NORTH";
        for (int step = 0; step < speed; step++){
            legs.y++;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.y--;
                    break;
                }
            }
            for (Slave slave : slaves) {
                if (legs.overlaps(slave.legs) && !legs.equals(slave.legs)) {
                    legs.y--;
                    break;
                }
            }
        }
        y = legs.y;
    }
    public void moveDown(List<Surface> surfaces, List<Slave> slaves){
        texture = sprites.get(DOWN);
        direction = "SOUTH";
        for (int step = 0; step < speed; step++){
            legs.y--;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.y++;
                    break;
                }
            }
            for (Slave slave : slaves) {
                if (legs.overlaps(slave.legs) && !legs.equals(slave.legs)) {
                    legs.y++;
                    break;
                }
            }
        }
        y = legs.y;
    }
    public void moveRight(List<Surface> surfaces, List<Slave> slaves){
        texture = sprites.get(RIGHT);
        direction = "EAST";
        for (int step = 0; step < speed; step++){
            legs.x++;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")) {
                    legs.x--;
                    break;
                }
            }
            for (Slave slave : slaves) {
                if (legs.overlaps(slave.legs) && !legs.equals(slave.legs)) {
                    legs.x--;
                    break;
                }
            }
        }
        x = legs.x;
    }
    public void moveLeft(List<Surface> surfaces, List<Slave> slaves){
        texture = sprites.get(LEFT);
        direction = "WEST";
        for (int step = 0; step < speed; step++){
            legs.x--;
            for (Surface surface : surfaces) {
                if (legs.overlaps(surface) && surface.getEffect().equals("solid")){
                    legs.x++;
                    break;
                }
            }
            for (Slave slave : slaves) {
                if (legs.overlaps(slave.legs) && !legs.equals(slave.legs)) {
                    legs.x++;
                    break;
                }
            }
        }
        x = legs.x;
    }

    public void shot(String cumType){
        Cum cum = new Cum();
        switch (direction) {
            case "NORTH" -> {
                cum.x = x + 2;
                cum.y = y + 6;
            }
            case "SOUTH" -> {
                cum.x = x + 2;
                cum.y = y;
            }
            case "WEST" -> {
                cum.x = x + 1;
                cum.y = y + 2;
            }
            case "EAST" -> {
                cum.x = x + 6;
                cum.y = y + 2;
            }
        }
        cum.height = 2;
        cum.width = 2;
        cum.direction = direction;
        cum.type = cumType;
        switch (cumType){
            case "GOOD" -> cum.texture = getCumTexture();
            case "BAD" -> cum.texture = getBadCumTexture();
        }
        ShooterLevelsLogic.cumList.add(cum);
    }

    @Override
    public Rectangle setX(float x){
        this.x = x;
        legs.x = x;
        return this;
    }
    @Override
    public Rectangle setY(float y){
        this.y = y;
        legs.y = y;
        return this;
    }
}
