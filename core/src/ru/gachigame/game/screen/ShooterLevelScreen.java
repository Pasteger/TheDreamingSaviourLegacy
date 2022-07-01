package ru.gachigame.game.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import ru.gachigame.game.characters.parts.CumArray;
import ru.gachigame.game.characters.parts.Cum;
import ru.gachigame.game.characters.Master;
import ru.gachigame.game.characters.Billy;
import ru.gachigame.game.characters.Slave;
import com.badlogic.gdx.graphics.Texture;
import org.json.simple.parser.JSONParser;
import com.badlogic.gdx.graphics.GL20;
import ru.gachigame.game.parts.Wall;
import ru.gachigame.game.MyGdxGame;
import org.json.simple.JSONObject;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.util.Random;
import java.util.List;
import java.io.File;

public class ShooterLevelScreen implements Screen {
    private final MyGdxGame game;

    private final OrthographicCamera camera;
    private final Random random;

    private final Billy billy;
    private final List<Slave> slaveArray;
    private final List<Wall> wallsArray;
    private final Texture dungeonTexture;
    private float billyX;
    private float billyY;

    public ShooterLevelScreen(final MyGdxGame game){
        random = new Random();
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 200, 200);

        dungeonTexture = new Texture("sprites/dungeon.jpg");
        wallsArray = readWalls();
        slaveArray = readSlave();
        billy = new Billy();

        spawnMaster();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!game.music.isPlaying()){
            game.musicID = random.nextInt(4);
            game.music = game.musicList.get(game.musicID);
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
            CumArray.cumArray.clear();
            game.setScreen(new MainMenuScreen(game));
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

    private List<Wall> readWalls() {
        List<Wall> wallList = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) readJson(new File("core/objects/shooter/walls.json"));
            List<JSONObject> JSONWallsList = (List<JSONObject>) jsonObject.get("wallsList");
            for (JSONObject thisObject : JSONWallsList) {
                int x = Integer.parseInt(String.valueOf(thisObject.get("x")));
                int y = Integer.parseInt(String.valueOf(thisObject.get("y")));
                int width = Integer.parseInt(String.valueOf(thisObject.get("width")));
                int height = Integer.parseInt(String.valueOf(thisObject.get("height")));
                wallList.add(new Wall(x, y, width, height));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return wallList;
    }

    private List<Slave> readSlave(){
        List<Slave> slaveList = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) readJson(new File("core/objects/shooter/slave.json"));
            List<JSONObject> JSONSlavesList = (List<JSONObject>) jsonObject.get("slavesList");
            for (JSONObject thisObject : JSONSlavesList) {
                float x = Float.parseFloat(String.valueOf(thisObject.get("x")));
                float y = Float.parseFloat(String.valueOf(thisObject.get("y")));
                Slave slave = new Slave();
                slave.setX(x);
                slave.setY(y);
                slaveList.add(slave);
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return slaveList;
    }

    public static Object readJson(File file) throws Exception {
        FileReader reader = new FileReader(file);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
}
