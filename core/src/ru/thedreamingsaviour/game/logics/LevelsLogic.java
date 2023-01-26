package ru.thedreamingsaviour.game.logics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.Bullet;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.character.Enemy;
import ru.thedreamingsaviour.game.gameobject.character.Ilya;
import ru.thedreamingsaviour.game.gameobject.character.Character;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;
import ru.thedreamingsaviour.game.screen.DeathScreen;
import ru.thedreamingsaviour.game.screen.LevelsScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.getFactoryMusic;

public class LevelsLogic {
    private final MyGdxGame game;
    public static final List<Bullet> BULLET_LIST = new ArrayList<>();
    private final Ilya ilya;
    private final List<Enemy> enemyList;
    private final List<Surface> surfaceList;
    private final Music music;

    //Необходимое для отладки
    int fps;
    long startFPSTime;
    int countRenders;

    public LevelsLogic(final MyGdxGame game) {
        this.game = game;
        game.camera.setToOrtho(false, 4000, 4000);

        surfaceList = LevelLoader.getSurfaceList();
        enemyList = LevelLoader.getEnemyList();
        ilya = new Ilya();
        music = getFactoryMusic();
        music.setLooping(true);
        music.play();
        startFPSTime = System.currentTimeMillis();
    }

    public void render() {
        surfaceList.forEach(surface -> surface.draw(game.batch));

        game.batch.draw(ilya.texture, ilya.x, ilya.y);
        surfaceLogic();
        enemyLive();
        bulletLogic();

        ilya.move(surfaceList, enemyList);
        game.camera.position.x = ilya.x;
        game.camera.position.y = ilya.y;


        game.universalFont.draw(game.batch, "HP: " + ilya.HP, ilya.x - 1900, ilya.y + 1900);

        ilyaDeath();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            music.stop();
            game.setScreen(new MainMenuScreen(game));
        }

        //Необходимое для отладки
        countRenders++;
        long finishFPSTime = System.currentTimeMillis();
        if (finishFPSTime - startFPSTime >= 1000) {
            startFPSTime = finishFPSTime;
            fps = countRenders;
            System.out.println(fps);
            countRenders = 0;
        }
    }

    private void surfaceLogic() {
        List<Character> characterList = new ArrayList<>();
        characterList.add(ilya);
        characterList.addAll(enemyList);

        for (Surface surface : surfaceList) {
            for (Character character : characterList) {
                if (surface.overlaps(character)) {
                    if (surface.getEffect().equals("gravity")) {
                        character.gravitated = true;
                        character.fall(surfaceList, enemyList);
                    } else if (surface.getEffect().equals("death")) {
                        character.HP = 0;
                    } else {
                        character.gravitated = false;
                    }
                }
            }
        }
    }


    private void enemyLive() {
        if (!enemyList.isEmpty()) {
            for (Enemy enemy : enemyList) {
                game.batch.draw(enemy.texture, enemy.x, enemy.y);
                enemy.moveToPlayer(ilya, surfaceList, enemyList);
                enemy.sightCalibration();
                enemy.attack(ilya);

                if (enemy.HP <= 0) {
                    enemyList.remove(enemy);
                    if (ilya.HP < 4) ilya.HP++;
                    break;
                }
            }
        } else {
            BULLET_LIST.clear();
            music.stop();
            try {
                LevelLoader.load(LevelLoader.getNextLevel());
                game.setScreen(new LevelsScreen(game));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void bulletLogic() {
        if (!BULLET_LIST.isEmpty()) {
            Iterator<Bullet> bulletIterator = BULLET_LIST.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                bullet.move();
                bullet.textures.draw(game.batch, bullet.x, bullet.y, 2);
                for (Surface surface : surfaceList) {
                    if (bullet.overlaps(surface) && surface.getEffect().equals("solid")) {
                        bulletIterator.remove();
                        return;
                    }
                }
                for (Enemy enemy : enemyList) {
                    if (bullet.overlaps(enemy) && bullet.type.equals("GOOD")) {
                        bulletIterator.remove();
                        enemy.HP--;
                        return;
                    }
                }
                if (bullet.overlaps(ilya) && (bullet.type.equals("BAD") || bullet.type.equals("MASTERS"))) {
                    bulletIterator.remove();
                    if (bullet.type.equals("MASTERS")) {
                        ilya.HP = 0;
                        return;
                    }
                    ilya.HP--;
                    return;
                }
            }
        }
    }

    private void ilyaDeath() {
        if (ilya.HP < 1) {
            music.stop();
            BULLET_LIST.clear();
            game.setScreen(new DeathScreen(game));
        }
    }
}
