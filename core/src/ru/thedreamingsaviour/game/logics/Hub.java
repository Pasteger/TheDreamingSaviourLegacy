package ru.thedreamingsaviour.game.logics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import ru.thedreamingsaviour.game.MyGdxGame;
import ru.thedreamingsaviour.game.gameobject.entity.Player;
import ru.thedreamingsaviour.game.resourceloader.DialogueLoader;
import ru.thedreamingsaviour.game.resourceloader.LevelLoader;
import ru.thedreamingsaviour.game.entity.Dialogue;
import ru.thedreamingsaviour.game.entity.Phrase;
import ru.thedreamingsaviour.game.screen.LevelsScreen;
import ru.thedreamingsaviour.game.screen.MainMenuScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.thedreamingsaviour.game.resourceloader.MusicLoader.HUB_MUSIC;
import static ru.thedreamingsaviour.game.resourceloader.SaveLoader.PLAYER;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.getHubBackground;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.getTraderTexture;

public class Hub {
    private final MyGdxGame game;
    private final Player player;
    private final Texture background;
    private final Texture trader;
    private Music music;
    List<Dialogue> dialogues;
    List<Phrase> phraseList = new ArrayList<>();
    private String outputPhrase = "";
    private int phraseIndex;
    private int currentIndex;
    private int currentPhase;
    private int speechPhase;

    public Hub(final MyGdxGame game) {
        this.game = game;
        game.camera.setToOrtho(false, 4000, 4000);

        background = getHubBackground();
        trader = getTraderTexture();
        PLAYER.heal();
        player = PLAYER;

        switch (PLAYER.hubLevel) {
            case 0 -> {
                music = HUB_MUSIC.get("ACT_1").get(0);
                dialogues = DialogueLoader.getStart();
            }
            case 1 -> {
                music = HUB_MUSIC.get("ACT_1").get(0);
                dialogues = DialogueLoader.getAct2();
            }
            case 2 -> {
                music = HUB_MUSIC.get("ACT_2").get(0);
                dialogues = DialogueLoader.getAct3();
            }
        }

        music.setLooping(true);
        Gdx.input.setInputProcessor(new HubInputProcessor());
        music.play();

        Optional<Dialogue> dialogueOptional = dialogues.stream().filter(
                dialogue -> dialogue.getCondition().equals("enter")).findFirst();
        dialogueOptional.ifPresent(dialogue -> updateDialogue(dialogues.indexOf(dialogue)));
    }

    public void render() {
        game.batch.draw(background, 0, 0);
        game.batch.draw(trader, 1404, 1300);

        game.universalFont.draw(game.batch, "HP: " + player.HP, 100, 3900);
        game.universalFont.draw(game.batch, "damage: " + player.damage, 100, 3700);
        game.universalFont.draw(game.batch, "speed: " + player.saveSpeed, 100, 3500);
        game.universalFont.draw(game.batch, "balance: " + player.balance, 2500, 3900);

        game.universalFont.draw(game.batch, "" + outputPhrase, 50, 500);

        traderSpeak();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            music.stop();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void updateDialogue(int dialogueIndex) {
        currentIndex = 0;
        phraseIndex = 0;
        currentPhase = 0;
        phraseList = dialogues.get(dialogueIndex).getPhraseList();
        speechPhase = phraseList.get(0).getSpeed();
        phraseList.forEach(phrase -> phrase.end = false);
    }

    private void traderSpeak() {
        try {
            if (phraseIndex > phraseList.size() - 1) {
                outputPhrase = "";
                return;
            }

            if (currentIndex > phraseList.get(phraseIndex).getText().length() - 1 && !phraseList.get(phraseIndex).end) {
                phraseList.get(phraseIndex).end = true;
            }

            if (phraseList.get(phraseIndex).end && currentPhase == phraseList.get(phraseIndex).getPause()) {
                currentIndex = 0;
                speechPhase = phraseList.get(phraseIndex).getSpeed();
                phraseIndex++;
                currentPhase = 0;
            } else if (!phraseList.get(phraseIndex).end && currentPhase == speechPhase) {
                currentIndex++;
                try {
                    while (phraseList.get(phraseIndex).getText().charAt(currentIndex - 1) == ' ') {
                        currentIndex++;
                    }
                } catch (Exception ignored) {
                }
                outputPhrase = phraseList.get(phraseIndex).getText().substring(0, currentIndex);
                currentPhase = 0;
            } else {
                currentPhase++;
            }
        } catch (Exception ignored) {
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
                if (screenX > 279 && screenX < 454 && screenY > 280 && screenY < 400) {
                    Optional<Dialogue> dialogueOptional = dialogues.stream().filter(
                            dialogue -> dialogue.getCondition().equals("click")).findFirst();
                    dialogueOptional.ifPresent(dialogue -> updateDialogue(dialogues.indexOf(dialogue)));
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
