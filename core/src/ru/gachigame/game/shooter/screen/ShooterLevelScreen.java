package ru.gachigame.game.shooter.screen;

import com.badlogic.gdx.Input;
import ru.gachigame.game.screen.MainMenuScreen;
import ru.gachigame.game.resourceloader.ShooterCRUD;
import ru.gachigame.game.shooter.gameobject.character.parts.Cum;
import ru.gachigame.game.shooter.gameobject.character.Billy;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import static ru.gachigame.game.resourceloader.ShooterCRUD.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import ru.gachigame.game.gameobject.Wall;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import ru.gachigame.game.resourceloader.JSONReader;
import ru.gachigame.game.MyGdxGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import java.util.*;

public class ShooterLevelScreen implements Screen {
    private final MyGdxGame game;
    public static final List<Cum> cumArray = new ArrayList<>();
    private final OrthographicCamera camera;
    private final Random random;
    private final Billy billy;
    private final List<Slave> slaveArray;
    private final List<Wall> wallsArray;
    private final Texture dungeonTexture;
    private float billyX;
    private float billyY;

    public ShooterLevelScreen(final MyGdxGame game){
        this.game = game;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        dungeonTexture = new Texture(SHOOTER_BACKGROUND_TEXTURE_PATH);
        random = new Random();
        camera = game.getCamera();
        camera.setToOrtho(false, 200, 200);
        wallsArray = JSONReader.readWalls(SHOOTER_WALLS_PATH);
        slaveArray = ShooterCRUD.readSlave(SHOOTER_SLAVES_PATH);
        slaveArray.addAll(ShooterCRUD.readMaster(SHOOTER_MASTER_PATH));
        billy = new Billy();
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
        game.batch.draw(billy.texture, billy.x, billy.y);

        slaveLive();
        cumLogic();
        game.font.draw(game.batch, billy.HP+"", billy.x-80, billy.y+80);

        game.batch.end();

        billy.move();
        billyAngWalls();
        camera.position.x = billy.x;
        camera.position.y = billy.y;
        billyDeath();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}

    @Override
    public void dispose() {
        dungeonTexture.dispose();
        billy.texture.dispose();
        game.batch.dispose();
        game.dispose();
    }

    private void billyAngWalls(){
        for (Wall wall : wallsArray) {
            if (billy.overlaps(wall) & billyX-billy.x != 0) {
                billy.x = billyX;
            }
            if (billy.overlaps(wall) & billyY-billy.y != 0) {
                billy.y = billyY;
            }
        }
        for (Slave slave : slaveArray) {
            if (billy.overlaps(slave) & billyX - billy.x != 0) {
                billy.x = billyX;
                if(slave.type.equals("master")){
                    billy.HP = 0;
                }
            }
            if (billy.overlaps(slave) & billyY - billy.y != 0) {
                billy.y = billyY;
                if(slave.type.equals("master")){
                    billy.HP = 0;
                }
            }
        }
        billyX = billy.x;
        billyY = billy.y;
    }

    private void slavesAngWalls(Slave slave){
        for (Wall wall : wallsArray) {
            if (slave.overlaps(wall) & slave.slaveX-slave.x != 0) {
                slave.x = slave.slaveX;
            }
            if (slave.overlaps(wall) & slave.slaveY-slave.y != 0) {
                slave.y = slave.slaveY;
            }
        }

        for (Slave slave1 : slaveArray) {
            if (!slave.equals(slave1)) {
                if (slave.overlaps(slave1) & slave.slaveX - slave.x != 0) {
                    slave.x = slave.slaveX;
                }
                if (slave.overlaps(slave1) & slave.slaveY - slave.y != 0) {
                    slave.y = slave.slaveY;
                }
            }
        }
        slave.slaveX = slave.x;
        slave.slaveY = slave.y;

    }


    private void slaveMoveToBilly(Slave slave){
        slave.fieldOfView.x = slave.x - slave.fieldOfView.width/2;
        slave.fieldOfView.y = slave.y - slave.fieldOfView.height/2;
        if (slave.fieldOfView.overlaps(billy)) {
            if (billy.x > slave.x & billy.x - slave.x >= slave.width) {
                slave.moveRight();
            }
            if (billy.x < slave.x & slave.x - billy.x >= slave.width) {
                slave.moveLeft();
            }
            if (billy.y > slave.y & billy.y - slave.y >= slave.width) {
                slave.moveUp();
            }
            if (billy.y < slave.y & slave.y - billy.y >= slave.width) {
                slave.moveDown();
            }
        }
    }

    private void slaveShot(Slave slave){
        if (slave.shotDistanceHitBox.overlaps(billy)) {
            slave.recharge--;
            if (slave.recharge <= 0) {
                slave.recharge = (byte) ((byte) 12 + random.nextInt(50));
                slave.shot("BAD");
            }
        }
    }

    private void slaveLive(){
        if(!slaveArray.isEmpty()) {
            for (Slave slave : slaveArray) {
                game.batch.draw(slave.texture, slave.x, slave.y);
                slaveMoveToBilly(slave);
                slavesAngWalls(slave);
                slave.sightCalibration();
                slaveShot(slave);

                if (slave.HP <= 0) {
                    slaveArray.remove(slave);
                    if(billy.HP < 4) billy.HP++;
                    break;
                }
            }
        }
        else {
            cumArray.clear();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void cumLogic(){
        if (!cumArray.isEmpty()) {
            Iterator<Cum> cumIterator = cumArray.iterator();
            while (cumIterator.hasNext()) {
                Cum cum = cumIterator.next();
                cum.move();
                game.batch.draw(cum.texture, cum.x, cum.y);
                for (Wall wall : wallsArray) {
                    if (cum.overlaps(wall)) {
                        cumIterator.remove();
                        return;
                    }
                }
                for (Slave slave : slaveArray) {
                    if (cum.overlaps(slave) && cum.type.equals("GOOD")) {
                        cumIterator.remove();
                        slave.HP--;
                        return;
                    }
                }
                if (cum.overlaps(billy) && (cum.type.equals("BAD") || cum.type.equals("MASTERS"))) {
                    cumIterator.remove();
                    if(cum.type.equals("MASTERS")){
                        billy.HP = 0;
                        return;
                    }
                    billy.HP--;
                    return;
                }
            }
        }
    }

    private void billyDeath(){
        if (billy.HP < 1) {
            cumArray.clear();
            game.nickname = null;
            game.setScreen(new DeathScreen(game));
        }
    }
}
