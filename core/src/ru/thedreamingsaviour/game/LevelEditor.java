package ru.thedreamingsaviour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.character.Enemy;
import ru.thedreamingsaviour.game.gameobject.character.Ilya;
import ru.thedreamingsaviour.game.gameobject.character.ShortAttackEnemy;
import ru.thedreamingsaviour.game.guiobject.TextWindow;
import ru.thedreamingsaviour.game.resourceloader.LevelSaver;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelEditor implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final TextWindow saveTextWindow;
    private final TextWindow colorTextWindow;
    private final TextWindow effectTextWindow;
    private final Ilya ilya;
    private final List<Enemy> enemyList;
    private final List<Surface> surfaceList;
    private boolean dragged;
    private String currentTask;
    private int deltaX;
    private int deltaY;
    private Surface currentSurface;
    private Enemy currentEnemy;
    private String drawingSurfaceColor;
    private String drawingSurfaceEffect;
    private final Surface demoDrawingSurface;

    public LevelEditor(final MyGdxGame game) {
        this.game = game;
        saveTextWindow = new TextWindow();
        colorTextWindow = new TextWindow();
        effectTextWindow = new TextWindow();
        currentTask = "";
        drawingSurfaceColor = "0.5;0.5;0.5;1";
        drawingSurfaceEffect = "color";
        demoDrawingSurface = new Surface(0, 0, 100, 100, drawingSurfaceEffect, drawingSurfaceColor);
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        camera = game.camera;
        camera.setToOrtho(false, 4000, 4000);

        ilya = new Ilya();

        enemyList = LevelLoader.getEnemyList();
        surfaceList = LevelLoader.getSurfaceList();

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

        game.universalFont.draw(game.batch, camera.position.x + "  " + camera.position.y, camera.position.x - 2000, camera.position.y + 1900);
        game.universalFont.draw(game.batch, currentTask, camera.position.x - 2000, camera.position.y + 1700);
        demoDrawingSurface.setX(camera.position.x - 2000);
        demoDrawingSurface.setY(camera.position.y + 1400);
        demoDrawingSurface.draw(game.batch);

        saveTextWindow.render(game);
        colorTextWindow.render(game);
        effectTextWindow.render(game);
        game.batch.end();

        if (dragged) {
            camera.position.x += (float) deltaX / 10;
            camera.position.y -= (float) deltaY / 10;
        }
        if (!(saveTextWindow.isRendering() || colorTextWindow.isRendering() || effectTextWindow.isRendering())) {
            buttons();
        }
        save();
        changeColor();
        changeEffect();
    }

    private void buttons() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            currentTask = !currentTask.equals("addDrawingSurface") ? "addDrawingSurface" : "";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            currentTask = !currentTask.equals("spawnEnemy") ? "spawnEnemy" : "";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            currentTask = !currentTask.equals("deleteEnemy") ? "deleteEnemy" : "";
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            currentTask = "change color...";
            colorTextWindow.call((int) camera.position.x - 1000, (int) camera.position.y, 2000, 400, "Color");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            currentTask = "change effect...";
            effectTextWindow.call((int) camera.position.x - 1000, (int) camera.position.y, 2000, 400, "Effect");
        }

        if (currentTask.equals("addDrawingSurface") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Surface drawingSurface = new Surface(getSynchronizedX(), getSynchronizedY(),
                    200, 200, drawingSurfaceEffect, drawingSurfaceColor);
            surfaceList.add(drawingSurface);
            System.out.println("drawing surface added");
        }

        if (currentTask.equals("addFloor") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Surface floor = new Surface(getSynchronizedX(), getSynchronizedY(),
                    200, 200, "none", "1;0.8902;0.6941;1");
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
                currentSurface.setStandardColor();
                currentSurface = null;
            }
            if (currentEnemy != null) {
                currentEnemy = null;
            }
            currentTask = "";
        }

        if (currentTask.equals("spawnEnemy") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Enemy slave = new ShortAttackEnemy();
            slave.setX(getSynchronizedX());
            slave.setY(getSynchronizedY());
            enemyList.add(slave);
        }
        if (currentTask.equals("deleteEnemy") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float x = getSynchronizedX();
            float y = getSynchronizedY();
            enemyList.removeIf(enemy -> (
                    enemy.getX() >= x - enemy.getWidth() && enemy.getX() <= x + 1 &&
                            enemy.getY() >= y - enemy.getHeight() && enemy.getY() <= y + 1
            ));
        }
        if (currentTask.equals("generateWall") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Surface wall = new Surface(getSynchronizedX(), getSynchronizedY(),
                    200, 200, "solid", "0;0;0;1");
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
            currentTask = "save...";
            saveTextWindow.call((int) camera.position.x - 1000, (int) camera.position.y, 2000, 400, "levelName nextLevel");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void save() {
        String text = saveTextWindow.getOutputText();
        if (!(text.equals("") || text.equals("new"))) {
            try {
                List<ShortAttackEnemy> shortAttackEnemyList = new ArrayList<>();
                enemyList.stream().filter(
                        enemy -> enemy.type.equals("ShortAttackEnemy")).forEach(
                        enemy -> shortAttackEnemyList.add((ShortAttackEnemy) enemy));

                String[] save = text.split(" ");

                LevelSaver.save(surfaceList, shortAttackEnemyList, save[1], save[0]);

                currentTask = "saved!";
            } catch (Exception exception) {
                currentTask = "save exception";
            }
        }
    }

    private void changeColor() {
        String text = colorTextWindow.getOutputText();
        if (!text.equals("")) {
            try {
                demoDrawingSurface.setStandardColor(text);
                drawingSurfaceColor = text;
                currentTask = "color set";
            } catch (Exception exception) {
                demoDrawingSurface.setStandardColor(drawingSurfaceColor);
                currentTask = "color exception";
            }
        }
    }

    private void changeEffect() {
        String text = effectTextWindow.getOutputText();
        if (!text.equals("")) {
            try {
                demoDrawingSurface.setEffect(text);
                drawingSurfaceEffect = text;
                currentTask = "effect set";
            } catch (Exception exception) {
                demoDrawingSurface.setStandardColor(drawingSurfaceEffect);
                currentTask = "effect exception";
            }
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
                currentSurface.setEditableColor();
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
                currentSurface.setEditableColor();
            }
            if (moveCurrentRectangle) {
                currentSurface.editMove(getSynchronizedX(), getSynchronizedY());
                currentSurface.setEditableColor();
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
