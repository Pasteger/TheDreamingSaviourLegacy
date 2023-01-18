package ru.gachigame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.gameobject.Surface;
import ru.gachigame.game.gameobject.shooter.character.Enemy;
import ru.gachigame.game.gameobject.shooter.character.Ilya;
import ru.gachigame.game.gameobject.shooter.character.ShortAttackEnemy;
import ru.gachigame.game.guiobject.TextWindow;
import ru.gachigame.game.resourceloader.LevelSaver;
import ru.gachigame.game.screen.MainMenuScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.gachigame.game.resourceloader.LevelLoader.*;

public class LevelEditor implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    TextWindow textWindow;
    private final Ilya ilya;
    private final List<Enemy> enemyList;
    private final List<Surface> surfaceList;
    private boolean dragged;
    private String currentTask;
    private int deltaX;
    private int deltaY;
    private Surface currentSurface;
    private Enemy currentEnemy;

    public LevelEditor(final MyGdxGame game) {
        this.game = game;
        textWindow = new TextWindow();
        currentTask = "";
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        camera = game.camera;
        camera.setToOrtho(false, 4000, 4000);

        ilya = new Ilya();

        enemyList = getEnemyList();
        surfaceList = getSurfaceList();

        Gdx.input.setInputProcessor(new EditorInputProcessor());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        surfaceList.forEach(surface -> surface.draw(game.batch));

        enemyList.forEach(enemy -> game.batch.draw(enemy.texture, enemy.x, enemy.y));

        game.batch.draw(ilya.texture, ilya.x, ilya.y);

        game.font.draw(game.batch, camera.position.x + "  " + camera.position.y, camera.position.x - 2000, camera.position.y + 1900);
        game.font.draw(game.batch, currentTask, camera.position.x - 2000, camera.position.y + 1750);

        textWindow.render(game);
        game.batch.end();

        if (dragged) {
            camera.position.x += (float) deltaX / 10;
            camera.position.y -= (float) deltaY / 10;
        }
        buttons();
    }

    private void buttons() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            currentTask = !currentTask.equals("spawnSlave") ? "spawnSlave" : "";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            currentTask = !currentTask.equals("deleteSlave") ? "deleteSlave" : "";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            currentTask = !currentTask.equals("generateWall") ? "generateWall" : "";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            currentTask = !currentTask.equals("removeWall") ? "removeWall" : "";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            currentTask = !currentTask.equals("edit") ? "edit" : "";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            currentTask = !currentTask.equals("addFloor") ? "addFloor" : "";
        }

        if (currentTask.equals("addFloor") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Surface floor = new Surface(getSynchronizedX(), getSynchronizedY(),
                    200, 200, "none", "floorTexture");
            surfaceList.add(floor);
            System.out.println("floor added");
        }

        if (currentTask.equals("edit") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
                (currentSurface == null && currentEnemy == null)) {
            setCurrentObject();
        }
        if (currentTask.equals("edit") && Gdx.input.isKeyJustPressed(Input.Keys.ENTER) &&
                (currentSurface != null || currentEnemy != null)) {
            if (currentSurface != null) {
                currentSurface.setStandardTexture();
                currentSurface = null;
            }
            if (currentEnemy != null) {
                currentEnemy = null;
            }
            currentTask = "";
        }

        if (currentTask.equals("spawnSlave") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Enemy slave = new ShortAttackEnemy();
            slave.setX(getSynchronizedX());
            slave.setY(getSynchronizedY());
            enemyList.add(slave);
        }
        if (currentTask.equals("deleteSlave") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float x = getSynchronizedX();
            float y = getSynchronizedY();
            enemyList.removeIf(enemy -> (
                    enemy.getX() >= x - enemy.getWidth() && enemy.getX() <= x + 1 &&
                            enemy.getY() >= y - enemy.getHeight() && enemy.getY() <= y + 1
            ));
        }
        if (currentTask.equals("generateWall") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Surface wall = new Surface(getSynchronizedX(), getSynchronizedY(),
                    200, 200, "solid", "wallTexture");
            surfaceList.add(wall);
        }
        if (currentTask.equals("removeWall") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float y = getSynchronizedY();
            float x = getSynchronizedX();
            surfaceList.removeIf(wall -> (wall.getEffect().equals("solid") &&
                    wall.getX() >= x - wall.getWidth() && wall.getX() <= x + 1 &&
                    wall.getY() >= y - wall.getHeight() && wall.getY() <= y + 1)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            List<ShortAttackEnemy> shortAttackEnemyList = new ArrayList<>();
            enemyList.stream().filter(
                    enemy -> enemy.type.equals("ShortAttackEnemy")).forEach(
                    enemy -> shortAttackEnemyList.add((ShortAttackEnemy) enemy));
            LevelSaver.save(surfaceList, shortAttackEnemyList, "level0", "level0", "shooter");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private float getSynchronizedX() {
        float coefficientX = 4000f / 800f;
        return (camera.position.x + Gdx.input.getX() * coefficientX) - 2000;
    }

    private float getSynchronizedY() {
        float coefficientY = 4000f / 600f;
        return (camera.position.y + Gdx.input.getY() * coefficientY * -1) + 2000;
    }

    private void setCurrentObject() {
        float x = getSynchronizedX();
        float y = getSynchronizedY();
        for (Enemy slave : enemyList) {
            if (findObjectFromCord(slave, x, y)) {
                currentEnemy = slave;
                return;
            }
        }
        for (int i = surfaceList.size() - 1; i >= 0; i--) {
            if (findObjectFromCord(surfaceList.get(i), x, y)) {
                currentSurface = surfaceList.get(i);
                currentSurface.setEditableTexture();
                Collections.swap(surfaceList, surfaceList.indexOf(surfaceList.get(i)), surfaceList.size() - 1);
                return;
            }
        }
    }

    private boolean findObjectFromCord(Rectangle object, float x, float y) {
        return (object.getX() >= x - object.getWidth() && object.getX() <= x + 1 &&
                object.getY() >= y - object.getHeight() && object.getY() <= y + 1);
    }

    class EditorInputProcessor implements InputProcessor {
        private int touchDownX;
        private int touchDownY;
        private boolean extensionCurrentRectangle;
        private boolean moveCurrentRectangle;
        private boolean moveSlave;

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button == 1) {
                touchDownX = screenX;
                touchDownY = screenY;
                dragged = true;
            }
            if (currentSurface != null && !extensionCurrentRectangle &&
                    getSynchronizedX() >= currentSurface.x &&
                    getSynchronizedX() <= currentSurface.x + 100 &&
                    getSynchronizedY() >= currentSurface.y &&
                    getSynchronizedY() <= currentSurface.y + 100) {
                moveCurrentRectangle = true;
            }
            if (currentSurface != null && !moveCurrentRectangle &&
                    getSynchronizedX() >= currentSurface.x + currentSurface.width - 100 &&
                    getSynchronizedX() <= currentSurface.x + currentSurface.width &&
                    getSynchronizedY() >= currentSurface.y + currentSurface.height - 100 &&
                    getSynchronizedY() <= currentSurface.y + currentSurface.height) {
                extensionCurrentRectangle = true;
            }

            if (currentEnemy != null && !moveSlave &&
                    getSynchronizedX() >= currentEnemy.x &&
                    getSynchronizedX() <= currentEnemy.x + currentEnemy.width &&
                    getSynchronizedY() >= currentEnemy.y &&
                    getSynchronizedY() <= currentEnemy.y + currentEnemy.height) {
                moveSlave = true;
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            dragged = false;
            extensionCurrentRectangle = false;
            moveCurrentRectangle = false;
            moveSlave = false;
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            deltaX = touchDownX - screenX;
            deltaY = touchDownY - screenY;
            if (extensionCurrentRectangle) {
                int width = (int) getSynchronizedX() - (int) currentSurface.getX();
                int height = (int) getSynchronizedY() - (int) currentSurface.getY();
                currentSurface.editExtension(width, height);
                currentSurface.setEditableTexture();
            }
            if (moveCurrentRectangle) {
                currentSurface.editMove(getSynchronizedX(), getSynchronizedY());
                currentSurface.setEditableTexture();
            }

            if (moveSlave) {
                currentEnemy.x = getSynchronizedX();
                currentEnemy.y = getSynchronizedY();
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
