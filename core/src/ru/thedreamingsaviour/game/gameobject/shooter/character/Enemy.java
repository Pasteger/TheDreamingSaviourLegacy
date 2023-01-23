package ru.thedreamingsaviour.game.gameobject.shooter.character;

import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.Surface;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends Character {
    public String type = "enemy";
    public Cell fieldOfView = new Cell();
    public final List<Cell> cellsOfView = new ArrayList<>();
    public void moveToPlayer(Ilya ilya, List<Surface> surfaceList, List<Enemy> enemies){}
    public void attack(Ilya ilya){}
    public void sightCalibration(){}
}
