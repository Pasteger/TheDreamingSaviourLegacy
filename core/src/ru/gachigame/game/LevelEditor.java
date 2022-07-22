package ru.gachigame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.gameobject.Floor;
import ru.gachigame.game.gameobject.Wall;
import ru.gachigame.game.resourceloader.JSONReader;
import ru.gachigame.game.screen.MainMenuScreen;
import ru.gachigame.game.resourceloader.ShooterCRUD;
import ru.gachigame.game.shooter.gameobject.character.Master;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static ru.gachigame.game.resourceloader.ShooterCRUD.*;

public class LevelEditor implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final List<Slave> slaveList;
    private final List<Master> masterList;
    private final List<Wall> wallsList;
    private final List<Floor> floorList;
    private boolean dragged;
    private String currentTask;
    private int deltaX;
    private int deltaY;
    private Wall currentWall;
    private Floor currentFloor;
    private Slave currentSlave;

    public LevelEditor(final MyGdxGame game){
        this.game = game;
        currentTask = "";
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        camera = game.getCamera();
        camera.setToOrtho(false, 400, 400);
        floorList = new ArrayList<>();
        wallsList = JSONReader.readWalls(getShooterWallsPath());
        slaveList = ShooterCRUD.readSlave(getShooterSlavesPath());
        masterList = ShooterCRUD.readMaster(getShooterMasterPath());

        Gdx.input.setInputProcessor(new EditorInputProcessor());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        floorList.forEach(floor -> floor.draw(game.batch));

        wallsList.forEach(wall -> game.batch.draw(wall.texture, wall.x, wall.y));
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
            Floor floor = new Floor((int) getSynchronizedX(), (int) getSynchronizedY(), 20, 20);
            floorList.add(floor);
            System.out.println("floor added");
        }

        if (currentTask.equals("edit") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
                (currentWall == null && currentSlave == null && currentFloor == null)){
            setCurrentObject();
        }
        if (currentTask.equals("edit") && Gdx.input.isKeyJustPressed(Input.Keys.ENTER) &&
                (currentWall != null || currentSlave != null || currentFloor != null)) {
            if (currentWall != null) {
                currentWall.setStandardTexture();
                currentWall = null;
            }
            if (currentSlave != null) {
                currentSlave = null;
            }
            if (currentFloor != null) {
                currentFloor.setStandardTexture();
                currentFloor = null;
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
            Wall wall = new Wall((int) getSynchronizedX(), (int) getSynchronizedY(), 20, 20);
            wallsList.add(wall);
        }
        if (currentTask.equals("removeWall") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            float y = getSynchronizedY();
            float x = getSynchronizedX();
            wallsList.removeIf(wall -> (
                    wall.getX() >= x - wall.getWidth() && wall.getX() <= x + 1 &&
                            wall.getY() >= y - wall.getHeight() && wall.getY() <= y + 1)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.S)){
            boolean saveSlave = ShooterCRUD.saveSlaveList(slaveList);
            System.out.println("saveSlave " + saveSlave);
            boolean saveMaster = ShooterCRUD.saveMasterList(masterList);
            System.out.println("saveMaster " + saveMaster);
            boolean saveWall = JSONReader.saveWallList(wallsList, getShooterWallsPath());
            System.out.println("saveWall " + saveWall);
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
        for (Wall wall : wallsList) {
            if (findObjectFromCord(wall, x, y)){
                currentWall = wall;
                currentWall.setEditableTexture();
                Collections.swap(wallsList, wallsList.indexOf(currentWall), wallsList.size()-1);
                return;
            }
        }
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
        for (Floor floor : floorList) {
            if (findObjectFromCord(floor, x, y)){
                currentFloor = floor;
                currentFloor.setEditableTexture();
                Collections.swap(floorList, floorList.indexOf(currentFloor), floorList.size()-1);
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
        private boolean extensionWall;
        private boolean moveWall;
        private boolean extensionFloor;
        private boolean moveFloor;
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
            if (currentWall != null && !extensionWall &&
                    getSynchronizedX() >= currentWall.x &&
                    getSynchronizedX() <= currentWall.x + 10 &&
                    getSynchronizedY() >= currentWall.y &&
                    getSynchronizedY() <= currentWall.y + 10){
                moveWall = true;
            }
            if (currentWall != null && !moveWall &&
                    getSynchronizedX() >= currentWall.x + currentWall.width - 10 &&
                    getSynchronizedX() <= currentWall.x + currentWall.width &&
                    getSynchronizedY() >= currentWall.y + currentWall.height - 10 &&
                    getSynchronizedY() <= currentWall.y + currentWall.height){
                extensionWall = true;
            }
            if (currentFloor != null && !extensionFloor &&
                    getSynchronizedX() >= currentFloor.x &&
                    getSynchronizedX() <= currentFloor.x + 10 &&
                    getSynchronizedY() >= currentFloor.y &&
                    getSynchronizedY() <= currentFloor.y + 10){
                moveFloor = true;
            }
            if (currentFloor != null && !moveFloor &&
                    getSynchronizedX() >= currentFloor.x + currentFloor.width - 10 &&
                    getSynchronizedX() <= currentFloor.x + currentFloor.width &&
                    getSynchronizedY() >= currentFloor.y + currentFloor.height - 10 &&
                    getSynchronizedY() <= currentFloor.y + currentFloor.height){
                extensionFloor = true;
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
            extensionWall = false;
            extensionFloor = false;
            moveFloor = false;
            moveWall = false;
            moveSlave = false;
            return false;
        }
        @Override public boolean touchDragged(int screenX, int screenY, int pointer) {
            deltaX = touchDownX - screenX;
            deltaY = touchDownY - screenY;
            if (extensionWall){
                int width = (int) getSynchronizedX() - (int) currentWall.getX();
                int height = (int) getSynchronizedY() - (int) currentWall.getY();
                currentWall.width = width;
                currentWall.height = height;
                currentWall.setEditableTexture();
            }
            if (moveWall){
                currentWall.x = getSynchronizedX();
                currentWall.y = getSynchronizedY();
                currentWall.setEditableTexture();
            }
            if (extensionFloor){
                int width = (int) getSynchronizedX() - (int) currentFloor.getX();
                int height = (int) getSynchronizedY() - (int) currentFloor.getY();
                currentFloor.width = width;
                currentFloor.height = height;
                currentFloor.setEditableTexture();
            }
            if (moveFloor){
                currentFloor.x = getSynchronizedX();
                currentFloor.y = getSynchronizedY();
                currentFloor.setEditableTexture();
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
