package ru.gachigame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.gameobject.Surface;
import ru.gachigame.game.resourceloader.LevelSaver;
import ru.gachigame.game.screen.MainMenuScreen;
import ru.gachigame.game.shooter.gameobject.character.Master;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.util.Collections;
import java.util.List;
import static ru.gachigame.game.resourceloader.LevelLoader.*;

public class LevelEditor implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final List<Slave> slaveList;
    private final List<Master> masterList;
    private final List<Surface> surfaceList;
    private boolean dragged;
    private String currentTask;
    private int deltaX;
    private int deltaY;
    private Surface currentSurface;
    private Slave currentSlave;

    public LevelEditor(final MyGdxGame game){
        this.game = game;
        currentTask = "";
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        camera = game.getCamera();
        camera.setToOrtho(false, 400, 400);

        slaveList = getSlaveList();
        masterList = getMasterList();
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

        slaveList.forEach(slave -> game.batch.draw(slave.texture, slave.x, slave.y));
        masterList.forEach(master -> game.batch.draw(master.texture, master.x, master.y));

        game.font.draw(game.batch, camera.position.x + "  " + camera.position.y, camera.position.x-200, camera.position.y+190);
        game.font.draw(game.batch, currentTask, camera.position.x-200, camera.position.y+175);

        game.batch.end();

        if (dragged) {
            camera.position.x += (float) deltaX/100;
            camera.position.y -= (float) deltaY/100;
        }
        buttons();
    }

    private void buttons(){
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

        if (currentTask.equals("addFloor") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Surface floor = new Surface(getSynchronizedX(), getSynchronizedY(),
                    20, 20, "none", "floorTexture");
            surfaceList.add(floor);
            System.out.println("floor added");
        }

        if (currentTask.equals("edit") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
                (currentSurface == null && currentSlave == null)){
            setCurrentObject();
        }
        if (currentTask.equals("edit") && Gdx.input.isKeyJustPressed(Input.Keys.ENTER) &&
                (currentSurface != null || currentSlave != null)) {
            if (currentSurface != null) {
                currentSurface.setStandardTexture();
                currentSurface = null;
            }
            if (currentSlave != null) {
                currentSlave = null;
            }
            currentTask = "";
        }

        if (currentTask.equals("spawnSlave") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Slave slave = new Slave();
            slave.setX(getSynchronizedX());
            slave.setY(getSynchronizedY());
            slaveList.add(slave);
        }
        if (currentTask.equals("deleteSlave") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            float x = getSynchronizedX();
            float y = getSynchronizedY();
            slaveList.removeIf(slave -> (
                    slave.getX() >= x - slave.getWidth() && slave.getX() <= x + 1 &&
                            slave.getY() >= y - slave.getHeight() && slave.getY() <= y + 1
            ));
        }
        if (currentTask.equals("generateWall") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Surface wall = new Surface(getSynchronizedX(), getSynchronizedY(),
                    20, 20, "solid", "wallTexture");
            surfaceList.add(wall);
        }
        if (currentTask.equals("removeWall") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            float y = getSynchronizedY();
            float x = getSynchronizedX();
            surfaceList.removeIf(wall -> (wall.getEffect().equals("solid") &&
                    wall.getX() >= x - wall.getWidth() && wall.getX() <= x + 1 &&
                    wall.getY() >= y - wall.getHeight() && wall.getY() <= y + 1)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.S)){
            LevelSaver.save(surfaceList, slaveList, masterList, "level0", "level0");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private float getSynchronizedX(){
        float coefficientX = 400f / 800f;
        return (camera.position.x + Gdx.input.getX() * coefficientX) - 200;
    }
    private float getSynchronizedY(){
        float coefficientY = 400f / 600f;
        return (camera.position.y + Gdx.input.getY() * coefficientY * -1) + 200;
    }
    private void setCurrentObject(){
        float x = getSynchronizedX();
        float y = getSynchronizedY();
        for (Slave slave : slaveList) {
            if (findObjectFromCord(slave, x, y)){
                currentSlave = slave;
                return;
            }
        }
        for (Slave master : masterList) {
            if (findObjectFromCord(master, x, y)){
                currentSlave = master;
                return;
            }
        }
        for (Surface surface : surfaceList) {
            if (findObjectFromCord(surface, x, y)){
                currentSurface = surface;
                currentSurface.setEditableTexture();
                Collections.swap(surfaceList, surfaceList.indexOf(surface), surfaceList.size()-1);
                return;
            }
        }
    }

    private boolean findObjectFromCord(Rectangle object, float x, float y){
        return (object.getX() >= x - object.getWidth() && object.getX() <= x + 1 &&
                object.getY() >= y - object.getHeight() && object.getY() <= y + 1);
    }

    class EditorInputProcessor implements InputProcessor {
        private int touchDownX;
        private int touchDownY;
        private boolean extensionCurrentRectangle;
        private boolean moveCurrentRectangle;
        private boolean moveSlave;
        @Override public boolean keyDown(int keycode) {
            return false;
        }
        @Override public boolean keyUp(int keycode) {
            return false;
        }
        @Override public boolean keyTyped(char character) {
            return false;
        }
        @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button == 1) {
                touchDownX = screenX;
                touchDownY = screenY;
                dragged = true;
            }
            if (currentSurface != null && !extensionCurrentRectangle &&
                    getSynchronizedX() >= currentSurface.x &&
                    getSynchronizedX() <= currentSurface.x + 10 &&
                    getSynchronizedY() >= currentSurface.y &&
                    getSynchronizedY() <= currentSurface.y + 10){
                moveCurrentRectangle = true;
            }
            if (currentSurface != null && !moveCurrentRectangle &&
                    getSynchronizedX() >= currentSurface.x + currentSurface.width - 10 &&
                    getSynchronizedX() <= currentSurface.x + currentSurface.width &&
                    getSynchronizedY() >= currentSurface.y + currentSurface.height - 10 &&
                    getSynchronizedY() <= currentSurface.y + currentSurface.height){
                extensionCurrentRectangle = true;
            }

            if (currentSlave != null && !moveSlave &&
                    getSynchronizedX() >= currentSlave.x &&
                    getSynchronizedX() <= currentSlave.x + currentSlave.width &&
                    getSynchronizedY() >= currentSlave.y &&
                    getSynchronizedY() <= currentSlave.y + currentSlave.height){
                moveSlave = true;
            }
            return false;
        }
        @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            dragged = false;
            extensionCurrentRectangle = false;
            moveCurrentRectangle = false;
            moveSlave = false;
            return false;
        }
        @Override public boolean touchDragged(int screenX, int screenY, int pointer) {
            deltaX = touchDownX - screenX;
            deltaY = touchDownY - screenY;
            if (extensionCurrentRectangle){
                int width = (int) getSynchronizedX() - (int) currentSurface.getX();
                int height = (int) getSynchronizedY() - (int) currentSurface.getY();
                currentSurface.editExtension(width, height);
                currentSurface.setEditableTexture();
            }
            if (moveCurrentRectangle){
                currentSurface.editMove(getSynchronizedX(), getSynchronizedY());
                currentSurface.setEditableTexture();
            }

            if (moveSlave){
                currentSlave.x = getSynchronizedX();
                currentSlave.y = getSynchronizedY();
            }
            return false;
        }
        @Override public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }
        @Override public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }

    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose() {}
}
