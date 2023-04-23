package ru.thedreamingsaviour.game.logics.bossbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.*;
import ru.thedreamingsaviour.game.gameobject.entity.Box;
import ru.thedreamingsaviour.game.gameobject.entity.Enemy;
import ru.thedreamingsaviour.game.gameobject.entity.Entity;
import ru.thedreamingsaviour.game.gameobject.entity.Player;
import ru.thedreamingsaviour.game.logics.GameLogic;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.screen.DeathScreen;
import ru.thedreamingsaviour.game.screen.LevelsScreen;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;
import ru.thedreamingsaviour.game.utility.SwitchHandler;

import java.util.*;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.getRexDuodecimAngelusMusic;
import static ru.thedreamingsaviour.game.resourceloader.SaveLoader.PLAYER;
import static ru.thedreamingsaviour.game.resourceloader.SoundLoader.DAMAGE;

public class Kahaousil implements GameLogic {
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
    private final PlatformHandler platformHandler;

    public Kahaousil(final MyGdxGame game) {
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
        music = getRexDuodecimAngelusMusic();
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        player.heal();

        entityList.clear();
        entityList.add(player);
        entityList.addAll(enemyList);
        entityList.addAll(boxList);

        entityList.forEach(entity -> entity.BULLET_LIST = BULLET_LIST);
        switchSurface();

        List<Surface> platforms = new ArrayList<>();
        for (Surface surface : surfaceList) {
            if (surface.id == 2) {
                platforms.add(surface);
            }
        }

        platformHandler = new PlatformHandler(platforms);
    }

    @Override
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


        ilyaDeath();
        exitLevel();

        platformHandler.rotatePlatforms();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            music.stop();
        }
    }

    private void switchSurface() {
        for (Surface surface : surfaceList) {
            if (surface.id == 1 && surface.getEffect().equals("gravity")) {
                surface.setEffect("none");
                surface.setStandardColor("1;0.8902;0.6941;1");
            } else if (surface.id == 1) {
                surface.setEffect("gravity");
                surface.setStandardColor("0.30;0.56;0.87;1");
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

    private void exitLevel() {
        if (exit != null) {
            if (player.overlaps(exit)) {
                nextLevel();
            }
        }
    }

    private void nextLevel() {
        BULLET_LIST.clear();
        player.timeFall = 0;
        music.stop();
        try {
            PLAYER.currentLevel = LevelLoader.getNextLevel();
            LevelLoader.load(LevelLoader.getNextLevel());
            if (LevelLoader.isBoss()) {
                game.setScreen(new LevelsScreen(game, LevelLoader.getLevelName()));
            } else {
                game.setScreen(new LevelsScreen(game, "level"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static class PlatformHandler {
        float x = 2100;
        float y = 4500;
        int radius = 1000;

        List<Surface> platforms;
        List<Point> circle = new ArrayList<>();

        PlatformHandler(List<Surface> platforms) {
            this.platforms = platforms;
            createCircle();
            setPlatforms();
        }

        void createCircle() {
            for (int i = 0; i < 4; i++) {
                List<Point> points = new ArrayList<>();
                int xPoint = 0;
                int yPoint = radius;
                int gap;
                int delta = (2 - 2 * radius);

                while (yPoint >= 0) {
                    if (i == 0) {
                        points.add(new Point(x + xPoint, y + yPoint));
                    }
                    if (i == 1) {
                        points.add(new Point(x - xPoint, y + yPoint));
                    }
                    if (i == 2) {
                        points.add(new Point(x - xPoint, y - yPoint));
                    }
                    if (i == 3) {
                        points.add(new Point(x + xPoint, y - yPoint));
                    }
                    gap = 2 * (delta + yPoint) - 1;
                    if (delta < 0 && gap <= 0) {
                        xPoint++;
                        delta += 2 * xPoint + 1;
                        continue;
                    }
                    if (delta > 0 && gap > 0) {
                        yPoint--;
                        delta -= 2 * yPoint + 1;
                        continue;
                    }
                    xPoint++;
                    delta += 2 * (xPoint - yPoint);
                    yPoint--;
                }
                if (i == 0 || i == 2) {
                    Collections.reverse(points);
                }
                circle.addAll(points);
            }
        }

        void setPlatforms() {
            for (Surface ignored : platforms) {
                circle.get(0).target = true;
                circle.get(circle.size() - 1).target = true;

                List<Integer> distances = new ArrayList<>();
                Map<Integer, Point> distanceAndPoints = new HashMap<>();

                List<Point> targetPoints = new ArrayList<>();
                for (int i = circle.size() - 1; i >= 0; i--) {
                    if (circle.get(i).target) {
                        targetPoints.add(circle.get(i));
                    }
                }

                for (int i = 0; i < targetPoints.size() - 1; i++) {
                    int distance = circle.indexOf(targetPoints.get(i)) - circle.indexOf(targetPoints.get(i + 1));
                    if (circle.indexOf(targetPoints.get(i)) == circle.size() - 1) {
                        distance++;
                    }
                    distances.add(distance);
                    distanceAndPoints.put(distance, targetPoints.get(i));
                }

                int distance = 0;
                for (int d : distances) {
                    if (d > distance) {
                        distance = d;
                    }
                }

                int half = distance / 2;

                int index = circle.indexOf(distanceAndPoints.get(distance)) - half;

                circle.get(index).target = true;
            }
            for (Surface surface : platforms) {
                int i = circle.indexOf(circle.stream().filter(a -> a.target).findFirst().get());
                surface.x = circle.get(i).x;
                surface.y = circle.get(i).y;
                circle.get(i).target = false;
            }
        }

        void rotatePlatforms() {
            List<Point> linkedPoints = new ArrayList<>();
            for (Surface platform : platforms) {
                for (Point point : circle) {
                    if (platform.x == point.x && platform.y == point.y) {
                        linkedPoints.add(point);
                        break;
                    }
                }
            }

            for (int i = 0; i < platforms.size(); i++) {
                int index = circle.indexOf(linkedPoints.get(i));
                index += 10;
                if (index > circle.size() - 1) {
                    index = index - circle.size();
                }
                platforms.get(i).x = circle.get(index).x;
                platforms.get(i).y = circle.get(index).y;
            }
        }

        private static class Point {
            float x;
            float y;
            boolean target;

            Point(float x, float y) {
                this.x = x;
                this.y = y;
            }
        }
    }
}
