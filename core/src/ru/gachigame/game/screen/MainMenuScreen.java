package ru.gachigame.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.gachigame.game.MyGdxGame;
import ru.gachigame.game.screen.parts.MyTextInputListener;
import ru.gachigame.game.screen.parts.ReadingAndWritingTheDatabase;
import ru.gachigame.game.screen.parts.TableOfRecords;
import java.io.IOException;
import java.util.Random;

public class MainMenuScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    Texture background;
    Stage stage;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    Button startButton;
    Button settingsButton;
    Button tableOfRecordsButton;
    Button exitButton;
    boolean isPressed;
    Random random;
    MyTextInputListener listener;
    boolean listenerExist;
    float volume;


    public MainMenuScreen(final MyGdxGame gam){
        this.game = gam;
        listenerExist = false;
        random = new Random();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        volume = (float) game.volume/10;

        font = new BitmapFont();
        skin = new Skin();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.font.getData().setScale(2);

        startButton = new TextButton("Start", textButtonStyle);
        stage.addActor(startButton);
        startButton.setPosition(20, 200);

        settingsButton = new TextButton("Settings", textButtonStyle);
        stage.addActor(settingsButton);
        settingsButton.setPosition(20, 150);

        tableOfRecordsButton = new TextButton("Table of records", textButtonStyle);
        stage.addActor(tableOfRecordsButton);
        tableOfRecordsButton.setPosition(20, 100);

        exitButton = new TextButton("Exit", textButtonStyle);
        stage.addActor(exitButton);
        exitButton.setPosition(20, 50);




        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture("sprites/main_menu_background.jpg");


        StringBuilder table = ReadingAndWritingTheDatabase.readingFile();
        table = TableOfRecords.sortTable(table);
        try {
            ReadingAndWritingTheDatabase.writingFile(table.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        listener = new MyTextInputListener(game);
    }

    @Override
    public void render(float delta) {
        isPressed = false;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.music = game.musicArrayList.get(game.musicID);
        game.music.setVolume(volume);
        game.music.setLooping(false);

        if(!game.music.isPlaying()){
            game.musicID = random.nextInt(4);
            game.music = game.musicArrayList.get(game.musicID);
            game.music.play();
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();

        stage.draw();




        if (game.nickname != null) {
            game.setScreen(new MainGameSpace(game));
            dispose();
        }

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isPressed) {
                    isPressed = true;
                    Gdx.input.getTextInput(listener, "Enter you nickname", "", "Enter you nickname");
                    listenerExist = true;
                }
            }
        });


        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!isPressed) {
                    isPressed = true;
                    game.setScreen(new SettingsScreen(game));
                    dispose();
                }
            }
        });

        tableOfRecordsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!isPressed) {
                    isPressed = true;
                    game.setScreen(new TableOfRecordsScreen(game));
                    dispose();
                }
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!isPressed) {
                    isPressed = true;
                    int a = 0 / 0;
                }
            }
        });
    }
    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose(){}
}
