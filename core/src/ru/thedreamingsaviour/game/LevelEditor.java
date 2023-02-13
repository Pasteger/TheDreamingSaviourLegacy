package ru.thedreamingsaviour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.thedreamingsaviour.game.gameobject.*;
import ru.thedreamingsaviour.game.gameobject.entity.*;
import ru.thedreamingsaviour.game.guiobject.TextWindow;
import ru.thedreamingsaviour.game.resourceloader.LevelSaver;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.utility.SwitchHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.thedreamingsaviour.game.resourceloader.SaveLoader.PLAYER;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.*;
import static ru.thedreamingsaviour.game.utility.SurfaceListSorter.sortSurfaceList;

public class LevelEditor implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final TextWindow saveTextWindow;
    private final TextWindow colorTextWindow;
    private final TextWindow effectTextWindow;

    private final Player player;
    private final List<Enemy> enemyList;
    private List<Surface> surfaceList;
    private final List<Coin> coinList;
    private final List<Box> boxList;
    private final List<DecorObject> decorList;
    private final List<SwitchHandler> switchHandlerList;
    private Exit exit;

    private boolean dragged;
    private String currentTask;
    private int deltaX;
    private int deltaY;
    private Surface currentSurface;
    private Rectangle currentEntity;
    private DecorObject currentDecorObject;
    private String currentSurfaceColor;
    private String currentSurfaceEffect;
    private final AnimatedObject demoAddedObject = new AnimatedObject();
    private int currentCoinValue = 1;
    private byte currentBoxHP = 1;
    private byte currentAnimatedSpeed = 10;
    private String currentBoxMaterial = "WOODEN";
    private String currentDecorTexture = "STEAM_HAMMER";
    private long currentSurfaceId = 0;
    private int currentSwitchHandlerIndex;
    private Switch currentSwitch;

    public LevelEditor(final MyGdxGame game) {
        this.game = game;
        saveTextWindow = new TextWindow();
        colorTextWindow = new TextWindow();
        effectTextWindow = new TextWindow();
        currentTask = "";
        currentSurfaceColor = "0.5;0.5;0.5;1";
        currentSurfaceEffect = "none";
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        camera = game.camera;
        camera.setToOrtho(false, 4000, 4000);

        player = PLAYER;

        enemyList = LevelLoader.getEnemyList();
        surfaceList = LevelLoader.getSurfaceList();
        coinList = LevelLoader.getCoinList();
        boxList = LevelLoader.getBoxList();
        decorList = LevelLoader.getDecorList();
        switchHandlerList = LevelLoader.getSwitchHandlerList();
        exit = LevelLoader.getExit();

        demoAddedObject.setTextures(player.animatedObject.getTextures());

        Gdx.input.setInputProcessor(new EditorInputProcessor());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        surfaceList.stream().filter(surface ->
                !(surface.getEffect().equals("solid") || surface.getEffect().equals("draw_over"))).forEach(surface -> surface.draw(game.batch));

        decorList.forEach(decorObject -> decorObject.draw(game.batch));

        surfaceList.stream().filter(surface ->
                (surface.getEffect().equals("solid") || surface.getEffect().equals("draw_over"))).forEach(surface -> surface.draw(game.batch));

        switchHandlerList.forEach(switchHandler -> switchHandler.handle(game.batch));

        if (exit != null) {
            exit.draw(game.batch);
        }

        coinList.forEach(coin -> coin.textures.draw(game.batch, coin.x, coin.y, coin.width, coin.height, 5));
        boxList.forEach(box -> box.animatedObject.draw(game.batch, box.x, box.y, box.width, box.height, 10));

        enemyList.forEach(enemy -> enemy.animatedObject.draw(game.batch, enemy.x, enemy.y, enemy.width, enemy.height, 20));

        player.animatedObject.draw(game.batch, player.x, player.y, player.width, player.height, 20);

        game.universalFont.draw(game.batch, camera.position.x + "  " + camera.position.y, camera.position.x - 2000, camera.position.y + 1900);
        game.universalFont.draw(game.batch, currentTask, camera.position.x - 2000, camera.position.y + 1700);
        demoAddedObject.draw(game.batch, camera.position.x - 2000, camera.position.y + 1250, 250, 250, currentAnimatedSpeed);

        String currentValue = "";
        switch (currentTask) {
            case "addCoin", "deleteCoin" -> currentValue = "Coin: " + currentCoinValue;
            case "addBox", "removeBox" -> currentValue = "Box: " + currentBoxHP + " | " + currentBoxMaterial;
            case "addDecor", "deleteDecor" -> currentValue = "Speed: " + currentAnimatedSpeed;
            case "SwitchHandler" -> {
                if (!switchHandlerList.isEmpty()) {
                    currentValue = "ID: " + currentSwitchHandlerIndex + " switches: " +
                            switchHandlerList.get(currentSwitchHandlerIndex).getSwitches().size() + " sID: " +
                            switchHandlerList.get(currentSwitchHandlerIndex).getSurfacesId();
                }
            }
            default -> currentValue = "Effect: " + currentSurfaceEffect + " id:" + currentSurfaceId;
        }
        game.universalFont.draw(game.batch, currentValue, camera.position.x - 2000, camera.position.y + 1200);

        saveTextWindow.render(game);
        colorTextWindow.render(game);
        effectTextWindow.render(game);
        game.batch.end();

        if (dragged) {
            camera.position.x += (float) deltaX / 10;
            camera.position.y -= (float) deltaY / 10;
        }
        if (!(saveTextWindow.isRendering() || colorTextWindow.isRendering() || effectTextWindow.isRendering())) {
            buttons();
            handleTask();
        }
        save();
        changeColor();
        changeEffect();
    }

    private void demoAddedObjectSetTextures(List<Texture> textures) {
        demoAddedObject.setTextures(textures);
        demoAddedObject.setSpriteColor(1, 1, 1, 1);
    }

    private void demoAddedObjectSetTextures(String color) {
        List<Texture> textures = new ArrayList<>();
        textures.add(getNullTexture());
        demoAddedObject.setTextures(textures);
        String[] rgbaS = color.split(";");
        demoAddedObject.setSpriteColor(
                Float.parseFloat(rgbaS[0]), Float.parseFloat(rgbaS[1]),
                Float.parseFloat(rgbaS[2]), Float.parseFloat(rgbaS[3]));
    }

    private void buttons() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            currentTask = !currentTask.equals("addDrawingSurface") ? "addDrawingSurface" : "edit";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            currentTask = !currentTask.equals("spawnEnemy") ? "spawnEnemy" : "edit";
            demoAddedObjectSetTextures(currentTask.equals("spawnEnemy") ? SHORT_ATTACK_ENEMY.get("NORTH") : PLAYER_TEXTURES.get("NORTH"));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            currentTask = !currentTask.equals("deleteEnemy") ? "deleteEnemy" : "edit";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            currentTask = !currentTask.equals("addWall") ? "addWall" : "edit";
            currentSurfaceEffect = "solid";
            currentSurfaceColor = "0;0;0;1";
            if ("addWall".equals(currentTask)) {
                demoAddedObjectSetTextures(currentSurfaceColor);
            } else {
                demoAddedObjectSetTextures(PLAYER_TEXTURES.get("NORTH"));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            if (currentSurfaceColor.equals("0;0;0;1")) {
                currentSurfaceColor = "0.36;0.63;0.19;1";
            } else if (currentSurfaceColor.equals("0.36;0.63;0.19;1")) {
                currentSurfaceColor = "1;1;1;1";
            } else {
                currentSurfaceColor = "0;0;0;1";
            }
            demoAddedObjectSetTextures(currentSurfaceColor);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            currentTask = !currentTask.equals("addFloor") ? "addFloor" : "edit";
            currentSurfaceEffect = "none";
            currentSurfaceColor = "1;0.8902;0.6941;1";
            if ("addFloor".equals(currentTask)) {
                demoAddedObjectSetTextures(currentSurfaceColor);
            } else {
                demoAddedObjectSetTextures(PLAYER_TEXTURES.get("NORTH"));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            currentTask = !currentTask.equals("addSky") ? "addSky" : "edit";
            currentSurfaceEffect = "gravity";
            currentSurfaceColor = "0.30;0.56;0.87;1";
            if ("addSky".equals(currentTask)) {
                demoAddedObjectSetTextures(currentSurfaceColor);
            } else {
                demoAddedObjectSetTextures(PLAYER_TEXTURES.get("NORTH"));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            currentTask = !currentTask.equals("removeSurface") ? "removeSurface" : "edit";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            currentTask = !currentTask.equals("edit") ? "edit" : "";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            currentTask = !currentTask.equals("addBox") ? "addBox" : "edit";
            demoAddedObjectSetTextures(currentTask.equals("addBox") ? BOX.get(currentBoxMaterial) : PLAYER_TEXTURES.get("NORTH"));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            currentTask = !currentTask.equals("removeBox") ? "removeBox" : "edit";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            currentTask = !currentTask.equals("SwitchHandler") ? "SwitchHandler" : "";
        }
        if (currentTask.equals("SwitchHandler") && Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
            if (currentSwitchHandlerIndex > 0) {
                currentSwitchHandlerIndex--;
            }
        }
        if (currentTask.equals("SwitchHandler") && Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) {
            if (!switchHandlerList.isEmpty()) {
                switchHandlerList.get(currentSwitchHandlerIndex).getSwitches().add(
                        new Switch("lever", getSynchronizedX(), getSynchronizedY(),
                                200, 200, 10, false));
            }
        }
        if (currentTask.equals("SwitchHandler") && Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            if (currentSwitchHandlerIndex < switchHandlerList.size() - 1) {
                currentSwitchHandlerIndex++;
            }
        }
        if (currentTask.equals("SwitchHandler") && Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            SwitchHandler switchHandler = new SwitchHandler(currentSurfaceId);
            switchHandlerList.add(switchHandler);
            currentSwitchHandlerIndex = switchHandlerList.indexOf(switchHandler);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            currentTask = !currentTask.equals("addExit") ? "addExit" : "edit";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            currentTask = !currentTask.equals("removeExit") ? "removeExit" : "edit";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            surfaceList = sortSurfaceList(surfaceList);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            currentTask = "change color...";
            colorTextWindow.call((int) camera.position.x - 1000, (int) camera.position.y, 2000, 400, "Color");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            currentTask = "change effect...";
            effectTextWindow.call((int) camera.position.x - 1000, (int) camera.position.y, 2000, 400, "Effect");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            currentTask = !currentTask.equals("addCoin") ? "addCoin" : "edit";
            demoAddedObjectSetTextures(currentTask.equals("addCoin") ? COINS.get(String.valueOf(currentCoinValue)) : PLAYER_TEXTURES.get("NORTH"));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            currentTask = !currentTask.equals("deleteCoin") ? "deleteCoin" : "edit";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            switch (currentCoinValue) {
                case 1 -> currentCoinValue = 10;
                case 10 -> currentCoinValue = 100;
                case 100 -> currentCoinValue = 1000;
                case 1000 -> currentCoinValue = 5000;
                case 5000 -> currentCoinValue = 1;
            }
            demoAddedObjectSetTextures(currentTask.equals("addCoin") ? COINS.get(String.valueOf(currentCoinValue)) : PLAYER_TEXTURES.get("NORTH"));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            currentBoxHP++;
            if (currentBoxHP > 30) {
                currentBoxHP = 1;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            switch (currentBoxMaterial) {
                case "WOODEN" -> currentBoxMaterial = "STEEL";
                case "STEEL" -> currentBoxMaterial = "BARREL";
                case "BARREL" -> currentBoxMaterial = "WOODEN";
            }
            demoAddedObjectSetTextures(currentTask.equals("addBox") ? BOX.get(currentBoxMaterial) : PLAYER_TEXTURES.get("NORTH"));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            currentTask = !currentTask.equals("addDecor") ? "addDecor" : "edit";
            demoAddedObjectSetTextures(currentTask.equals("addDecor") ? DECOR.get(currentDecorTexture) : PLAYER_TEXTURES.get("NORTH"));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            currentTask = !currentTask.equals("deleteDecor") ? "deleteDecor" : "edit";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            switch (currentDecorTexture) {
                case "STEAM_HAMMER" -> currentDecorTexture = "CISTERN";
                case "CISTERN" -> currentDecorTexture = "PIPE";
                case "PIPE" -> currentDecorTexture = "PORTAL";
                case "PORTAL" -> currentDecorTexture = "STEAM_HAMMER";
            }
            demoAddedObjectSetTextures(currentTask.equals("addDecor") ? DECOR.get(currentDecorTexture) : PLAYER_TEXTURES.get("NORTH"));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            currentAnimatedSpeed++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            if (currentAnimatedSpeed > 1) {
                currentAnimatedSpeed--;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            currentSurfaceId++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            if (currentSurfaceId > 0) {
                currentSurfaceId--;
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            currentTask = "save...";
            saveTextWindow.call((int) camera.position.x - 1000, (int) camera.position.y, 2000, 400, "levelName nextLevel");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void handleTask() {
        if (currentTask.equals("addExit") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            exit = new Exit("PORTAL", getSynchronizedX() - 60, getSynchronizedY() - 60, 300, 300, currentAnimatedSpeed);
            currentDecorObject = exit;
        }
        if (currentTask.equals("removeExit") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            exit = null;
        }
        if (currentTask.equals("addCoin") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Coin coin = new Coin(getSynchronizedX() - 60, getSynchronizedY() - 60, currentCoinValue);
            coinList.add(coin);
        }
        if (currentTask.equals("deleteCoin") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float y = getSynchronizedY();
            float x = getSynchronizedX();
            for (Coin coin : coinList) {
                if (coin.getX() >= x - coin.getWidth() && coin.getX() <= x + 1 &&
                        coin.getY() >= y - coin.getHeight() && coin.getY() <= y + 1) {
                    coinList.remove(coin);
                    break;
                }
            }
        }

        if (currentTask.equals("removeBox") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float y = getSynchronizedY();
            float x = getSynchronizedX();
            for (Box box : boxList) {
                if (box.getX() >= x - box.getWidth() && box.getX() <= x + 1 &&
                        box.getY() >= y - box.getHeight() && box.getY() <= y + 1) {
                    boxList.remove(box);
                    break;
                }
            }
        }

        if (currentTask.equals("deleteDecor") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float y = getSynchronizedY();
            float x = getSynchronizedX();
            for (DecorObject decor : decorList) {
                if (decor.getX() >= x - decor.getWidth() && decor.getX() <= x + 1 &&
                        decor.getY() >= y - decor.getHeight() && decor.getY() <= y + 1) {
                    decorList.remove(decor);
                    break;
                }
            }
        }

        if (currentTask.equals("addBox") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Box box = new Box(getSynchronizedX() - 100, getSynchronizedY() - 100,
                    200, 200, currentBoxMaterial, currentBoxHP);
            boxList.add(box);
        }

        if (currentTask.equals("addDecor") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            DecorObject decor = new DecorObject(currentDecorTexture, (int) getSynchronizedX() - 100,
                    (int) getSynchronizedY() - 100, 200, 200, currentAnimatedSpeed);
            decorList.add(decor);
        }

        if ((currentTask.equals("addFloor") || currentTask.equals("addWall") || currentTask.equals("addSky")
                || currentTask.equals("addDrawingSurface"))
                && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Surface surface = new Surface(getSynchronizedX() - 100, getSynchronizedY() - 100,
                    200, 200, currentSurfaceEffect, currentSurfaceColor, currentSurfaceId);
            surfaceList.add(surface);
        }

        if (currentTask.equals("edit") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
                (currentSurface == null && currentEntity == null && currentDecorObject == null && currentSwitch == null)) {
            setCurrentObject();
        }
        if (currentTask.equals("edit") && Gdx.input.isKeyJustPressed(Input.Keys.ENTER) &&
                (currentSurface != null || currentEntity != null || currentDecorObject != null || currentSwitch != null)) {
            if (currentSurface != null) {
                currentSurface.setStandardColor();
                currentSurface = null;
            }
            if (currentEntity != null) {
                currentEntity = null;
            }
            if (currentDecorObject != null) {
                currentDecorObject = null;
            }
            if (currentSwitch != null) {
                currentSwitch = null;
            }
            currentTask = "";
        }

        if (currentTask.equals("spawnEnemy") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Enemy slave = new ShortAttackEnemy();
            slave.setX(getSynchronizedX());
            slave.setY(getSynchronizedY());
            enemyList.add(slave);
        }
        if (currentTask.equals("deleteEnemy") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float x = getSynchronizedX();
            float y = getSynchronizedY();
            enemyList.removeIf(enemy -> (
                    enemy.getX() >= x - enemy.getWidth() && enemy.getX() <= x + 1 &&
                            enemy.getY() >= y - enemy.getHeight() && enemy.getY() <= y + 1
            ));
        }

        if (currentTask.equals("removeSurface") && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float y = getSynchronizedY();
            float x = getSynchronizedX();
            Collections.reverse(surfaceList);
            for (Surface surface : surfaceList) {
                if (surface.getX() >= x - surface.getWidth() && surface.getX() <= x + 1 &&
                        surface.getY() >= y - surface.getHeight() && surface.getY() <= y + 1) {
                    surfaceList.remove(surface);
                    break;
                }
            }
            surfaceList = sortSurfaceList(surfaceList);
        }
    }

    private void setCurrentObject() {
        List<Rectangle> entities = new ArrayList<>();
        entities.addAll(enemyList);
        entities.addAll(boxList);
        entities.addAll(coinList);
        entities.add(player);
        float x = getSynchronizedX();
        float y = getSynchronizedY();

        for (Rectangle entity : entities) {
            if (findObjectFromCord(entity, x, y)) {
                currentEntity = entity;
                return;
            }
        }
        for (DecorObject decorObject : decorList) {
            if (findObjectFromCord(decorObject, x, y)) {
                currentDecorObject = decorObject;
                return;
            }
        }
        if (!switchHandlerList.isEmpty()) {
            for (Switch swch : switchHandlerList.get(currentSwitchHandlerIndex).getSwitches()) {
                if (findObjectFromCord(swch, x, y)) {
                    currentSwitch = swch;
                    return;
                }
            }
        }
        for (int i = surfaceList.size() - 1; i >= 0; i--) {
            if (findObjectFromCord(surfaceList.get(i), x, y)) {
                currentSurface = surfaceList.get(i);
                currentSurface.setEditableColor();
                Collections.swap(surfaceList, surfaceList.indexOf(surfaceList.get(i)), surfaceList.size() - 1);
                return;
            }
        }
    }

    private void save() {
        String text = saveTextWindow.getOutputText();
        if (!(text.equals("") || text.equals("new"))) {
            try {
                List<ShortAttackEnemy> shortAttackEnemyList = new ArrayList<>();
                enemyList.stream().filter(
                        enemy -> enemy.type.equals("ShortAttackEnemy")).forEach(
                        enemy -> shortAttackEnemyList.add((ShortAttackEnemy) enemy));

                String[] save = text.split(" ");

                LevelSaver.save(surfaceList, shortAttackEnemyList, coinList, boxList, decorList, switchHandlerList,
                        exit, player.x, player.y, save[1], save[0]);

                currentTask = "saved!";
            } catch (Exception exception) {
                currentTask = "save exception";
            }
        }
    }

    private void changeColor() {
        String text = colorTextWindow.getOutputText();
        if (!text.equals("")) {
            try {
                String[] rgbaS = text.split(";");
                Float[] rgba = new Float[3];
                for (int i = 0; i < 3; i++) {
                    rgba[i] = Float.parseFloat(rgbaS[i]) / 255;
                }
                text = rgba[0] + ";" + rgba[1] + ";" + rgba[2] + ";" + rgbaS[3];

                currentSurfaceColor = text;
                demoAddedObjectSetTextures(text);
                currentTask = "color set";
            } catch (Exception exception) {
                demoAddedObjectSetTextures(currentSurfaceColor);
                currentTask = "color exception";
            }
        }
    }

    private void changeEffect() {
        String text = effectTextWindow.getOutputText();
        if (!text.equals("")) {
            try {
                currentSurfaceEffect = text;
                currentTask = "effect set";
            } catch (Exception exception) {
                currentTask = "effect exception";
            }
        }
    }

    private float getSynchronizedX() {
        float coefficientX = 4000f / 800f;
        return (camera.position.x + Gdx.input.getX() * coefficientX) - 2000;
    }

    private float getSynchronizedY() {
        float coefficientY = 4000f / 600f;
        return (camera.position.y + Gdx.input.getY() * coefficientY * -1) + 2000;
    }

    private boolean findObjectFromCord(Rectangle object, float x, float y) {
        return (object.getX() >= x - object.getWidth() && object.getX() <= x + 1 &&
                object.getY() >= y - object.getHeight() && object.getY() <= y + 1);
    }

    class EditorInputProcessor implements InputProcessor {
        private int touchDownX;
        private int touchDownY;
        private boolean extensionCurrentSurface;
        private boolean moveCurrentSurface;
        private boolean moveEntity;
        private boolean moveDecor;
        private boolean extensionDecor;
        private boolean moveSwitch;
        private boolean extensionSwitch;

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button == 1) {
                touchDownX = screenX;
                touchDownY = screenY;
                dragged = true;
            }
            if (button == Input.Buttons.LEFT && currentSurface != null && !extensionCurrentSurface &&
                    getSynchronizedX() >= currentSurface.x &&
                    getSynchronizedX() <= currentSurface.x + currentSurface.width / 2 &&
                    getSynchronizedY() >= currentSurface.y &&
                    getSynchronizedY() <= currentSurface.y + currentSurface.height / 2) {
                moveCurrentSurface = true;
            }
            if (button == Input.Buttons.LEFT && currentSurface != null && !moveCurrentSurface &&
                    getSynchronizedX() >= currentSurface.x + currentSurface.width - currentSurface.width / 2 &&
                    getSynchronizedX() <= currentSurface.x + currentSurface.width &&
                    getSynchronizedY() >= currentSurface.y + currentSurface.height - currentSurface.height / 2 &&
                    getSynchronizedY() <= currentSurface.y + currentSurface.height) {
                extensionCurrentSurface = true;
            }

            if (button == Input.Buttons.LEFT && currentEntity != null && !moveEntity &&
                    getSynchronizedX() >= currentEntity.x &&
                    getSynchronizedX() <= currentEntity.x + currentEntity.width &&
                    getSynchronizedY() >= currentEntity.y &&
                    getSynchronizedY() <= currentEntity.y + currentEntity.height) {
                moveEntity = true;
            }

            if (button == Input.Buttons.LEFT && currentDecorObject != null && !extensionDecor &&
                    getSynchronizedX() >= currentDecorObject.x &&
                    getSynchronizedX() <= currentDecorObject.x + currentDecorObject.width / 2 &&
                    getSynchronizedY() >= currentDecorObject.y &&
                    getSynchronizedY() <= currentDecorObject.y + currentDecorObject.height / 2) {
                moveDecor = true;
            }
            if (button == Input.Buttons.LEFT && currentDecorObject != null && !moveDecor &&
                    getSynchronizedX() >= currentDecorObject.x + currentDecorObject.width - currentDecorObject.width / 2 &&
                    getSynchronizedX() <= currentDecorObject.x + currentDecorObject.width &&
                    getSynchronizedY() >= currentDecorObject.y + currentDecorObject.height - currentDecorObject.height / 2 &&
                    getSynchronizedY() <= currentDecorObject.y + currentDecorObject.height) {
                extensionDecor = true;
            }
            if (button == Input.Buttons.LEFT && currentSwitch != null && !extensionSwitch &&
                    getSynchronizedX() >= currentSwitch.x &&
                    getSynchronizedX() <= currentSwitch.x + currentSwitch.width / 2 &&
                    getSynchronizedY() >= currentSwitch.y &&
                    getSynchronizedY() <= currentSwitch.y + currentSwitch.height / 2) {
                moveSwitch = true;
            }
            if (button == Input.Buttons.LEFT && currentSwitch != null && !moveSwitch &&
                    getSynchronizedX() >= currentSwitch.x + currentSwitch.width - currentSwitch.width / 2 &&
                    getSynchronizedX() <= currentSwitch.x + currentSwitch.width &&
                    getSynchronizedY() >= currentSwitch.y + currentSwitch.height - currentSwitch.height / 2 &&
                    getSynchronizedY() <= currentSwitch.y + currentSwitch.height) {
                extensionSwitch = true;
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            dragged = false;
            extensionCurrentSurface = false;
            moveCurrentSurface = false;
            moveEntity = false;
            extensionDecor = false;
            moveDecor = false;
            extensionSwitch = false;
            moveSwitch = false;
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            deltaX = touchDownX - screenX;
            deltaY = touchDownY - screenY;
            if (extensionCurrentSurface) {
                int width = (int) getSynchronizedX() - (int) currentSurface.getX();
                int height = (int) getSynchronizedY() - (int) currentSurface.getY();
                currentSurface.editExtension(width, height);
                currentSurface.setEditableColor();
            }
            if (moveCurrentSurface) {
                currentSurface.editMove(getSynchronizedX(), getSynchronizedY());
                currentSurface.setEditableColor();
            }

            if (moveEntity) {
                currentEntity.x = getSynchronizedX() - currentEntity.width / 2;
                currentEntity.y = getSynchronizedY() - currentEntity.height / 2;
            }

            if (extensionDecor) {
                int width = (int) getSynchronizedX() - (int) currentDecorObject.getX();
                int height = (int) getSynchronizedY() - (int) currentDecorObject.getY();
                currentDecorObject.editExtension(width, height);
            }
            if (moveDecor) {
                currentDecorObject.editMove(getSynchronizedX(), getSynchronizedY());
            }

            if (extensionSwitch) {
                int width = (int) getSynchronizedX() - (int) currentSwitch.getX();
                int height = (int) getSynchronizedY() - (int) currentSwitch.getY();
                currentSwitch.editExtension(width, height);
            }
            if (moveSwitch) {
                currentSwitch.editMove(getSynchronizedX(), getSynchronizedY());
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
