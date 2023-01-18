package ru.thedreamingsaviour.game.screen;

import ru.thedreamingsaviour.game.logics.ShooterLevelsLogic;

import com.badlogic.gdx.graphics.GL20;
import ru.thedreamingsaviour.game.MyGdxGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;

public class LevelsScreen implements Screen {
    private final MyGdxGame game;
    private ShooterLevelsLogic shooterLevelsLogic;

    public LevelsScreen(final MyGdxGame game) {
        this.game = game;
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);

        switch (LevelLoader.getLevelType()) {
            case "shooter":
                shooterLevelsLogic = new ShooterLevelsLogic(this.game);
                break;
            case "platformer":
                break;
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        switch (LevelLoader.getLevelType()) {
            case "shooter":
                shooterLevelsLogic.render();
                break;
            case "platformer":
                break;
        }

        game.batch.end();

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
        game.batch.dispose();
        game.dispose();
    }
}
