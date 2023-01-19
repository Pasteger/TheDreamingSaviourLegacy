package ru.thedreamingsaviour.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.*;

public class MainMenuScreen implements Screen {
    private final TextWindow textWindow = new TextWindow();
    private final TextWindow editorTextWindow = new TextWindow();
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final Stage stage;
    private String exceptionMessage = "";
    private final BitmapFont headFont;

    public MainMenuScreen(final MyGdxGame gam) {
        this.game = gam;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        headFont = game.getFont(100, Color.CYAN);
        headFont.getData().scale(1);

        background = getMainMenuBackground();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.getFont(45, new Color(0.9f, 0.9f, 0.9f, 1));

        Button startButton = new TextButton("Start", textButtonStyle);
        stage.addActor(startButton);
        startButton.setPosition(20, 230);

        Button settingsButton = new TextButton("Settings", textButtonStyle);
        stage.addActor(settingsButton);
        settingsButton.setPosition(20, 150);

        Button exitButton = new TextButton("Exit", textButtonStyle);
        stage.addActor(exitButton);
        exitButton.setPosition(20, 70);


        camera = game.camera;
        camera.setToOrtho(false, 3000, 2500);


        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textWindow.call(1000, 1100, 1000, 400, "level");
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
                throw new Error();
            }
        });
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
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
        textWindow.render(game);
        editorTextWindow.render(game);
        game.batch.end();
        stage.draw();

        if (!(textWindow.isRendering() || editorTextWindow.isRendering())) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                editorTextWindow.call(1000, 1100, 1000, 400, "level");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                throw new Error();
            }
        }
        start();
        startEditor();
    }

    private void start() {
        String level = textWindow.getOutputText();
        if (!(level.equals("") || level.equals("new"))) {
            try {
                LevelLoader.load(level);
                game.setScreen(new LevelsScreen(game));
            } catch (Exception exception) {
                exceptionMessage = "level not found";
            }
        }
    }

    private void startEditor() {
        String level = editorTextWindow.getOutputText();
        if (!level.equals("")) {
            try {
                LevelLoader.load(level);
                game.setScreen(new LevelEditor(game));
            } catch (Exception exception) {
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
