package ru.gachigame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.gachigame.game.screen.MainMenuScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public List<Music> musicList;
	public Music music;
	public int musicID;
	public int volume;
	public Random random;
	public String nickname;

	@Override
	public void create () {
		random = new Random();
		musicID = random.nextInt(4);
		volume = 1;
		batch = new SpriteBatch();
		font = new BitmapFont();
		musicList = new ArrayList<>();
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("music/bossmark.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("music/fool_and_lightning.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("music/first_lieutenant.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("music/youth_in_boots.mp3")));
		this.setScreen(new MainMenuScreen(this));
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
}
