package com.mygdx.game.model.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class MapObject {
    protected Vector2 position;
    protected Texture texture;
    protected int width;
    protected int height;
    protected float depth;


    public Vector2 getPosition () {
        return position;
    }

    public void render(SpriteBatch batch, BitmapFont bitmapFont){
        batch.draw(texture, position.x, position.y);
    }

    public int getWidth () {
        return width;
    }

    public void setWidth (int width) {
        this.width = width;
    }

    public int getHeight () {
        return height;
    }

    public void setHeight (int height) {
        this.height = height;
    }

    public float getDepth () {
        return depth;
    }

    public void setDepth (float depth) {
        this.depth = depth;
    }

    public Texture getTexture () {
        return texture;
    }

}
