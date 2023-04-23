package ru.thedreamingsaviour.game.screen;

import ru.thedreamingsaviour.game.logics.GameLogic;
import ru.thedreamingsaviour.game.logics.Hub;
import ru.thedreamingsaviour.game.logics.LevelsLogic;

import com.badlogic.gdx.graphics.GL20;
import ru.thedreamingsaviour.game.MyGdxGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;

public class LevelsScreen implements Screen {
    private final MyGdxGame game;
    private final GameLogic gameLogic;

    public LevelsScreen(final MyGdxGame game, String logic) {
        this.game = game;
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);

        if (logic.equals("HUB")) {
            gameLogic = new Hub(this.game);
        } else if (logic.equals("level")){
            gameLogic = new LevelsLogic(this.game);
        } else {
            String boss = logic.substring(0, 1).toUpperCase() + logic.substring(1);
            try {
                gameLogic = (GameLogic)  Class.forName("ru.thedreamingsaviour.game.logics.bossbattle." + boss)
                        .getConstructor(MyGdxGame.class).newInstance(this.game);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        gameLogic.render();

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
