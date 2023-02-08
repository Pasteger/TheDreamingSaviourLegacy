package ru.thedreamingsaviour.game.resourceloader;

import org.json.simple.JSONObject;
import ru.thedreamingsaviour.game.resourceloader.database.entity.Dialogue;
import ru.thedreamingsaviour.game.resourceloader.database.entity.Phrase;

import java.util.ArrayList;
import java.util.List;

public class DialogueLoader {
    private static List<Dialogue> start = new ArrayList<>();
    private static List<Dialogue> act2 = new ArrayList<>();
    private static List<Dialogue> act3 = new ArrayList<>();

    public static void load() throws Exception {
        start = readDialogueList("start");
        act2 = readDialogueList("act2");
        act3 = readDialogueList("act3");
    }

    private static List<Dialogue> readDialogueList(String name) throws Exception {
        List<Dialogue> dialogueList = new ArrayList<>();
        List<JSONObject> dialogues = JSONReader.getDialogues(name);
        for (JSONObject jsonDialogue : dialogues) {
            Dialogue dialogue = new Dialogue();
            dialogue.setCondition(jsonDialogue.get("condition").toString());
            dialogue.setPhraseList(readPhraseList(jsonDialogue));

            dialogueList.add(dialogue);
        }
        return dialogueList;
    }
    private static List<Phrase> readPhraseList(JSONObject jsonDialogue) {
        List<Phrase> phrases = new ArrayList<>();
        List<JSONObject> jsonPhrases = (List<JSONObject>) jsonDialogue.get("phraseList");
        for (JSONObject jsonPhrase : jsonPhrases) {
            Phrase phrase = new Phrase();
            phrase.setSpeed(Integer.parseInt(jsonPhrase.get("speed").toString()));
            phrase.setText((jsonPhrase.get("text").toString()));
            phrase.setPause(Integer.parseInt(jsonPhrase.get("pause").toString()));

            phrases.add(phrase);
        }
        return phrases;
    }

    public static List<Dialogue> getStart() {
        return start;
    }

    public static List<Dialogue> getAct2() {
        return act2;
    }

    public static List<Dialogue> getAct3() {
        return act3;
    }
}
