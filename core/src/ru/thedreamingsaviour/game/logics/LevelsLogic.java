package ru.thedreamingsaviour.game.logics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.character.Box;
import ru.thedreamingsaviour.game.gameobject.Bullet;
import ru.thedreamingsaviour.game.gameobject.Coin;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.character.Enemy;
import ru.thedreamingsaviour.game.gameobject.character.Ilya;
import ru.thedreamingsaviour.game.gameobject.character.Entity;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;
import ru.thedreamingsaviour.game.screen.DeathScreen;
import ru.thedreamingsaviour.game.screen.LevelsScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.getFactoryMusic;

public class LevelsLogic {
    private final MyGdxGame game;
    public static final List<Bullet> BULLET_LIST = new ArrayList<>();
    private final Ilya ilya;
    private final List<Enemy> enemyList;
    private final List<Surface> surfaceList;
    private final List<Coin> coinList;
    private final List<Box> boxList;
    private final List<Entity> entityList;
    private final Music music;
    private long score;

    //Необходимое для отладки
    int fps;
    long startFPSTime;
    int countRenders;

    public LevelsLogic(final MyGdxGame game) {
        this.game = game;
        game.camera.setToOrtho(false, 4000, 4000);

        surfaceList = LevelLoader.getSurfaceList();
        enemyList = LevelLoader.getEnemyList();
        coinList = LevelLoader.getCoinList();
        boxList = LevelLoader.getBoxList();

        entityList = new ArrayList<>();
        ilya = new Ilya();
        music = getFactoryMusic();
        music.setLooping(true);
        music.play();
        startFPSTime = System.currentTimeMillis();
    }

    public void render() {
        entityList.clear();
        entityList.add(ilya);
        entityList.addAll(enemyList);
        entityList.addAll(boxList);

        surfaceList.forEach(surface -> surface.draw(game.batch));

        ilya.sprite.draw(game.batch, ilya.x, ilya.y, 20);
        surfaceLogic();
        enemyLive();
        coinLogic();
        boxLogic();
        bulletLogic();

        boxList.forEach(box -> box.sprite.draw(game.batch, box.x, box.y, 5));
        coinList.forEach(coin -> coin.textures.draw(game.batch, coin.x, coin.y, 5));

        ilya.move(surfaceList, entityList);
        game.camera.position.x = ilya.x;
        game.camera.position.y = ilya.y;


        game.universalFont.draw(game.batch, "HP: " + ilya.HP, ilya.x - 1900, ilya.y + 1900);
        game.universalFont.draw(game.batch, "score: " + score, ilya.x - 1900, ilya.y + 1700);

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
            //System.out.println(fps);
            countRenders = 0;
        }
    }

    private void boxLogic() {
        for (Box box : boxList) {
            if (box.HP < 1) {
                int randomValue = new Random().nextInt(100);
                if (randomValue == 1) {
                    coinList.add(new Coin(box.x, box.y, 1000));
                } else if (randomValue < 5) {
                    coinList.add(new Coin(box.x, box.y, 100));
                } else if (randomValue < 10) {
                    coinList.add(new Coin(box.x, box.y, 10));
                } else if (randomValue < 20) {
                    coinList.add(new Coin(box.x, box.y, 1));
                }

                boxList.remove(box);
                return;
            }
        }
    }

    private void coinLogic() {
        for (Coin coin : coinList) {
            if (ilya.overlaps(coin)) {
                score += coin.value;
                coinList.remove(coin);
                return;
            }
        }
    }

    private void surfaceLogic() {
        List<Entity> entityList = new ArrayList<>();
        entityList.add(ilya);
        entityList.addAll(enemyList);
        entityList.addAll(boxList);

        for (Entity entity : entityList) {
            entity.jumpRender(surfaceList, boxList);
        }

        for (Surface surface : surfaceList) {
            for (Entity entity : entityList) {
                if (surface.overlaps(entity)) {
                    if (surface.getEffect().equals("gravity")) {
                        entity.gravitated = true;
                        entity.fall(surfaceList, entityList);
                    }
                    if (surface.getEffect().equals("death")) {
                        entity.HP = 0;
                        return;
                    }
                    if (surface.getEffect().equals("none")) {
                        entity.gravitated = false;
                        if (entity.timeFall != 0 && System.currentTimeMillis() - entity.deltaTime > 1000)
                            entity.timeFall = 0;
                    }
                }
            }
        }
    }

    private void enemyLive() {
        if (!enemyList.isEmpty()) {
            for (Enemy enemy : enemyList) {
                enemy.sprite.draw(game.batch, enemy.x, enemy.y, 20);
                enemy.sightCalibration();
                enemy.attack(ilya);
                enemy.moveToPlayer(ilya, surfaceList, entityList);

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
                for (Box box : boxList) {
                    if (bullet.overlaps(box)) {
                        bulletIterator.remove();
                        box.HP--;
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
