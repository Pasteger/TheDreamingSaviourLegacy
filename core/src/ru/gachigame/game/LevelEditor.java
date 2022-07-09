package ru.gachigame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.gachigame.game.gameobject.Wall;
import ru.gachigame.game.resourceloader.JSONReader;
import ru.gachigame.game.screen.MainMenuScreen;
import ru.gachigame.game.resourceloader.ShooterCRUD;
import ru.gachigame.game.shooter.gameobject.character.Master;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.util.List;
import static ru.gachigame.game.resourceloader.ShooterCRUD.*;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class LevelEditor implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final List<Slave> slaveArray;
    private final List<Master> masterArray;
    private final List<Wall> wallsArray;
    private final Texture dungeonTexture;
    private boolean spawnSlave;
    private boolean deleteSlave;
    private boolean generateWall;
    private boolean removeWall;
    private boolean edit;
    private boolean dragged;
    private int deltaX;
    private int deltaY;
    private Wall currentWall;
    private Slave currentSlave;

    public LevelEditor(final MyGdxGame game){
        this.game = game;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        camera = game.getCamera();
        camera.setToOrtho(false, 400, 400);
        dungeonTexture = getShooterBackground();
        wallsArray = JSONReader.readWalls(getShooterWallsPath());
        slaveArray = ShooterCRUD.readSlave(getShooterSlavesPath());
        masterArray = ShooterCRUD.readMaster(getShooterMasterPath());

        Gdx.input.setInputProcessor(new EditorInputProcessor());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(dungeonTexture, 0, 0);

        wallsArray.forEach(wall -> game.batch.draw(wall.texture, wall.x, wall.y));
        slaveArray.forEach(slave -> game.batch.draw(slave.texture, slave.x, slave.y));
        masterArray.forEach(master -> game.batch.draw(master.texture, master.x, master.y));

        if (dragged) {
            camera.position.x += (float) deltaX/100;
            camera.position.y -= (float) deltaY/100;
        }

        game.font.draw(game.batch, camera.position.x + "  " + camera.position.y, camera.position.x-200, camera.position.y+190);
        game.font.draw(game.batch, Gdx.input.getX() + "   " + Gdx.input.getY(), camera.position.x-200, camera.position.y+175);

        game.batch.end();

        buttons();
    }

    private void buttons(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            spawnSlave = !spawnSlave;
            deleteSlave = false;
            generateWall = false;
            removeWall = false;
            edit = false;
            System.out.println("spawnSlave");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            deleteSlave = !deleteSlave;
            spawnSlave = false;
            generateWall = false;
            removeWall = false;
            edit = false;
            System.out.println("deleteSlave");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            generateWall = !generateWall;
            spawnSlave = false;
            deleteSlave = false;
            removeWall = false;
            edit = false;
            System.out.println("generateWall");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            removeWall = !removeWall;
            spawnSlave = false;
            deleteSlave = false;
            generateWall = false;
            edit = false;
            System.out.println("removeWall");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            edit = !edit;
            spawnSlave = false;
            deleteSlave = false;
            generateWall = false;
            removeWall = false;
            System.out.println("edit");
        }

        if (edit && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && (currentWall == null || currentSlave == null)){
            setCurrentObject();
        }
        if (edit && Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && (currentWall != null || currentSlave != null)) {
            if (currentWall != null) {
                currentWall.setStandardTexture();
                currentWall = null;
            }
            if (currentSlave != null) {
                currentSlave = null;
            }
            edit = false;
        }

        if (spawnSlave && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Slave slave = new Slave();
            slave.setX(getXForCameraAndForMouse());
            slave.setY(getYForCameraAndForMouse());
            slaveArray.add(slave);
        }
        if (deleteSlave && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            float x = getXForCameraAndForMouse();
            float y = getYForCameraAndForMouse();
            slaveArray.removeIf(slave -> (
                    slave.getX() >= x - slave.getWidth() && slave.getX() <= x + 1 &&
                            slave.getY() >= y - slave.getHeight() && slave.getY() <= y + 1
            ));
        }
        if (generateWall && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Wall wall = new Wall(
                    (int) getXForCameraAndForMouse(),
                    (int) getYForCameraAndForMouse(),
                    20, 20);
            wallsArray.add(wall);
        }
        if (removeWall && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            float x = getXForCameraAndForMouse();
            float y = getYForCameraAndForMouse();
            wallsArray.removeIf(wall -> (
                    wall.getX() >= x - wall.getWidth() && wall.getX() <= x + 1 &&
                            wall.getY() >= y - wall.getHeight() && wall.getY() <= y + 1)
            );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.S)){
            boolean saveSlave = ShooterCRUD.saveSlaveList(slaveArray);
            System.out.println("saveSlave " + saveSlave);
            boolean saveMaster = ShooterCRUD.saveMasterList(masterArray);
            System.out.println("saveMaster " + saveMaster);
            boolean saveWall = JSONReader.saveWallList(wallsArray, getShooterWallsPath());
            System.out.println("saveWall " + saveWall);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {game.setScreen(new MainMenuScreen(game));}
    }

    private float getXForCameraAndForMouse(){
        float coefficientX = 400f / 800f;
        return (camera.position.x + Gdx.input.getX() * coefficientX) - 200;
    }
    private float getYForCameraAndForMouse(){
        float coefficientY = 400f / 600f;
        return (camera.position.y + Gdx.input.getY() * coefficientY * -1) + 200;
    }
    private void setCurrentObject(){
        float x = getXForCameraAndForMouse();
        float y = getYForCameraAndForMouse();
        for (Wall wall : wallsArray) {
            if (findObjectFromCord(wall, x, y)){
                currentWall = wall;
                currentWall.setEditableTexture();
                return;
            }
        }
        for (Slave slave : slaveArray) {
            if (findObjectFromCord(slave, x, y)){
                currentSlave = slave;
                return;
            }
        }
        for (Slave master : masterArray) {
            if (findObjectFromCord(master, x, y)){
                currentSlave = master;
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
                    getXForCameraAndForMouse() >= currentWall.x &&
                    getXForCameraAndForMouse() <= currentWall.x + 10 &&
                    getYForCameraAndForMouse() >= currentWall.y &&
                    getYForCameraAndForMouse() <= currentWall.y + 10){
                moveWall = true;
            }
            if (currentWall != null && !moveWall &&
                    getXForCameraAndForMouse() >= currentWall.x + currentWall.width - 10 &&
                    getXForCameraAndForMouse() <= currentWall.x + currentWall.width &&
                    getYForCameraAndForMouse() >= currentWall.y + currentWall.height - 10 &&
                    getYForCameraAndForMouse() <= currentWall.y + currentWall.height){
                extensionWall = true;
            }
            if (currentSlave != null && !moveSlave &&
                    getXForCameraAndForMouse() >= currentSlave.x &&
                    getXForCameraAndForMouse() <= currentSlave.x + currentSlave.width &&
                    getYForCameraAndForMouse() >= currentSlave.y &&
                    getYForCameraAndForMouse() <= currentSlave.y + currentSlave.height){
                moveSlave = true;
            }
            return false;
        }
        @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            dragged = false;
            extensionWall = false;
            moveWall = false;
            moveSlave = false;
            return false;
        }
        @Override public boolean touchDragged(int screenX, int screenY, int pointer) {
            deltaX = touchDownX - screenX;
            deltaY = touchDownY - screenY;
            if (extensionWall){
                int width = (int) getXForCameraAndForMouse() - (int) currentWall.getX();
                int height = (int) getYForCameraAndForMouse() - (int) currentWall.getY();
                currentWall.width = width;
                currentWall.height = height;
                currentWall.setEditableTexture();
            }
            if (moveWall){
                currentWall.x = getXForCameraAndForMouse();
                currentWall.y = getYForCameraAndForMouse();
                currentWall.setEditableTexture();
            }
            if (moveSlave){
                currentSlave.x = getXForCameraAndForMouse();
                currentSlave.y = getYForCameraAndForMouse();
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
