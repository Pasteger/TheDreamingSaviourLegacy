package ru.gachigame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.gachigame.game.resourceloader.JSONReader;
import ru.gachigame.game.resourceloader.ShooterCRUD;
import ru.gachigame.game.resourceloader.TextureLoader;
import ru.gachigame.game.screen.MainMenuScreen;
import ru.gachigame.game.shooter.screen.LoadScreen;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public String nickname;
	private OrthographicCamera camera;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();

		/*try {
			JSONReader.load();
			ShooterCRUD.load();
			TextureLoader.load();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}*/
		this.setScreen(new LoadScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	public OrthographicCamera getCamera() {return camera;}
}
