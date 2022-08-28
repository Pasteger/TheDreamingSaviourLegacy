package ru.gachigame.game.screen;

import ru.gachigame.game.logics.ShooterLevelsLogic;
import static ru.gachigame.game.resourceloader.LevelLoader.getLevelType;
import com.badlogic.gdx.graphics.GL20;
import ru.gachigame.game.MyGdxGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;

public class LevelsScreen implements Screen {
    private final MyGdxGame game;
    private ShooterLevelsLogic shooterLevelsLogic;

    public LevelsScreen(final MyGdxGame game){
        this.game = game;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);

        switch (getLevelType()){
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

        switch (getLevelType()){
            case "shooter":
                shooterLevelsLogic.render();
                break;
            case "platformer":
                break;
        }

        game.batch.end();

    }

    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}

    @Override
    public void dispose() {
        game.batch.dispose();
        game.dispose();
    }
}
