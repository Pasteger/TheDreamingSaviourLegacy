package ru.thedreamingsaviour.game.gameobject.entity;

import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.entity.part.Cell;

import java.util.ArrayList;
import java.util.List;

import static ru.thedreamingsaviour.game.resourceloader.SoundLoader.getAgrShortAttackEnemy;
import static ru.thedreamingsaviour.game.resourceloader.SoundLoader.getAgrShotAttackEnemy;

public abstract class Enemy extends Entity {
    Cell targetCell;
    byte recharge;
    public Rectangle fieldOfView = new Rectangle();
    public final List<Cell> cellsOfView = new ArrayList<>();

    public void moveToPlayer(Player player, List<Surface> surfaceList, List<Entity> entities, int countRenders) {
        isMoved = false;
        fieldOfView.x = x - fieldOfView.width / 2;
        fieldOfView.y = y - fieldOfView.height / 2;
        if (System.currentTimeMillis() - timeAgr > 1000) {
            isAgr = false;
        }
        if (fieldOfView.overlaps(player)) {
            if (!isAgr) {
                switch (type) {
                    case "ShortAttackEnemy" -> getAgrShortAttackEnemy().play(0.2f);
                    case "ShotAttackEnemy" -> getAgrShotAttackEnemy().play(0.7f);
                }
                isAgr = true;
            }
            if (targetCell == null || countRenders % 10 == 0) {
                targetCell = findWay(player, surfaceList);
            }
            if (targetCell.x > x && !this.overlaps(player)) {
                direction = gravitated ? "RIGHT" : "EAST";
                isMoved = true;
                move(surfaceList, entities);
            }
            if (targetCell.x < x && !this.overlaps(player)) {
                direction = gravitated ? "LEFT" : "WEST";
                isMoved = true;
                move(surfaceList, entities);
            }
            if (targetCell.y > y && !this.overlaps(player)) {
                if (gravitated) {
                    jump(surfaceList, entities);
                } else {
                    direction = "NORTH";
                    isMoved = true;
                    move(surfaceList, entities);
                }
            }
            if (targetCell.y < y && !this.overlaps(player) && !gravitated) {
                direction = "SOUTH";
                isMoved = true;
                move(surfaceList, entities);
            }
            timeAgr = System.currentTimeMillis();
        }
        if(!isMoved && jumped == 0 && timeFall == 0) {
            if (gravitated) {
                animatedObject.setTextures(sprites.get("STAND/SIDE"));
            } else {
                animatedObject.setTextures(sprites.get("STAND/TOP"));
            }
        }
        moveCells();
    }

    public void attack(Player player) {
    }

    public void sightCalibration() {
    }

    Cell findWay(Player player, List<Surface> surfaceList) {
        Cell enemyCell = new Cell();
        for (Cell cell : cellsOfView) {
            if (cell.overlaps(player) &&
                    cell.x > player.x && cell.y > player.y &&
                    cell.x - player.x <= cell.width && cell.y - player.y <= cell.height) {
                cell.property = "target";
            }
            if (cell.overlaps(this)) {
                cell.property = "this";
                enemyCell = cell;
            }
            for (Surface surface : surfaceList) {
                if (cell.overlaps(surface) && surface.getEffect().equals("solid")) {
                    cell.property = "impassable";
                }
            }
        }

        int half = (int) Math.sqrt(cellsOfView.size());
        int step = 0;
        do {
            step++;
            for (int i = 0; i < cellsOfView.size(); i++) {
                Cell thisCell = cellsOfView.get(i);
                if (thisCell.property.equals("target") ||
                        thisCell.property.equals("way") ||
                        thisCell.property.equals("preWay")) {
                    try {
                        Cell cell = cellsOfView.get(i - 1);
                        if (thisCell.y == cell.y && paintCell(thisCell, cell)) {
                            return thisCell;
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        Cell cell = cellsOfView.get(i + 1);
                        if (thisCell.y == cell.y && paintCell(thisCell, cell)) {
                            return thisCell;
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        Cell cell = cellsOfView.get(i + half);
                        if (thisCell.x == cell.x && paintCell(thisCell, cell)) {
                            return thisCell;
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        Cell cell = cellsOfView.get(i - half);
                        if (thisCell.x == cell.x && paintCell(thisCell, cell)) {
                            return thisCell;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        } while (step < Math.sqrt(cellsOfView.size()));
        return enemyCell;
    }

    boolean paintCell(Cell thisCell, Cell cell) {
        if (cell.property.equals("this")) {
            return true;
        }
        if ((thisCell.property.equals("way") || thisCell.property.equals("target")) &&
                cell.property.equals("preWay")) {
            cell.property = "way";
        }
        if ((thisCell.property.equals("way") || thisCell.property.equals("target")) &&
                cell.property.equals("")) {
            cell.property = "preWay";
        }
        return false;
    }

    void generateCells() {
        int squareSpace = (int) ((fieldOfView.width * fieldOfView.width) / ((width / 2) * (width / 2)));
        int row = (int) Math.sqrt(squareSpace);
        if (row < Math.sqrt(squareSpace)) {
            row++;
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                Cell cell = new Cell();
                cell.x = fieldOfView.x + (width / 2 * i);
                cell.y = fieldOfView.y + (height / 2 * j);
                cell.width = width / 2;
                cell.height = height / 2;
                cellsOfView.add(cell);
            }
        }
    }

    void moveCells() {
        int n = 0;
        for (int i = 0; i < Math.sqrt(cellsOfView.size()); i++) {
            for (int j = 0; j < Math.sqrt(cellsOfView.size()); j++) {
                cellsOfView.get(n).x = fieldOfView.x + (width / 2 * j);
                cellsOfView.get(n).y = fieldOfView.y + (width / 2 * i);
                cellsOfView.get(n).property = "";
                n++;
            }
        }
    }
    @Override
    public Rectangle setX(float x) {
        this.x = x;
        legs.x = x;
        fieldOfView.x = x - fieldOfView.width / 2;
        return this;
    }

    @Override
    public Rectangle setY(float y) {
        this.y = y;
        legs.y = y;
        fieldOfView.y = y - fieldOfView.height / 2;
        return this;
    }
}
