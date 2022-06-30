package ru.gachigame.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.gachigame.game.MyGdxGame;
import ru.gachigame.game.characters.Billy;
import ru.gachigame.game.characters.Master;
import ru.gachigame.game.characters.Slave;
import ru.gachigame.game.characters.parts.Cum;
import ru.gachigame.game.characters.parts.CumArray;
import ru.gachigame.game.parts.Wall;
import ru.gachigame.game.parts.Walls;
import ru.gachigame.game.screen.parts.TableOfRecords;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainGameSpace implements Screen {
    final MyGdxGame game;

    OrthographicCamera camera;
    Random random;

    Billy billy;
    ArrayList<Slave> slaveArray;
    Array<Wall> wallsArray;
    Texture dungeonTexture;

    long startCurrentTime;
    long finishCurrentTime;

    float billyX;
    float billyY;

    public MainGameSpace(final MyGdxGame game){
        random = new Random();
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 200, 200);

        dungeonTexture = new Texture("sprites/dungeon.jpg");
        slaveArray = new ArrayList<>();
        wallsArray = new Walls().wallsArray;
        billy = new Billy();

        spawnSlave(800, 400);
        /*spawnSlave(820, 420);
        spawnSlave(1000, 500);


        spawnSlave(925, 880);
        spawnSlave(1260, 872);


        spawnSlave(1126, 1030);
        spawnSlave(1264, 1030);
        spawnSlave(1400, 1030);
        spawnSlave(1538, 1030);

        spawnSlave(1126, 1166);
        spawnSlave(1264, 1166);
        spawnSlave(1400, 1166);
        spawnSlave(1538, 1166);

        spawnSlave(1126, 1302);
        spawnSlave(1264, 1302);
        spawnSlave(1400, 1302);
        spawnSlave(1538, 1302);

        spawnMaster();*/

        startCurrentTime = System.currentTimeMillis();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!game.music.isPlaying()){
            game.musicID = random.nextInt(4);
            game.music = game.musicArrayList.get(game.musicID);
            game.music.play();
        }


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

    private void spawnSlave(int slaveX, int slaveY){
        Slave slave = new Slave();
        slave.x = slaveX;
        slave.y = slaveY;
        slaveArray.add(slave);
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
                slave.shot();
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
            finishCurrentTime = System.currentTimeMillis();
            float timeMs = finishCurrentTime - startCurrentTime;
            float time = (timeMs/1000/60);
            String sTime = String.valueOf(time);
            StringBuilder sTimeAccuracy = new StringBuilder();
            char timeChar;
            for (int accuracy = 0; accuracy < 5; accuracy++){
                timeChar = sTime.charAt(accuracy);
                sTimeAccuracy.append(timeChar);
            }
            time = Float.parseFloat(sTimeAccuracy.toString());
            CumArray.cumArray.clear();
            TableOfRecords.addRecord(time, game.nickname);
            game.nickname = null;
            game.setScreen(new TableOfRecordsScreen(game));
        }
    }

    private void cumLogic(){
        if (!CumArray.cumArray.isEmpty()) {
            Iterator<Cum> cumIterator = CumArray.cumArray.iterator();
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

    private void spawnMaster(){
        Master master = new Master();
        master.x = 580;
        master.y = 820;
        slaveArray.add(master);
    }

    private void billyDeath(){
        if (billy.HP < 1) {
            CumArray.cumArray.clear();
            game.nickname = null;
            game.setScreen(new DeathScreen(game));
        }
    }
}
