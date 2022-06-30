package ru.gachigame.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import ru.gachigame.game.MyGdxGame;

import java.util.Random;

public class DeathScreen implements Screen {
    final MyGdxGame game;
    boolean isPressed;
    OrthographicCamera camera;
    Texture background;
    Random random;
    Sound sound;
    long startCurrentTime;
    long finishCurrentTime;

    public DeathScreen(final MyGdxGame gam) {
        this.game = gam;
        random = new Random();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture("sprites/death_background.png");
        startCurrentTime = System.currentTimeMillis();
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/sometimes_i_rip_the_skin.wav"));
        sound.play();
    }

    @Override
    public void render(float delta) {
        isPressed = false;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if(!game.music.isPlaying()){
            game.musicID = random.nextInt(4);
            game.music = game.musicArrayList.get(game.musicID);
            game.music.play();
        }

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();
        finishCurrentTime = System.currentTimeMillis();
        float timeMs = finishCurrentTime - startCurrentTime;
        if(timeMs > 3000) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose(){}
}
