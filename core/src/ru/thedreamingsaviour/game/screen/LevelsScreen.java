package ru.thedreamingsaviour.game.screen;

import ru.thedreamingsaviour.game.logics.Hub;
import ru.thedreamingsaviour.game.logics.LevelsLogic;

import com.badlogic.gdx.graphics.GL20;
import ru.thedreamingsaviour.game.MyGdxGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;

public class LevelsScreen implements Screen {
    private final MyGdxGame game;
    private LevelsLogic levelsLogic;
    private Hub hub;
    private final String level;

    public LevelsScreen(final MyGdxGame game, String level) {
        this.game = game;
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);

        this.level = level;
        if (level.equals("HUB")) {
            hub = new Hub(this.game);
        } else {
            levelsLogic = new LevelsLogic(this.game);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        if (level.equals("HUB")) {
            hub.render();
        }
        else {
            levelsLogic.render();
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
