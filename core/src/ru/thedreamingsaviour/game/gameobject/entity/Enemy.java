package ru.thedreamingsaviour.game.gameobject.entity;

import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.entity.part.Cell;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends Entity {
    public Rectangle fieldOfView = new Rectangle();
    public final List<Cell> cellsOfView = new ArrayList<>();

    public void moveToPlayer(Player player, List<Surface> surfaceList, List<Entity> entities, int countRenders) {
    }

    public void attack(Player player) {
    }

    public void sightCalibration() {
    }
}