package ru.gachigame.game.shooter.screen;

import com.badlogic.gdx.Input;
import ru.gachigame.game.gameobject.Surface;
import ru.gachigame.game.resourceloader.LevelLoader;
import ru.gachigame.game.screen.MainMenuScreen;
import ru.gachigame.game.shooter.gameobject.character.parts.Cum;
import ru.gachigame.game.shooter.gameobject.character.Billy;
import ru.gachigame.game.shooter.gameobject.character.Slave;
import static ru.gachigame.game.resourceloader.LevelLoader.*;
import static ru.gachigame.game.resourceloader.LevelLoader.getSurfaceList;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import ru.gachigame.game.MyGdxGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import java.util.*;

public class ShooterLevelScreen implements Screen {
    private final MyGdxGame game;
    public static final List<Cum> cumList = new ArrayList<>();
    private final OrthographicCamera camera;
    private final Random random;
    private final Billy billy;
    private final List<Slave> slaveList;
    private final List<Surface> surfaceList;
    private float billyX;
    private float billyY;

    public ShooterLevelScreen(final MyGdxGame game){
        this.game = game;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        random = new Random();
        camera = game.getCamera();
        camera.setToOrtho(false, 200, 200);

        surfaceList = getSurfaceList();
        slaveList = getSlaveList();
        slaveList.addAll(getMasterList());
        billy = new Billy();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        surfaceList.forEach(surface -> surface.draw(game.batch));

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
        billy.texture.dispose();
        game.batch.dispose();
        game.dispose();
    }

    private void billyAngWalls(){
        for (Surface wall : surfaceList) {
            if (billy.overlaps(wall) && wall.getEffect().equals("solid")) {
                if (billyX - billy.x != 0) {
                    billy.x = billyX;
                }
                if (billyY - billy.y != 0) {
                    billy.y = billyY;
                }
            }
        }
        for (Slave slave : slaveList) {
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
        for (Surface wall : surfaceList) {
            if (slave.overlaps(wall) && wall.getEffect().equals("solid")) {
                if (slave.slaveX - slave.x != 0) {
                    slave.x = slave.slaveX;
                }
                if (slave.slaveY - slave.y != 0) {
                    slave.y = slave.slaveY;
                }
            }
        }

        for (Slave slave1 : slaveList) {
            if (!slave.equals(slave1)) {
                if (slave.overlaps(slave1) && slave.slaveX - slave.x != 0) {
                    slave.x = slave.slaveX;
                }
                if (slave.overlaps(slave1) && slave.slaveY - slave.y != 0) {
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
        if(!slaveList.isEmpty()) {
            for (Slave slave : slaveList) {
                game.batch.draw(slave.texture, slave.x, slave.y);
                slaveMoveToBilly(slave);
                slavesAngWalls(slave);
                slave.sightCalibration();
                slaveShot(slave);

                if (slave.HP <= 0) {
                    slaveList.remove(slave);
                    if(billy.HP < 4) billy.HP++;
                    break;
                }
            }
        }
        else {
            cumList.clear();
            try {
                LevelLoader.load(getNextLevel());
                game.setScreen(new ShooterLevelScreen(game));
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }

    private void cumLogic(){
        if (!cumList.isEmpty()) {
            Iterator<Cum> cumIterator = cumList.iterator();
            while (cumIterator.hasNext()) {
                Cum cum = cumIterator.next();
                cum.move();
                game.batch.draw(cum.texture, cum.x, cum.y);
                for (Surface wall : surfaceList) {
                    if (cum.overlaps(wall) && wall.getEffect().equals("solid")) {
                        cumIterator.remove();
                        return;
                    }
                }
                for (Slave slave : slaveList) {
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
            cumList.clear();
            game.nickname = null;
            game.setScreen(new DeathScreen(game));
        }
    }
}
