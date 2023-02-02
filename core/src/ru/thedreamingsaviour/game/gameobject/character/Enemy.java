package ru.thedreamingsaviour.game.gameobject.character;

import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.character.part.Cell;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends Entity {
    public Rectangle fieldOfView = new Rectangle();
    public final List<Cell> cellsOfView = new ArrayList<>();
    public void moveToPlayer(Ilya ilya, List<Surface> surfaceList, List<Enemy> enemies, List<Box> boxes){}
    public void attack(Ilya ilya){}
    public void sightCalibration(){}
}
