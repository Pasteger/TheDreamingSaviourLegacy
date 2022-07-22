package ru.gachigame.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.gachigame.game.LevelEditor;
import ru.gachigame.game.MyGdxGame;
import ru.gachigame.game.resourceloader.LevelLoader;
import ru.gachigame.game.shooter.screen.ShooterLevelScreen;
import static ru.gachigame.game.resourceloader.TextureLoader.*;

public class MainMenuScreen implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final Stage stage;

    public MainMenuScreen(final MyGdxGame gam){
        this.game = gam;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        background = getMainMenuBackground();

        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.font.getData().setScale(2);

        Button startButton = new TextButton("Start", textButtonStyle);
        stage.addActor(startButton);
        startButton.setPosition(20, 200);

        Button settingsButton = new TextButton("Settings", textButtonStyle);
        stage.addActor(settingsButton);
        settingsButton.setPosition(20, 150);

        Button exitButton = new TextButton("Exit", textButtonStyle);
        stage.addActor(exitButton);
        exitButton.setPosition(20, 100);


        camera = game.getCamera();
        camera.setToOrtho(false, 800, 480);


        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    LevelLoader.load("level0");
                    game.setScreen(new ShooterLevelScreen(game));
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.setScreen(new SettingsScreen(game));
                System.out.println("WIP");
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
        game.batch.end();
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            try {
                LevelLoader.load("level0");
                game.setScreen(new LevelEditor(game));
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }
    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose(){}
}
