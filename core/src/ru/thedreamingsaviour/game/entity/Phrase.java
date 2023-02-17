package ru.thedreamingsaviour.game.entity;

public class Phrase {
    private int speed;
    private String text;
    private int pause;
    public boolean end;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    public int getSpeed() {
        return speed;
    }

    public String getText() {
        return text;
    }

    public int getPause() {
        return pause;
    }
}
