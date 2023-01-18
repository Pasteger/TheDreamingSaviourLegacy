package ru.thedreamingsaviour.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.thedreamingsaviour.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("TheDreamingSaviour");
		config.setWindowedMode(800, 600);
		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
