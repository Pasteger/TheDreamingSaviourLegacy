package ru.thedreamingsaviour.game.logics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.entity.Player;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.screen.LevelsScreen;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.HUB_MUSIC;
import static ru.thedreamingsaviour.game.resourceloader.SaveLoader.PLAYER;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.getHubBackground;

public class Hub {
    private final MyGdxGame game;
    private final Player player;
    private final Texture background;
    private final Music music;

    public Hub(final MyGdxGame game) {
        this.game = game;
        game.camera.setToOrtho(false, 4000, 4000);

        background = getHubBackground();
        PLAYER.heal();
        player = PLAYER;
        music = HUB_MUSIC.get("ACT_1").get(0);
        music.setLooping(true);
        Gdx.input.setInputProcessor(new HubInputProcessor());
        music.play();
    }

    public void render() {
        game.batch.draw(background, 0, 0);

        game.universalFont.draw(game.batch, "HP: " + player.HP, 100, 3900);
        game.universalFont.draw(game.batch, "damage: " + player.damage, 100, 3700);
        game.universalFont.draw(game.batch, "speed: " + player.saveSpeed, 100, 3500);
        game.universalFont.draw(game.batch, "balance: " + player.balance, 2500, 3900);


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            music.stop();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    class HubInputProcessor implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button == Input.Buttons.LEFT) {
                if (screenX > 709 && screenX < 764 && screenY > 196 && screenY < 335) {
                    try {
                        LevelLoader.load(player.currentLevel);
                        game.setScreen(new LevelsScreen(game, "level"));
                        music.stop();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
