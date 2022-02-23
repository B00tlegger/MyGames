package com.mygdx.game.model.characters.decorations;

public enum Passability {

    PASSABLE(0),
    UNPASSABLE(1);

    private final float value;

    private Passability(float value) {
        this.value = value;
    }

    public float getValue () {
        return value;
    }
}
