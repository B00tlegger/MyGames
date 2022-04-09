package com.onceuponatime.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.onceuponatime.game.screens.MainMenuScreen;

public class OnceUponATime extends Game {
	SpriteBatch batch;
	World world;
	Box2DDebugRenderer debugRenderer;

	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(), false);
		screen = new MainMenuScreen(this);
		debugRenderer = new Box2DDebugRenderer();

	}

	@Override
	public void render () {
		screen.render(Gdx.graphics.getDeltaTime());
		world.step(1/60f, 6, 2);
	}

	@Override
	public void dispose () {
		debugRenderer.dispose();
		world.dispose();
		batch.dispose();
	}

	public SpriteBatch getBatch () {
		return batch;
	}

	public World getWorld () {
		return world;
	}

	public Box2DDebugRenderer getDebugRenderer () {
		return debugRenderer;
	}
}
