package ru.gachigame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import ru.gachigame.game.gameobject.Wall;
import ru.gachigame.game.shooter.ShooterCRUD;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import java.util.List;
import static ru.gachigame.game.shooter.ShooterCRUD.*;
import static ru.gachigame.game.shooter.ShooterCRUD.SHOOTER_MASTER_PATH;

public class LevelEditor implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final List<Slave> slaveArray;
    private final List<Wall> wallsArray;
    private final Texture dungeonTexture;
    private final Wall point = new Wall(0, 0, 1, 1);
    private boolean spawnSlave;
    private boolean deleteSlave;
    private boolean generateWall;
    private boolean removeWall;
    private boolean dragged;
    private int deltaX;
    private int deltaY;

    public LevelEditor(final MyGdxGame game){
        this.game = game;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        dungeonTexture = new Texture(SHOOTER_BACKGROUND_TEXTURE_PATH);
        camera = game.getCamera();
        camera.setToOrtho(false, 400, 400);
        wallsArray = JSONReader.readWalls(SHOOTER_WALLS_PATH);
        wallsArray.add(point);
        slaveArray = ShooterCRUD.readSlave(SHOOTER_SLAVES_PATH);
        slaveArray.addAll(ShooterCRUD.readMaster(SHOOTER_MASTER_PATH));

        Gdx.input.setInputProcessor(new InputProcessor() {
            private int touchDownX;
            private int touchDownY;
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
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                dragged = false;
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                deltaX = touchDownX - screenX;
                deltaY = touchDownY - screenY;
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
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(dungeonTexture, 0, 0);

        for (Wall wall : wallsArray){
            game.batch.draw(wall.texture, wall.x, wall.y);
        }
        for (Slave slave : slaveArray){
            game.batch.draw(slave.texture, slave.x, slave.y);
        }


        if (dragged) {
            camera.position.x += (float) deltaX/100;
            camera.position.y -= (float) deltaY/100;
        }



        game.font.draw(game.batch, camera.position.x + "  " + camera.position.y, camera.position.x-200, camera.position.y+190);
        game.font.draw(game.batch, Gdx.input.getX() + "   " + Gdx.input.getY(), camera.position.x-200, camera.position.y+175);


        point.x = camera.position.x;
        point.y = camera.position.y;

        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            spawnSlave = !spawnSlave;
            deleteSlave = false;
            System.out.println("spawnSlave");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            deleteSlave = !deleteSlave;
            spawnSlave = false;
            generateWall = false;
            removeWall = false;
            System.out.println("deleteSlave");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            generateWall = !generateWall;
            spawnSlave = false;
            deleteSlave = false;
            removeWall = false;
            System.out.println("generateWall");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            removeWall = !removeWall;
            spawnSlave = false;
            deleteSlave = false;
            generateWall = false;
            System.out.println("removeWall");
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
                    slave.getY() >= y - slave.getHeight() && slave.getY() <= y + 1)
            );
        }
    }

    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose() {}

    private float getXForCameraAndForMouse(){
        float coefficientX = 400f / 800f;
        return (camera.position.x + Gdx.input.getX() * coefficientX) - 200;
    }
    private float getYForCameraAndForMouse(){
        float coefficientY = 400f / 600f;
        return (camera.position.y + Gdx.input.getY() * coefficientY * -1) + 200;
    }
}
