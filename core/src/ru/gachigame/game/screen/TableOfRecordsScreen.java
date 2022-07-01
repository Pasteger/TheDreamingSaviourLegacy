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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.gachigame.game.MyGdxGame;
import ru.gachigame.game.screen.parts.ReadingAndWritingTheDatabase;
import ru.gachigame.game.screen.parts.TableOfRecords;
import java.io.IOException;
import java.util.Random;

public class TableOfRecordsScreen implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final Random random;
    private final Stage stage;
    private StringBuilder table;
    private float tableY;

    public TableOfRecordsScreen(final MyGdxGame gam) {
        this.game = gam;
        random = new Random();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture("sprites/main_menu_background.jpg");
        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        font.getData().setScale(2);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Button backButton = new TextButton("back", textButtonStyle);
        stage.addActor(backButton);
        backButton.setPosition(20, 20);

        Button upButton = new TextButton("up", textButtonStyle);
        stage.addActor(upButton);
        upButton.setPosition(510, 100);

        Button downButton = new TextButton("down", textButtonStyle);
        stage.addActor(downButton);
        downButton.setPosition(500, 20);

        table = ReadingAndWritingTheDatabase.readingFile();
        table = TableOfRecords.sortTable(table);

        try {ReadingAndWritingTheDatabase.writingFile(table.toString());}
        catch (IOException e) {e.printStackTrace();}

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        upButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (tableY > 470) {
                    tableY -= 20;
                }
            }
        });

        downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (tableY < 1110) {
                    tableY += 20;
                }
            }
        });

        tableY = 470;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if(!game.music.isPlaying()){
            game.musicID = random.nextInt(4);
            game.music = game.musicList.get(game.musicID);
            game.music.play();
        }

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.font.getData().setScale(2);
        game.font.draw(game.batch, table, 300, tableY);
        game.font.getData().setScale(1);
        game.batch.end();

        stage.draw();
    }

    @Override public void show(){}
    @Override public void resize(int width, int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose(){}
}
