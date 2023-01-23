package ru.thedreamingsaviour.game.gameobject.shooter.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.resourceloader.TextureLoader;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class ShortAttackEnemy extends Enemy {
    public byte recharge;

    private final Texture emptyCellTexture = new Texture("emptyCell.png");
    private final Texture impassableCellTexture = new Texture("impassableCell.png");
    private final Texture targetCellTexture = new Texture("targetCell.png");
    private final Texture thisCellTexture = new Texture("thisCell.png");
    private final Texture wayCellTexture = new Texture("wayCell.png");

    public ShortAttackEnemy() {
        legs = new Rectangle();
        sprites = TextureLoader.getShooterShortAttackEnemyTextures();
        texture = sprites.get(UP);
        width = 300;
        height = 300;
        HP = 3;
        recharge = 20;
        fieldOfView.width = 5000;
        fieldOfView.height = 5000;
        fieldOfView.x = x - fieldOfView.width / 2;
        fieldOfView.y = y - fieldOfView.height / 2;

        generateCells();

        speed = 10;
        type = "ShortAttackEnemy";

        legs.width = width;
        legs.height = height;
    }

    public void attack(Ilya ilya) {
        if (this.overlaps(ilya)) {
            recharge--;
            if (recharge <= 0) {
                recharge = (byte) ((byte) 5 + random.nextInt(20));
                ilya.HP--;
            }
        }
    }

    public void moveToPlayer(Ilya ilya, List<Surface> surfaceList, List<Enemy> enemies) {
        fieldOfView.x = x - fieldOfView.width / 2;
        fieldOfView.y = y - fieldOfView.height / 2;
        if (fieldOfView.overlaps(ilya)) {
            Cell cell = findWay(ilya, surfaceList);
            if (cell.x > x) {
                moveRight(surfaceList, enemies);
            }
            if (cell.x < x) {
                moveLeft(surfaceList, enemies);
            }
            if (cell.y > y) {
                moveUp(surfaceList, enemies);
            }
            if (cell.y < y) {
                moveDown(surfaceList, enemies);
            }
        }
        moveCells();
    }

    private Cell findWay(Ilya ilya, List<Surface> surfaceList) {
        Cell enemyCell = new Cell();
        for (Cell cell : cellsOfView) {
            if (cell.overlaps(ilya)) {
                cell.property = "target";
                cell.texture = targetCellTexture;
            }
            if (cell.overlaps(this)) {
                cell.property = "this";
                cell.texture = thisCellTexture;
                enemyCell = cell;
            }
            for (Surface surface : surfaceList) {
                if (cell.overlaps(surface)) {
                    cell.property = "impassable";
                    cell.texture = impassableCellTexture;
                }
            }
        }

        int half = (int) Math.sqrt(cellsOfView.size());
        while (true) {
            int step = 0;
            for (int i = 0; i < cellsOfView.size(); i++) {
                Cell thisCell = cellsOfView.get(i);
                if (thisCell.property.equals("target") || thisCell.property.equals("way")) {
                    //Дальше Бога нет
                    try {
                        Cell cell = cellsOfView.get(i - 1);
                        if (cell.property.equals("this")) {
                            return thisCell;
                        }
                        if (!(cell.property.equals("target") ||
                                cell.property.equals("way") ||
                                cell.property.equals("impassable"))) {
                            cell.property = "way";
                            cell.texture = wayCellTexture;
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        Cell cell = cellsOfView.get(i + 1);
                        if (cell.property.equals("this")) {
                            return thisCell;
                        }
                        if (!(cell.property.equals("target") ||
                                cell.property.equals("way") ||
                                cell.property.equals("impassable"))) {
                            cell.property = "way";
                            cell.texture = wayCellTexture;
                        }
                    } catch (Exception ignored) {
                    }
                    /*try {
                        Cell cell = cellsOfView.get(i + half - 1);
                        if (cell.property.equals("this")) {
                            return thisCell;
                        }
                        if (!(cell.property.equals("target") ||
                                cell.property.equals("way") ||
                                cell.property.equals("impassable"))) {
                            cell.property = "way";
                            cell.texture = wayCellTexture;
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        Cell cell = cellsOfView.get(i + half + 1);
                        if (cell.property.equals("this")) {
                            return thisCell;
                        }
                        if (!(cell.property.equals("target") ||
                                cell.property.equals("way") ||
                                cell.property.equals("impassable"))) {
                            cell.property = "way";
                            cell.texture = wayCellTexture;
                        }
                    } catch (Exception ignored) {
                    }*/
                    try {
                        Cell cell = cellsOfView.get(i + half);
                        if (cell.property.equals("this")) {
                            return thisCell;
                        }
                        if (!(cell.property.equals("target") ||
                                cell.property.equals("way") ||
                                cell.property.equals("impassable"))) {
                            cell.property = "way";
                            cell.texture = wayCellTexture;
                        }
                    } catch (Exception ignored) {
                    }
                    /*try {
                        Cell cell = cellsOfView.get(i - half - 1);
                        if (cell.property.equals("this")) {
                            return thisCell;
                        }
                        if (!(cell.property.equals("target") ||
                                cell.property.equals("way") ||
                                cell.property.equals("impassable"))) {
                            cell.property = "way";
                            cell.texture = wayCellTexture;
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        Cell cell = cellsOfView.get(i - half + 1);
                        if (cell.property.equals("this")) {
                            return thisCell;
                        }
                        if (!(cell.property.equals("target") ||
                                cell.property.equals("way") ||
                                cell.property.equals("impassable"))) {
                            cell.property = "way";
                            cell.texture = wayCellTexture;
                        }
                    } catch (Exception ignored) {
                    }*/
                    try {
                        Cell cell = cellsOfView.get(i - half);
                        if (cell.property.equals("this")) {
                            return thisCell;
                        }
                        if (!(cell.property.equals("target") ||
                                cell.property.equals("way") ||
                                cell.property.equals("impassable"))) {
                            cell.property = "way";
                            cell.texture = wayCellTexture;
                        }
                    } catch (Exception ignored) {
                    }
                    //Только лягушечка
                    step++;
                }
            }
            if (step == 0) {
                break;
            }
        }
        return enemyCell;
    }

    private void generateCells() {
        int squareSpace = (int) ((fieldOfView.width * fieldOfView.width) / (width * width));
        int row = (int) Math.sqrt(squareSpace);
        if (row < Math.sqrt(squareSpace)) {
            row++;
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                Cell cell = new Cell();
                cell.x = fieldOfView.x + (width * i);
                cell.y = fieldOfView.y + (height * j);
                cell.width = width;
                cell.height = height;
                cell.texture = emptyCellTexture;
                cellsOfView.add(cell);
            }
        }
    }

    private void moveCells() {
        int n = 0;
        for (int i = 0; i < Math.sqrt(cellsOfView.size()); i++) {
            for (int j = 0; j < Math.sqrt(cellsOfView.size()); j++) {
                cellsOfView.get(n).x = fieldOfView.x + (width * j);
                cellsOfView.get(n).y = fieldOfView.y + (width * i);
                cellsOfView.get(n).property = "";
                //cellsOfView.get(n).texture = emptyCellTexture;
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
