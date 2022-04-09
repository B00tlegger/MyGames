package com.onceuponatime.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.onceuponatime.game.OnceUponATime;


public class MainMenuScreen implements Screen {

    private OnceUponATime game;

    private Texture background;
    private Texture mainText;
    private Texture lowText;
    private float transparency = 1;
    private boolean extinction = true;

    public MainMenuScreen (final OnceUponATime game) {
        this.game = game;
        background = new Texture("back_ground.png");
        mainText = new Texture("main_text.png");
        lowText = new Texture("low_text.png");

    }

    @Override
    public void show () {
    }

    @Override
    public void render (float delta) {
        if(transparency >= 1) {
            extinction = true;
        }
        if(transparency <= 0) {
            extinction = false;
        }
        transparency =
                extinction ? transparency - Gdx.graphics.getDeltaTime() : transparency + Gdx.graphics.getDeltaTime();
        changeScreen();
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        game.getBatch().draw(background, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().draw(mainText, Gdx.graphics.getWidth() / 2f - mainText.getWidth() / 2f,
                             Gdx.graphics.getHeight() - mainText.getHeight());
        game.getBatch().setColor(1, 1, 1, transparency);
        game.getBatch().draw(lowText, Gdx.graphics.getWidth() / 2f - lowText.getWidth() / 2f, 10f, lowText.getWidth(),
                             lowText.getHeight());
        game.getBatch().setColor(1, 1, 1, 1);
        game.getBatch().end();
    }

    @Override
    public void resize (int width, int height) {

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {

    }

    @Override
    public void dispose () {

    }

    public void changeScreen () {
        if(Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }
}
