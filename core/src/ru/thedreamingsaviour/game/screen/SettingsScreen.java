/*package ru.gachigame.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

public class SettingsScreen implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final Stage stage;
    private float volume;


    public SettingsScreen(final MyGdxGame gam) {
        this.game = gam;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        BitmapFont universalFont = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.universalFont = universalFont;
        textButtonStyle.universalFont.getData().setScale(2);

        Button musicLeft = new TextButton("(<)", textButtonStyle);
        stage.addActor(musicLeft);
        musicLeft.setPosition(20, 200);

        Button musicRight = new TextButton("(>)", textButtonStyle);
        stage.addActor(musicRight);
        musicRight.setPosition(70, 200);

        Button musicVolumeUp = new TextButton("(+)", textButtonStyle);
        stage.addActor(musicVolumeUp);
        musicVolumeUp.setPosition(20, 150);

        Button musicVolumeDown = new TextButton("(-)", textButtonStyle);
        stage.addActor(musicVolumeDown);
        musicVolumeDown.setPosition(20, 100);

        Button backButton = new TextButton("back", textButtonStyle);
        stage.addActor(backButton);
        backButton.setPosition(20, 50);

        game.music = game.musicList.get(game.musicID);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture("sprites/main_menu_background.jpg");

        musicRight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    Music newMusic = game.musicList.get(game.musicID + 1);
                    game.musicID++;
                    game.music.stop();
                    game.music = newMusic;
                    game.music.play();
                    game.music.setVolume(volume);
                } catch (Exception ignored) {}
            }
        });

        musicLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    Music newMusic = game.musicList.get(game.musicID - 1);
                    game.music.stop();
                    game.music = newMusic;
                    game.musicID--;
                    game.music.play();
                    game.music.setVolume(volume);
                } catch (Exception ignored) {}
            }
        });


        musicVolumeUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.volume += 5;
                volume = (float) game.volume / 10;
                game.music.setVolume(volume);
            }
        });

        musicVolumeDown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.volume -= 5;
                if (game.volume < 0) {
                    game.volume = 0;
                    game.music.setVolume(0);
                }
                volume = (float) game.volume / 10;
                game.music.setVolume(volume);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);




        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.universalFont.getData().setScale(2);
        game.universalFont.draw(game.batch, game.musicID+"", 10, 400);
        game.universalFont.draw(game.batch, game.volume+"", 10, 350);
        game.universalFont.getData().setScale(1);
        game.batch.end();

        stage.draw();
    }

    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose(){}
}*/
