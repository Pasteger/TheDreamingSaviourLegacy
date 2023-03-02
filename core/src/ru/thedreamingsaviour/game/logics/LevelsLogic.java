package ru.thedreamingsaviour.game.logics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.*;
import ru.thedreamingsaviour.game.gameobject.entity.*;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.screen.DeathScreen;
import ru.thedreamingsaviour.game.screen.LevelsScreen;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;
import ru.thedreamingsaviour.game.utility.SwitchHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.getFactoryMusic;
import static ru.thedreamingsaviour.game.resourceloader.SaveLoader.PLAYER;
import static ru.thedreamingsaviour.game.resourceloader.SoundLoader.*;

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
    private final List<SwitchHandler> switchHandlerList;
    private final Exit exit;
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
        switchHandlerList = LevelLoader.getSwitchHandlerList();
        exit = LevelLoader.getExit();

        entityList = new ArrayList<>();
        player = PLAYER;
        player.setX(LevelLoader.getStartX());
        player.setY(LevelLoader.getStartY());
        music = getFactoryMusic();
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
        startFPSTime = System.currentTimeMillis();

        player.heal();
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

        switchHandlerList.forEach(switchHandler -> switchHandler.handle(game.batch));

        player.draw(game.batch);
        surfaceLogic();
        enemyLive();
        coinLogic();
        boxLogic();
        bulletLogic();

        if (exit != null) {
            exit.draw(game.batch);
        }

        boxList.forEach(box -> box.draw(game.batch));
        coinList.forEach(coin -> coin.textures.draw(game.batch, coin.x, coin.y, coin.width, coin.height, coin.gravitated ? 5 : 15));

        player.move(surfaceList, entityList);
        game.camera.position.x = player.x;
        game.camera.position.y = player.y;


        game.universalFont.draw(game.batch, "HP: " + player.HP, player.x - 1900, player.y + 1900);
        game.universalFont.draw(game.batch, "score: " + score, player.x - 1900, player.y + 1700);

        ilyaDeath();
        exitLevel();

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
            //System.out.println(fps);
            countRenders = 0;
        }
    }

    private void exitLevel() {
        if (exit != null) {
            if (player.overlaps(exit)) {
                BULLET_LIST.clear();
                player.timeFall = 0;
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

                List<Sound> sounds = DEATH.get("BOX/" + box.getMaterial());
                Sound deathSound = sounds.get(new Random().nextInt(sounds.size()));
                deathSound.play(0.35f);

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
                getPickCoin().play(1);
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
                        boolean overlapsGravity = false;
                        for (Surface surface1 : surfaceList) {
                            if (entity.overlaps(surface1) && surface1.getEffect().equals("gravity")) {
                                overlapsGravity = true;
                            }
                        }
                        if (entity.timeFall != 0 && !overlapsGravity)
                            entity.timeFall = 0;
                        if (entity.jumped != 0 && !overlapsGravity)
                            entity.jumped = 0;
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
            for (SwitchHandler switchHandler : switchHandlerList) {
                for (Switch switcher : switchHandler.getSwitches()) {
                    if (surface.overlaps(switcher)) {
                        if (surface.getEffect().equals("gravity")) {
                            switcher.gravitated = true;
                        }
                        if (surface.getEffect().equals("none")) {
                            switcher.gravitated = false;
                        }
                    }
                }
            }
        }
    }

    private void enemyLive() {
        if (!enemyList.isEmpty()) {
            for (Enemy enemy : enemyList) {
                enemy.draw(game.batch);
                enemy.sightCalibration();
                enemy.attack(player);
                enemy.moveToPlayer(player, surfaceList, entityList, countRenders);

                if (enemy.HP <= 0) {
                    StringBuilder key = new StringBuilder();
                    key.append(enemy.type.charAt(0));
                    for (int i = 1; i < enemy.type.length(); i++) {
                        key.append(Character.isUpperCase(enemy.type.charAt(i)) ?
                                "_" + enemy.type.charAt(i) : Character.toUpperCase(enemy.type.charAt(i)));
                    }

                    List<Sound> sounds = DEATH.get("ENEMY/" + key);
                    Sound deathSound = sounds.get(new Random().nextInt(sounds.size()));
                    deathSound.play(0.8f);
                    enemyList.remove(enemy);
                    if (player.HP < player.saveHP) {
                        player.takeHeal(1);
                    } else {
                        switch (enemy.type) {
                            case "ShortAttackEnemy" -> score += 50;
                            case "ShotAttackEnemy" -> score += 100;
                        }
                    }
                    break;
                }
            }
        } else if (exit == null) {
            BULLET_LIST.clear();
            player.timeFall = 0;
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
                bullet.move(bullet.getTargetAim());

                bullet.textures.draw(game.batch, bullet.x, bullet.y, bullet.width, bullet.height, 2);
                for (Surface surface : surfaceList) {
                    if (bullet.overlaps(surface) && surface.getEffect().equals("solid")) {
                        bulletIterator.remove();
                        return;
                    }
                }
                for (Enemy enemy : enemyList) {
                    if (bullet.overlaps(enemy) && bullet.type.equals("GOOD")) {
                        enemy.takeDamage(bullet.damage);
                        bulletIterator.remove();
                        return;
                    }
                }
                for (Box box : boxList) {
                    if (bullet.overlaps(box)) {
                        box.takeDamage(bullet.damage);
                        bulletIterator.remove();
                        return;
                    }
                }
                if (bullet.overlaps(player) && bullet.type.equals("BAD")) {
                    bulletIterator.remove();
                    player.takeDamage(bullet.damage);
                    return;
                }
                for (SwitchHandler switchHandler : switchHandlerList) {
                    for (Switch switcher : switchHandler.getSwitches()) {
                        if (bullet.overlaps(switcher)) {
                            switcher.toggle();
                            bulletIterator.remove();
                        }
                    }
                }
            }
        }
    }

    private void ilyaDeath() {
        if (player.HP < 1) {
            List<Sound> sounds = DAMAGE.get("PLAYER");
            Sound damageSound = sounds.get(new Random().nextInt(sounds.size()));
            damageSound.play(0.8f);
            music.stop();
            BULLET_LIST.clear();
            player.timeFall = 0;
            game.setScreen(new DeathScreen(game));
        }
    }
}
