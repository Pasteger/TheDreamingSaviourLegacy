package ru.thedreamingsaviour.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import ru.thedreamingsaviour.game.screen.LoadScreen;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont universalFont;
	public OrthographicCamera camera;

	@Override
	public void create () {
		universalFont = getFont(100, Color.WHITE);
		universalFont.getData().scale(1);

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		this.setScreen(new LoadScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		universalFont.dispose();
	}

	public BitmapFont getFont(int size, Color color){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("regular/arial.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<> ";
		parameter.size = size;
		parameter.color = color;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
}
