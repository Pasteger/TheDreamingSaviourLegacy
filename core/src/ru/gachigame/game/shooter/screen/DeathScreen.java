package ru.gachigame.game.shooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import ru.gachigame.game.MyGdxGame;
import ru.gachigame.game.screen.MainMenuScreen;
import static ru.gachigame.game.shooter.ShooterCRUD.DEATH_BACKGROUND_TEXTURE_PATH;

public class DeathScreen implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final long startCurrentTime;

    public DeathScreen(final MyGdxGame gam) {
        this.game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture(DEATH_BACKGROUND_TEXTURE_PATH);
        startCurrentTime = System.currentTimeMillis();
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/sometimes_i_rip_the_skin.wav"));
        sound.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();
        long finishCurrentTime = System.currentTimeMillis();
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
