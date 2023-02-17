package ru.thedreamingsaviour.game.entity;

import java.util.List;

public class Dialogue {
    private List<Phrase> phraseList;
    private String condition;

    public void setPhraseList(List<Phrase> phraseList) {
        this.phraseList = phraseList;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<Phrase> getPhraseList() {
        return phraseList;
    }

    public String getCondition() {
        return condition;
    }
}
