package ru.thedreamingsaviour.game.screen;

import ru.thedreamingsaviour.game.logics.LevelsLogic;

import com.badlogic.gdx.graphics.GL20;
import ru.thedreamingsaviour.game.MyGdxGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;

public class LevelsScreen implements Screen {
    private final MyGdxGame game;
    private final LevelsLogic levelsLogic;

    public LevelsScreen(final MyGdxGame game) {
        this.game = game;
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);

        levelsLogic = new LevelsLogic(this.game);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        levelsLogic.render();

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
