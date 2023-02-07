package ru.thedreamingsaviour.game.logics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.*;
import ru.thedreamingsaviour.game.gameobject.entity.Box;
import ru.thedreamingsaviour.game.gameobject.entity.Enemy;
import ru.thedreamingsaviour.game.gameobject.entity.Player;
import ru.thedreamingsaviour.game.gameobject.entity.Entity;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.screen.DeathScreen;
import ru.thedreamingsaviour.game.screen.LevelsScreen;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.getFactoryMusic;
import static ru.thedreamingsaviour.game.resourceloader.SaveLoader.PLAYER;

public class LevelsLogic {
    private final MyGdxGame game;
    public static final List<Bullet> BULLET_LIST = new ArrayList<>();
    private final Player player;
    private final List<Enemy> enemyList;
    private final List<Surface> surfaceList;
    private final List<Coin> coinList;
    private final List<Box> boxList;
    private final List<Entity> entityList;
    private final List<DecorObject> decorList;
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
        decorList = LevelLoader.getDecorList();

        entityList = new ArrayList<>();
        player = PLAYER;
        player.setX(LevelLoader.getStartX());
        player.setY(LevelLoader.getStartY());
        music = getFactoryMusic();
        music.setLooping(true);
        music.play();
        startFPSTime = System.currentTimeMillis();
    }

    public void render() {
        entityList.clear();
        entityList.add(player);
        entityList.addAll(enemyList);
        entityList.addAll(boxList);

        surfaceList.stream().filter(surface ->
                !(surface.getEffect().equals("solid") || surface.getEffect().equals("draw_over"))).forEach(surface -> surface.draw(game.batch));

        decorList.forEach(decorObject -> decorObject.draw(game.batch));

        surfaceList.stream().filter(surface ->
                (surface.getEffect().equals("solid") || surface.getEffect().equals("draw_over"))).forEach(surface -> surface.draw(game.batch));

        player.animatedObject.draw(game.batch, player.x, player.y, player.width, player.height, 20);
        surfaceLogic();
        enemyLive();
        coinLogic();
        boxLogic();
        bulletLogic();

        boxList.forEach(box -> box.animatedObject.draw(game.batch, box.x, box.y, box.width, box.height, 5));
        coinList.forEach(coin -> coin.textures.draw(game.batch, coin.x, coin.y, coin.width, coin.height, coin.gravitated ? 5 : 15));

        player.move(surfaceList, entityList);
        game.camera.position.x = player.x;
        game.camera.position.y = player.y;


        game.universalFont.draw(game.batch, "HP: " + player.HP, player.x - 1900, player.y + 1900);
        game.universalFont.draw(game.batch, "score: " + score, player.x - 1900, player.y + 1700);

        ilyaDeath();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            music.stop();
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

    private void boxLogic() {
        for (Box box : boxList) {
            if (box.HP < 1) {
                int randomValue = new Random().nextInt(100);
                if (randomValue == 1) {
                    coinList.add(new Coin(box.x + (box.width / 2 - 60), box.y + (box.height / 2 - 60), 1000));
                } else if (randomValue < 5) {
                    coinList.add(new Coin(box.x + (box.width / 2 - 60), box.y + (box.height / 2 - 60), 100));
                } else if (randomValue < 10) {
                    coinList.add(new Coin(box.x + (box.width / 2 - 60), box.y + (box.height / 2 - 60), 10));
                } else if (randomValue < 20) {
                    coinList.add(new Coin(box.x + (box.width / 2 - 60), box.y + (box.height / 2 - 60), 1));
                }

                boxList.remove(box);
                return;
            }
        }
    }

    private void coinLogic() {
        for (Coin coin : coinList) {
            if (coin.saveGravitated != coin.gravitated) {
                coin.saveGravitated = coin.gravitated;
                coin.updateTextures();
            }
            if (player.overlaps(coin)) {
                score += coin.value;
                coinList.remove(coin);
                return;
            }
        }
    }

    private void surfaceLogic() {
        List<Entity> entityList = new ArrayList<>();
        entityList.add(player);
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
            for (Coin coin : coinList) {
                if (surface.overlaps(coin)) {
                    if (surface.getEffect().equals("gravity")) {
                        coin.gravitated = true;
                    }
                    if (surface.getEffect().equals("none")) {
                        coin.gravitated = false;
                    }
                }
            }
        }
    }

    private void enemyLive() {
        if (!enemyList.isEmpty()) {
            for (Enemy enemy : enemyList) {
                enemy.animatedObject.draw(game.batch, enemy.x, enemy.y, enemy.width, enemy.height, 20);
                enemy.sightCalibration();
                enemy.attack(player);
                enemy.moveToPlayer(player, surfaceList, entityList, countRenders);

                if (enemy.HP <= 0) {
                    enemyList.remove(enemy);
                    if (player.HP < 4) {
                        player.HP++;
                    } else {
                        switch (enemy.type) {
                            case "ShortAttackEnemy" -> score += 50;
                            case "ShotAttackEnemy" -> score += 100;
                        }
                    }
                    break;
                }
            }
        } else {
            BULLET_LIST.clear();
            music.stop();
            player.balance += score;
            try {
                PLAYER.currentLevel = LevelLoader.getNextLevel();
                LevelLoader.load(LevelLoader.getNextLevel());
                game.setScreen(new LevelsScreen(game, "level"));
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
                bullet.textures.draw(game.batch, bullet.x, bullet.y, bullet.width, bullet.height, 2);
                for (Surface surface : surfaceList) {
                    if (bullet.overlaps(surface) && surface.getEffect().equals("solid")) {
                        bulletIterator.remove();
                        return;
                    }
                }
                for (Enemy enemy : enemyList) {
                    if (bullet.overlaps(enemy) && bullet.type.equals("GOOD")) {
                        enemy.HP -= bullet.damage;
                        bulletIterator.remove();
                        return;
                    }
                }
                for (Box box : boxList) {
                    if (bullet.overlaps(box)) {
                        box.HP -= bullet.damage;
                        bulletIterator.remove();
                        return;
                    }
                }
                if (bullet.overlaps(player) && (bullet.type.equals("BAD") || bullet.type.equals("MASTERS"))) {
                    bulletIterator.remove();
                    if (bullet.type.equals("MASTERS")) {
                        player.HP = 0;
                        return;
                    }
                    player.HP--;
                    return;
                }
            }
        }
    }

    private void ilyaDeath() {
        if (player.HP < 1) {
            music.stop();
            BULLET_LIST.clear();
            game.setScreen(new DeathScreen(game));
        }
    }
}
