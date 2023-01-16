package ru.gachigame.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import ru.gachigame.game.MyGdxGame;
import ru.gachigame.game.resourceloader.*;
import java.util.Arrays;

public class LoadScreen implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private String message;
    private String error;
    private boolean success;

    public LoadScreen(final MyGdxGame gam) {
        message = "Loading...";
        this.game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        Gdx.gl.glClearColor(0.2f, 0, 0.2f, 1);

        new Thread(() -> Gdx.app.postRunnable(() -> {
            try {
                message = "Loading jsons";
                error = "Error from jsons";
                System.out.println(message);
                JSONReader.load();
                message = "Loading shooter jsons";
                error = "Error from shooter jsons";
                System.out.println(message);
                ShooterCRUD.load();
                message = "Loading textures";
                error = "Error from textures";
                System.out.println(message);
                TextureLoader.load();
                message = "Loading sounds";
                error = "Error from sounds";
                System.out.println(message);
                SoundLoader.load();
                message = "Loading music";
                error = "Error from music";
                System.out.println(message);
                MusicLoader.load();
                message = "Loading success";
                success = true;
                System.out.println(message);
            } catch (Exception exception) {
                exception.printStackTrace();
                message = error + "\n" + Arrays.toString(exception.getStackTrace());
            }
        })).start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, message, 10, 80);
        game.batch.end();
        if (success) {
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
