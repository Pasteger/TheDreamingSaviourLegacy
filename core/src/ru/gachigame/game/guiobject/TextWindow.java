package ru.gachigame.game.guiobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import ru.gachigame.game.MyGdxGame;
import ru.gachigame.game.gameobject.Surface;

public class TextWindow {
    private Surface background;
    private Surface window;
    private final StringBuilder text = new StringBuilder();
    private String outputText = "";
    private boolean rendering;
    private InputProcessor standardInputProcessor;

    public void call(int x, int y, int width, int height){
        standardInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(new TextInputProcessor());

        window = new Surface(0, 0, width, height, "none", "floorTexture");
        window.x = x;
        window.y = y;

        background = new Surface(0, 0, width + 6, height + 6, "none", "wallTexture");
        background.x = x - 3;
        background.y = y - 3;

        rendering = true;
    }

    public void render(MyGdxGame game){
        if (!rendering){
            return;
        }
        background.draw(game.batch);
        window.draw(game.batch);
        game.font.draw(game.batch, text.toString(), window.x + 10, window.y + window.height / 2 + 5);
    }

    public void recall(){
        Gdx.input.setInputProcessor(standardInputProcessor);
        rendering = false;
        window = null;
        background = null;
        text.delete(0, text.length());
    }

    public String getOutputText(){
        String out = outputText;
        outputText = "";
        return out;
    }

    public boolean isRendering() {
        return rendering;
    }

    class TextInputProcessor implements InputProcessor{

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ENTER) {
                outputText = text.toString();
                recall();
                return false;
            }
            if (keycode == Input.Keys.ESCAPE) {
                recall();
                return false;
            }
            if (keycode == Input.Keys.BACKSPACE) {
                try {
                    text.delete(text.length() - 1, text.length());
                } catch (Exception ignored) {
                }
                return false;
            }

            if (text.length() < (window.width - 10) / 8 - 1) {
                if (Input.Keys.toString(keycode).matches("[A-Z]")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append(Input.Keys.toString(keycode));
                    } else {
                        text.append(Input.Keys.toString(keycode).toLowerCase());
                    }
                }
                if (Input.Keys.toString(keycode).equals("0")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append(')');
                    } else {
                        text.append(0);
                    }
                }
                if (Input.Keys.toString(keycode).equals("1")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('!');
                    } else {
                        text.append(1);
                    }
                }
                if (Input.Keys.toString(keycode).equals("2")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('@');
                    } else {
                        text.append(2);
                    }
                }
                if (Input.Keys.toString(keycode).equals("3")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('#');
                    } else {
                        text.append(3);
                    }
                }
                if (Input.Keys.toString(keycode).equals("4")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('$');
                    } else {
                        text.append(4);
                    }
                }
                if (Input.Keys.toString(keycode).equals("5")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('%');
                    } else {
                        text.append(5);
                    }
                }
                if (Input.Keys.toString(keycode).equals("6")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('^');
                    } else {
                        text.append(6);
                    }
                }
                if (Input.Keys.toString(keycode).equals("7")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('&');
                    } else {
                        text.append(7);
                    }
                }
                if (Input.Keys.toString(keycode).equals("8")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('*');
                    } else {
                        text.append(8);
                    }
                }
                if (Input.Keys.toString(keycode).equals("9")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('(');
                    } else {
                        text.append(9);
                    }
                }
                if (Input.Keys.toString(keycode).equals(";")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append(':');
                    } else {
                        text.append(';');
                    }
                }
                if (Input.Keys.toString(keycode).equals("'")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('\"');
                    } else {
                        text.append('\'');
                    }
                }
                if (Input.Keys.toString(keycode).equals(",")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('<');
                    } else {
                        text.append(',');
                    }
                }
                if (Input.Keys.toString(keycode).equals(".")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('>');
                    } else {
                        text.append('.');
                    }
                }
                if (Input.Keys.toString(keycode).equals("/")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('?');
                    } else {
                        text.append('/');
                    }
                }
                if (Input.Keys.toString(keycode).equals("=")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('+');
                    } else {
                        text.append('=');
                    }
                }
                if (Input.Keys.toString(keycode).equals("-")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('_');
                    } else {
                        text.append('-');
                    }
                }
                if (Input.Keys.toString(keycode).equals("[")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('{');
                    } else {
                        text.append('[');
                    }
                }
                if (Input.Keys.toString(keycode).equals("]")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('}');
                    } else {
                        text.append(']');
                    }
                }
                if (Input.Keys.toString(keycode).equals("\\")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('|');
                    } else {
                        text.append('\\');
                    }
                }
                if (Input.Keys.toString(keycode).equals("`")) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        text.append('~');
                    } else {
                        text.append('`');
                    }
                }
            }
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

        @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}
        @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}
        @Override public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
        @Override public boolean mouseMoved(int screenX, int screenY) {return false;}
        @Override public boolean scrolled(float amountX, float amountY) {return false;}
    }
}
