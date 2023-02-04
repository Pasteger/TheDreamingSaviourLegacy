package ru.thedreamingsaviour.game.gameobject.character;

import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.character.part.Cell;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.SHORT_ATTACK_ENEMY;

public class ShortAttackEnemy extends Enemy {
    private byte recharge;
    private Cell targetCell;

    public ShortAttackEnemy() {
        type = "ShortAttackEnemy";
        sprites = SHORT_ATTACK_ENEMY;
        sprite.setTextures(sprites.get("NORTH"));
        width = 300;
        height = 300;
        HP = 3;
        recharge = 20;
        fieldOfView.width = 4000;
        fieldOfView.height = 4000;
        fieldOfView.x = x - fieldOfView.width / 2;
        fieldOfView.y = y - fieldOfView.height / 2;

        generateCells();

        saveSpeed = 10;
        speed = saveSpeed;
        type = "ShortAttackEnemy";

        legs.width = width;
        legs.height = height;
    }

    @Override
    public void attack(Player player) {
        if (Math.abs(x - player.x) < width + speed && Math.abs(y - player.y) <= height + speed) {
            recharge--;
            if (recharge <= 0) {
                recharge = (byte) ((byte) 5 + random.nextInt(20));
                player.HP--;
            }
        }
    }

    @Override
    public void moveToPlayer(Player player, List<Surface> surfaceList, List<Entity> entities, int countRenders) {
        fieldOfView.x = x - fieldOfView.width / 2;
        fieldOfView.y = y - fieldOfView.height / 2;
        if (fieldOfView.overlaps(player)) {
            if (targetCell == null || countRenders % 10 == 0) {
                targetCell = findWay(player, surfaceList);
            }
            if (targetCell.x > x && !this.overlaps(player)) {
                direction = gravitated ? "RIGHT" : "EAST";
                move(surfaceList, entities);
            }
            if (targetCell.x < x && !this.overlaps(player)) {
                direction = gravitated ? "LEFT" : "WEST";
                move(surfaceList, entities);
            }
            if (targetCell.y > y && !this.overlaps(player)) {
                if (gravitated) {
                    jump(surfaceList, entities);
                } else {
                    direction = "NORTH";
                    move(surfaceList, entities);
                }
            }
            if (targetCell.y < y && !this.overlaps(player) && !gravitated) {
                direction = "SOUTH";
                move(surfaceList, entities);
            }
        }
        moveCells();
    }

    private Cell findWay(Player player, List<Surface> surfaceList) {
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

    private boolean paintCell(Cell thisCell, Cell cell) {
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

    private void generateCells() {
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

    private void moveCells() {
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
        moveCells();
        return this;
    }

    @Override
    public Rectangle setY(float y) {
        this.y = y;
        legs.y = y;
        fieldOfView.y = y - fieldOfView.height / 2;
        moveCells();
        return this;
    }
}
