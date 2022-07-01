package ru.gachigame.game.screen;

import com.badlogic.gdx.Gdx;
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
import ru.gachigame.game.MyGdxGame;
import java.util.Random;

public class MainMenuScreen implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final Stage stage;
    private final Random random;
    private final float volume;


    public MainMenuScreen(final MyGdxGame gam){
        this.game = gam;
        random = new Random();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        volume = (float) game.volume/30;

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




        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture("sprites/main_menu_background.jpg");


        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ShooterLevelScreen(game));
                //Gdx.input.getTextInput(listener, "Enter you nickname", "", "Enter you nickname");
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SettingsScreen(game));
            }
        });


        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                throw new RuntimeException();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.music = game.musicList.get(game.musicID);
        game.music.setVolume(volume);
        game.music.setLooping(false);

        if(!game.music.isPlaying()){
            game.musicID = random.nextInt(4);
            game.music = game.musicList.get(game.musicID);
            game.music.play();
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();

        stage.draw();
    }
    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose(){}
}
