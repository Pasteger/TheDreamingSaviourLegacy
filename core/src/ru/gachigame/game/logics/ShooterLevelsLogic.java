package ru.gachigame.game.logics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.gachigame.game.MyGdxGame;
import ru.gachigame.game.gameobject.Surface;
import ru.gachigame.game.resourceloader.LevelLoader;
import ru.gachigame.game.screen.MainMenuScreen;
import ru.gachigame.game.gameobject.shooter.character.Billy;
import ru.gachigame.game.gameobject.shooter.character.Slave;
import ru.gachigame.game.gameobject.Cum;
import ru.gachigame.game.screen.DeathScreen;
import ru.gachigame.game.screen.LevelsScreen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static ru.gachigame.game.resourceloader.LevelLoader.*;

public class ShooterLevelsLogic {
    private final MyGdxGame game;
    public static final List<Cum> cumList = new ArrayList<>();
    private final Billy billy;
    private final List<Slave> slaveList;
    private final List<Surface> surfaceList;

    public ShooterLevelsLogic(final MyGdxGame game) {
        this.game = game;
        game.camera.setToOrtho(false, 200, 200);

        surfaceList = getSurfaceList();
        slaveList = getSlaveList();
        slaveList.addAll(getMasterList());
        billy = new Billy();
    }

    public void render(){
        surfaceList.forEach(surface -> surface.draw(game.batch));

        game.batch.draw(billy.texture, billy.x, billy.y);

        slaveLive();
        cumLogic();
        game.font.draw(game.batch, billy.HP+"", billy.x-80, billy.y+80);

        billy.move(surfaceList, slaveList);
        game.camera.position.x = billy.x;
        game.camera.position.y = billy.y;
        billyDeath();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            billy.setX(10);
            billy.setY(10);
        }
    }
    private void slaveLive(){
        if(!slaveList.isEmpty()) {
            for (Slave slave : slaveList) {
                game.batch.draw(slave.texture, slave.x, slave.y);
                slave.moveToBilly(billy, surfaceList, slaveList);
                slave.sightCalibration();
                slave.slaveShot(billy);

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
                game.setScreen(new LevelsScreen(game));
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
        for (Slave slave : slaveList){
            if (billy.HP < 1 || billy.overlaps(slave) && slave.type.equals("master")){
                cumList.clear();
                game.nickname = null;
                game.setScreen(new DeathScreen(game));
            }
        }
    }
}
