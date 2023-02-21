package ru.thedreamingsaviour.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.thedreamingsaviour.game.LevelEditor;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.guiobject.TextWindow;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.resourceloader.SaveLoader;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.getMenuMusic;
import static ru.thedreamingsaviour.game.resourceloader.SaveLoader.PLAYER;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.*;

public class MainMenuScreen implements Screen {
    private final TextWindow startTextWindow = new TextWindow();
    private final TextWindow editorTextWindow = new TextWindow();
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final Stage stage;
    private String exceptionMessage = "";
    private final BitmapFont headFont;
    private final Music music;

    public MainMenuScreen(final MyGdxGame gam) {
        this.game = gam;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        headFont = game.getFont(100, Color.CYAN);
        headFont.getData().scale(1);

        background = getMainMenuBackground();
        music = getMenuMusic();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.getFont(45, new Color(0.9f, 0.9f, 0.9f, 1));

        Button loadGameButton = new TextButton("Продолжить", textButtonStyle);
        stage.addActor(loadGameButton);
        loadGameButton.setPosition(20, 330);

        Button newGameButton = new TextButton("Новая игра", textButtonStyle);
        stage.addActor(newGameButton);
        newGameButton.setPosition(20, 250);

        Button settingsButton = new TextButton("Настройки", textButtonStyle);
        stage.addActor(settingsButton);
        settingsButton.setPosition(20, 170);

        Button exitButton = new TextButton("Выход", textButtonStyle);
        stage.addActor(exitButton);
        exitButton.setPosition(20, 90);


        camera = game.camera;
        camera.setToOrtho(false, 3000, 2500);


        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startTextWindow.call(1500, 1100, 1000, 400, "player");
            }
        });

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exceptionMessage = "WIP";
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.setScreen(new SettingsScreen(game));
                exceptionMessage = "WIP";
            }
        });


        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        headFont.draw(game.batch, "The Dreaming Saviour", 80, 2300);
        game.universalFont.draw(game.batch, exceptionMessage, 80, 210);
        startTextWindow.render(game);
        editorTextWindow.render(game);
        game.batch.end();
        stage.draw();

        if (!(startTextWindow.isRendering() || editorTextWindow.isRendering())) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                editorTextWindow.call(1500, 1100, 1000, 400, "level");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                Gdx.app.exit();
            }
        }
        start();
        startEditor();
    }

    private void start() {
        String player = startTextWindow.getOutputText();
        if (!(player.equals("") || player.equals("new"))) {
            try {
                SaveLoader.load(player);
                LevelLoader.load(PLAYER.currentLevel);
                game.setScreen(new LevelsScreen(game, "level"));
                music.stop();
            } catch (Exception exception) {
                exception.printStackTrace();
                exceptionMessage = "player not found";
            }
        }
    }

    private void startEditor() {
        String level = editorTextWindow.getOutputText();
        if (!level.equals("")) {
            try {
                LevelLoader.load(level);
                game.setScreen(new LevelEditor(game));
                music.stop();
            } catch (Exception exception) {
                exception.printStackTrace();
                exceptionMessage = "level not found";
            }
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
