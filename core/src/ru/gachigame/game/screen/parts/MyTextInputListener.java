package ru.gachigame.game.screen.parts;

import com.badlogic.gdx.Input;
import ru.gachigame.game.MyGdxGame;

public class MyTextInputListener implements Input.TextInputListener {
    final MyGdxGame game;

    public MyTextInputListener(final MyGdxGame game){
        this.game = game;
    }

    @Override
    public void input (String text) {
        if (text.matches("[a-zA-Z_]*")) {
            game.nickname = text;
        }
    }

    @Override
    public void canceled () {
    }
}
