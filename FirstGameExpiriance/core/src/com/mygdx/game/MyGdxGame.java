package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.GameScreen;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    GameScreen screen;

    @Override
    public void create () {
        batch = new SpriteBatch();
        screen = new GameScreen(batch);
        screen.create();

    }

    @Override
    public void render () {
        screen.render();
    }

    private void update (float dt) {

    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}
