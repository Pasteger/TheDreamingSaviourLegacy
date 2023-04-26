package ru.thedreamingsaviour.game.gameobject.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.thedreamingsaviour.game.gameobject.AnimatedObject;

import java.util.List;
import java.util.Map;

import static ru.thedreamingsaviour.game.resourceloader.SoundLoader.getPickUpHeal;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.BOX;
import static ru.thedreamingsaviour.game.resourceloader.TextureLoader.PICK_UP_PACKAGE;

public class PickUpPackage extends Entity {
    private final String effect;
    private final int power;

    public PickUpPackage(String effect, int power, float x, float y, float width, float height){
        this.type = "PickUpPackage";
        this.effect = effect;
        this.power = power;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

        this.saveHP = 1;
        heal();

        this.sprites = PICK_UP_PACKAGE.get(effect);
    }

    @Override
    public void draw(SpriteBatch batch) {
        int drawSpeed = 15 - speed / 10 > 0 ? 15 - speed / 10 : 1;
        animatedObject.draw(batch, x, y, width, height, drawSpeed);
    }

    public void pickUp(Player player){
        if (effect.equals("heal")) {
            player.takeHeal(power);
            getPickUpHeal().play(0.5f);
        }
    }
}
