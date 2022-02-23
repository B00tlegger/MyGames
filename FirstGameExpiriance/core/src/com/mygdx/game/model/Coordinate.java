package com.mygdx.game.model;

import java.util.Objects;

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    @Override
    public boolean equals (Object o) {
        if(this == o) return true;
        if(!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return Float.compare(that.getX(), getX()) == 0 && Float.compare(that.getY(), getY()) == 0;
    }

    @Override
    public int hashCode () {
        return Objects.hash(getX(), getY());
    }
}
