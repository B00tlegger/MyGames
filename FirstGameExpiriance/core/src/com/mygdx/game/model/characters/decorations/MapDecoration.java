package com.mygdx.game.model.characters.decorations;

import com.mygdx.game.model.characters.MapObject;

import java.util.Objects;

public abstract class MapDecoration extends MapObject {

    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(!(o instanceof Barrel)) return false;
        Barrel mapDecoration = (Barrel) o;
        return Objects.equals(getTexture(), mapDecoration.getTexture()) &&
                Objects.equals(position, mapDecoration.position);
    }

    @Override
    public int hashCode () {
        return Objects.hash(getTexture(), position);
    }



}
