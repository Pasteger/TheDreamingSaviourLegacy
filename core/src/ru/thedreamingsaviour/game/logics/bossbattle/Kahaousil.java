package ru.thedreamingsaviour.game.logics.bossbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

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
import java.util.List;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.getRexDuodecimAngelusMusic;
import static ru.thedreamingsaviour.game.resourceloader.SaveLoader.PLAYER;
import static ru.thedreamingsaviour.game.resourceloader.SoundLoader.DAMAGE;
import static ru.thedreamingsaviour.game.resourceloader.SoundLoader.KAHAOUSIL_SOUNDS;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.EXPLOSION;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.KAHAOUSIL_TEXTURES;

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


    private final Rectangle core = new Rectangle();
    private int kahaousilHP = 100;

    private boolean isGravitationalPlane = true;
    private int animationOfDamage;

    private final Sprite duodecagonSprite = new Sprite(KAHAOUSIL_TEXTURES.get("duodecagon"));
    private final Sprite duodecagonLight = new Sprite(KAHAOUSIL_TEXTURES.get("duodecagon_light"));
    private boolean duodecagonActive;
    private final Rectangle duodecagonHitBox = new Rectangle();
    private float jawYOffset;
    private int jawPhase;
    private final List<PlatformHandler.Point> coreCircle = new ArrayList<>();

    private final Random random = new Random();
    private int recharge = 0;
    private int rechargeOfTakeDamage = 0;
    private boolean isMoveOnCircle;

    private int duodecagonPhase = 0;
    private String cruciformAttackDirection = "NORTH";
    private boolean isExplosion = true;
    private final Sound explosionsSound;
    private boolean explosionsSoundPlay = true;
    private final AnimatedObject explosionsAnimatedObject;
    private long explosionsTime;

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


        List<Surface> platforms = new ArrayList<>();
        for (Surface surface : surfaceList) {
            if (surface.id == 2) {
                platforms.add(surface);
            }
        }

        explosionsSound = KAHAOUSIL_SOUNDS.get("explosions");
        explosionsAnimatedObject = new AnimatedObject(EXPLOSION);

        platformHandler = new PlatformHandler(platforms);

        core.width = 450;
        core.height = 400;
        core.x = 2000;
        core.y = 4000;

        duodecagonHitBox.width = 500;
        duodecagonHitBox.height = 500;
        duodecagonHitBox.x = 2000;
        duodecagonHitBox.y = 4000;

        platformHandler.createCircle(2000, 4000, 800, coreCircle);
        Collections.reverse(coreCircle);
        coreCircle.get(0).target = true;
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

        BULLET_LIST.forEach(bullet -> bullet.textures.draw(game.batch, bullet.x, bullet.y, bullet.width, bullet.height, 2));

        player.move(surfaceList, entityList);
        game.camera.position.x = player.x;
        game.camera.position.y = player.y;


        game.universalFont.draw(game.batch, "HP: " + player.HP, player.x - 1900, player.y + 1900);
        game.universalFont.draw(game.batch, "Kahaousil: " + kahaousilHP, player.x - 1900, player.y + 1700);

        game.batch.draw(kahaousilHP > 50 ? KAHAOUSIL_TEXTURES.get("jaw") : KAHAOUSIL_TEXTURES.get("jaw_broken"), core.x + 62, core.y + jawYOffset);
        if (kahaousilHP > 0) {
            if (animationOfDamage > 0) {
                game.batch.draw(kahaousilHP > 50 ? KAHAOUSIL_TEXTURES.get("eyes_damage") : KAHAOUSIL_TEXTURES.get("eyes_broken_damage"), core.x + 73, core.y + 240);
                animationOfDamage--;
            } else {
                game.batch.draw(kahaousilHP > 50 ? KAHAOUSIL_TEXTURES.get("eyes") : KAHAOUSIL_TEXTURES.get("eyes_damage"), core.x + 73, core.y + 240);
            }
        }
        game.batch.draw(kahaousilHP > 50 ? KAHAOUSIL_TEXTURES.get("skull") : KAHAOUSIL_TEXTURES.get("skull_broken"), core.x, core.y + 50);


        if (duodecagonActive) {
            if (duodecagonHitBox.y == 10000) {
                duodecagonHitBox.x = 2000;
                duodecagonHitBox.y = 4000;
                duodecagonHitBox.width = 500;
                duodecagonHitBox.height = 500;
                duodecagonLight.setBounds(duodecagonHitBox.x, duodecagonHitBox.y, duodecagonHitBox.width, duodecagonHitBox.height);
                duodecagonSprite.setBounds(duodecagonHitBox.x, duodecagonHitBox.y, duodecagonHitBox.width, duodecagonHitBox.height);
            }
            duodecagonLight.draw(game.batch);
            duodecagonSprite.draw(game.batch);
            moveDuodecagon();
        } else {
            if (duodecagonHitBox.y != 10000) {
                duodecagonHitBox.y = 10000;
                duodecagonLight.setBounds(duodecagonHitBox.x, duodecagonHitBox.y, duodecagonHitBox.width, duodecagonHitBox.height);
                duodecagonSprite.setBounds(duodecagonHitBox.x, duodecagonHitBox.y, duodecagonHitBox.width, duodecagonHitBox.height);
            }
        }

        kahaousilMove();
        kahaousilShot();
        playerCheckDamage();

        playerDeath();
        exitLevel();

        if (!duodecagonActive) {
            platformHandler.rotatePlatforms();
        } else {
            platformHandler.stopPlatforms();
        }

        if (jawYOffset < -3000 && exit != null) {
            exit.setX(2075);
            exit.setY(3500);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            music.stop();
        }
    }

    private void playerCheckDamage(){
        if (player.overlaps(core) && rechargeOfTakeDamage == 0 && kahaousilHP > 0) {
            player.takeDamage(1);
            rechargeOfTakeDamage = 20;
        }
        if (player.overlaps(duodecagonHitBox) && rechargeOfTakeDamage == 0) {
            player.takeDamage(5);
            rechargeOfTakeDamage = 20;
        }
        if (rechargeOfTakeDamage > 0) {
            rechargeOfTakeDamage--;
        }
    }

    private void kahaousilShot(){
        try {
            if (random.nextInt(25 + kahaousilHP) == 1 && recharge == 0 && kahaousilHP > 0) {
                BULLET_LIST.add(createBullet("bullet", "moveTo", 3, 158));
                KAHAOUSIL_SOUNDS.get("shot").play();
                recharge = 20;
            }
            if (recharge > 0) {
                recharge--;
            }
        } catch (Exception ignored) {
        }
    }

    private void kahaousilMove(){
        if (kahaousilHP > 80) {
            moveCore();
            moveJaw(1, 100);
        } else if (kahaousilHP > 70) {
            if (core.x == 2000 && core.y == 4000) {
                if (isGravitationalPlane) {
                    switchSurface();
                }
                duodecagonActive = true;
                moveJaw(10, 14);
                if (duodecagonHitBox.width > 2100) {
                    cruciformAttack();
                }
            } else {
                moveToCenter();
                moveJaw(2, 90);
            }
        } else if (kahaousilHP > 60) {
            duodecagonActive = false;
            duodecagonPhase = 0;
            if (!isMoveOnCircle) {
                if (!isGravitationalPlane) {
                    switchSurface();
                }
                moveToCircle();
                moveJaw(2, 90);
            } else {
                moveCore();
                moveJaw(2, 100);
            }
        } else if (kahaousilHP > 50) {
            if (core.x == 2000 && core.y == 4000) {
                if (isGravitationalPlane) {
                    switchSurface();
                }
                duodecagonActive = true;
                moveJaw(10, 14);
                if (duodecagonHitBox.width > 2100) {
                    cruciformAttack();
                }
            } else {
                moveToCenter();
                moveJaw(2, 90);
            }
        } else if (kahaousilHP > 10) {
            duodecagonActive = false;
            duodecagonPhase = 0;
            if (!isMoveOnCircle) {
                if (!isGravitationalPlane) {
                    switchSurface();
                }
                moveToCircle();
                moveJaw(2, 90);
            } else {
                moveCore();
                moveJaw(2, 100);
            }
        } else if (kahaousilHP > 0) {
            if (core.x == 2000 && core.y == 4000) {
                if (isGravitationalPlane) {
                    switchSurface();
                }
                duodecagonActive = true;
                moveJaw(10, 14);
                if (duodecagonHitBox.width > 2100) {
                    cruciformAttack();
                }
            } else {
                moveToCenter();
                moveJaw(2, 90);
            }
        } else {
            duodecagonActive = false;
            duodecagonPhase = 0;
            if (!isGravitationalPlane) {
                switchSurface();
            }
            if (isExplosion) {
                if (explosionsTime == 0) {
                    explosionsTime = System.currentTimeMillis();
                }
                explosions();
                if (System.currentTimeMillis() - explosionsTime > 4000) {
                    isExplosion = false;
                }
            } else {
                fallJaw();
            }
        }
    }

    private void explosions(){
        if (explosionsSoundPlay) {
            explosionsSound.play();
            explosionsSoundPlay = false;
        }
        explosionsAnimatedObject.draw(game.batch, 2000, 4000, 500, 500, 3);
        explosionsAnimatedObject.draw(game.batch, 1900, 3900, 400, 400, 2);
        explosionsAnimatedObject.draw(game.batch, 2200, 3800, 300, 300, 1);
    }

    private void fallJaw() {
        if (jawYOffset > -10000) {
            jawYOffset -= 25;
        }
    }

    private void cruciformAttack() {
        BULLET_LIST.add(createBullet("fireball", cruciformAttackDirection, 1, 500));
        switch (cruciformAttackDirection) {
            case "NORTH" -> cruciformAttackDirection = "WEST";
            case "WEST" -> cruciformAttackDirection = "SOUTH";
            case "SOUTH" -> cruciformAttackDirection = "EAST";
            case "EAST" -> cruciformAttackDirection = "NORTH";
        }
    }

    private void moveDuodecagon() {
        int radius = 9;
        if (duodecagonPhase < 100) {
            duodecagonHitBox.x -= radius;
            duodecagonHitBox.y -= radius;
            duodecagonHitBox.width += radius * 2;
            duodecagonHitBox.height += radius * 2;
        } else if (duodecagonPhase < 200) {
            duodecagonHitBox.x += radius;
            duodecagonHitBox.y += radius;
            duodecagonHitBox.width -= radius * 2;
            duodecagonHitBox.height -= radius * 2;
        } else if (duodecagonPhase < 300) {
            duodecagonHitBox.x -= radius;
            duodecagonHitBox.y -= radius;
            duodecagonHitBox.width += radius * 2;
            duodecagonHitBox.height += radius * 2;
        } else {
            duodecagonPhase = 100;
        }
        duodecagonPhase++;
        duodecagonLight.setBounds(duodecagonHitBox.x, duodecagonHitBox.y, duodecagonHitBox.width, duodecagonHitBox.height);
        duodecagonSprite.setBounds(duodecagonHitBox.x, duodecagonHitBox.y, duodecagonHitBox.width, duodecagonHitBox.height);
    }

    private void moveToCircle() {
        if (core.x > coreCircle.get(0).x) {
            core.x -= 10;
        }
        if (core.x < coreCircle.get(0).x) {
            core.x += 10;
        }
        if (core.y > coreCircle.get(0).y) {
            core.y -= 10;
        }
        if (core.y < coreCircle.get(0).y) {
            core.y += 10;
        }
        if (Math.abs(core.x - coreCircle.get(0).x) < 30) {
            core.x = coreCircle.get(0).x;
        }
        if (Math.abs(core.y - coreCircle.get(0).y) < 30) {
            core.y = coreCircle.get(0).y;
        }
        if (core.x == coreCircle.get(0).x && core.y == coreCircle.get(0).y) {
            isMoveOnCircle = true;
        }
    }

    private void moveToCenter() {
        for (PlatformHandler.Point point : coreCircle) {
            point.target = false;
        }
        coreCircle.get(0).target = true;
        isMoveOnCircle = false;
        if (core.x > 2000) {
            core.x -= 10;
        }
        if (core.x < 2000) {
            core.x += 10;
        }
        if (core.y > 4000) {
            core.y -= 10;
        }
        if (core.y < 4000) {
            core.y += 10;
        }
        if (Math.abs(core.x - 2000) < 30) {
            core.x = 2000;
        }
        if (Math.abs(core.y - 4000) < 30) {
            core.y = 4000;
        }
    }

    private void moveCore() {
        for (PlatformHandler.Point pointCircle : coreCircle) {
            if (pointCircle.target) {
                core.x = pointCircle.x;
                core.y = pointCircle.y;
                pointCircle.target = false;
                int index = coreCircle.indexOf(pointCircle) + 20;
                if (index > coreCircle.size() - 1) {
                    coreCircle.get(0).target = true;
                } else {
                    coreCircle.get(index).target = true;
                }
                return;
            }
        }
    }

    private void moveJaw(int speed, int period) {
        if (jawPhase <= period) {
            jawYOffset -= speed;
        } else if (jawPhase <= period * 2) {
            jawYOffset += speed;
        } else {
            jawPhase = 0;
        }
        jawPhase++;
    }

    private Bullet createBullet(String texture, String direction, int damage, float size) {
        Bullet bullet = new Bullet("BAD");

        List<Texture> textures = new ArrayList<>();
        textures.add(KAHAOUSIL_TEXTURES.get(texture));
        bullet.setTextures(new AnimatedObject(textures));

        bullet.height = size;
        bullet.width = size;

        bullet.x = core.x + core.width / 2 - bullet.width / 2;
        bullet.y = core.y + core.height / 2 - bullet.height / 2;

        bullet.direction = direction;

        bullet.damage = damage;

        bullet.setTargetAim(player);

        return bullet;
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
        isGravitationalPlane = !isGravitationalPlane;
    }

    private void takeDamage(int damage) {
        kahaousilHP -= damage;
        animationOfDamage = 20;
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
                if (bullet.overlaps(core) && bullet.type.equals("GOOD")) {
                    if ((!duodecagonActive || duodecagonHitBox.width > 2000) && kahaousilHP > 0) {
                        takeDamage(bullet.damage);
                        bulletIterator.remove();
                        return;
                    }
                }
            }
        }
    }

    private void playerDeath() {
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
            createCircle(x, y, radius, circle);
            setPlatforms();
        }

        public void createCircle(float x, float y, int radius, List<Point> circle) {
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

        void stopPlatforms() {
            int p = circle.size() / platforms.size();
            for (Point point : circle) {
                if (point.target) {
                    point.target = false;
                }
            }
            for (int i = 0; i < platforms.size(); i++) {
                int t = i * p + p / 2;

                circle.get(t).target = true;
            }
            for (Surface surface : platforms) {
                int i = circle.indexOf(circle.stream().filter(a -> a.target).findFirst().get());
                surface.x = circle.get(i).x;
                surface.y = circle.get(i).y;
                circle.get(i).target = false;
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
