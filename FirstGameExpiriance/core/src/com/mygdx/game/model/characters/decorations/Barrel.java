package com.mygdx.game.model.characters.decorations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Barrel extends MapDecoration {

    public Barrel (Vector2 position) {
        this.position = position;
        texture = new Texture("barrel.png");
        width = texture.getWidth();
        height = texture.getHeight();
        depth = (float) (texture.getHeight() / 1.3);
    }

}
