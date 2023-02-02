package ru.thedreamingsaviour.game.gameobject.character;

import ru.thedreamingsaviour.game.gameobject.Surface;

import java.util.List;

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.BOX;

public class Box extends Entity {
    public boolean northBlocked;
    public boolean southBlocked;
    public boolean eastBlocked;
    public boolean westBlocked;
    private long blockTime;

    public Box(float x, float y, int width, int height, String texturesKey, int hp) {
        type = "Box";
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        sprite.setTextures(BOX.get(texturesKey));
        HP = (byte) hp;

        legs.x = x;
        legs.y = y;
        legs.width = width;
        legs.height = height;
    }

    @Override
    public void moveBox(Entity summoner, List<Surface> surfaces, List<Entity> entities) {
        if (System.currentTimeMillis() - blockTime > 1000) {
            northBlocked = false;
            southBlocked = false;
            eastBlocked = false;
            westBlocked = false;
        }
        speed = summoner.getSpeed();
        direction = summoner.direction;
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
                    blockDirection();
                    break;
                }
            }
            for (Entity entity : entities) {
                if (entity.type.equals("Box") && legs.overlaps(entity) && !legs.equals(entity.legs)) {
                    entity.moveBox(this, surfaces, entities);
                    backLegs();
                    blockDirection();
                    break;
                }
                if (!entity.equals(this) && legs.overlaps(entity) && !entity.equals(summoner)) {
                    backLegs();
                    blockDirection();
                    break;
                }
            }
        }
        y = legs.y;
        x = legs.x;
    }

    private void blockDirection() {
        switch (direction) {
            case "NORTH" -> northBlocked = true;
            case "SOUTH" -> southBlocked = true;
            case "EAST", "RIGHT" -> eastBlocked = true;
            case "WEST", "LEFT" -> westBlocked = true;
        }
        blockTime = System.currentTimeMillis();
    }
}
