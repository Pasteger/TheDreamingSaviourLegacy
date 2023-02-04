package ru.thedreamingsaviour.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.AnimatedObject;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.getDeathMusic;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.DEATH_BACKGROUND;

public class DeathScreen implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final AnimatedObject background;
    private final Music music;
    private final long startCurrentTime;

    public DeathScreen(final MyGdxGame gam) {
        this.game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new AnimatedObject(DEATH_BACKGROUND);
        music = getDeathMusic();
        startCurrentTime = System.currentTimeMillis();
        music.setLooping(false);
        music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        background.draw(game.batch, 0, 0, 800, 480, 10);
        game.batch.end();
        long finishCurrentTime = System.currentTimeMillis();
        float timeMs = finishCurrentTime - startCurrentTime;
        if (timeMs > 1000) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched() || timeMs > 152000) {
                music.stop();
                game.setScreen(new MainMenuScreen(game));
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
