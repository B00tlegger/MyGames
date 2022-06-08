package com.onceuponatime.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.onceuponatime.game.OnceUponATime;
import com.onceuponatime.game.model.characters.Character;
import com.onceuponatime.game.model.characters.MapObject;
import com.onceuponatime.game.model.characters.playeble_characters.Hero;
import com.onceuponatime.game.model.characters.unplayeble_character.Skeleton;
import com.onceuponatime.game.utils.RenderHelper;
import com.onceuponatime.game.utils.TiledMapProcessor;

import java.util.*;

public class GameScreen implements Screen {
    private final OnceUponATime game;
    private BitmapFont font24;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer renderer;
    private final int maxMonstersCount;
    private float timer;
    private final TiledMapProcessor map;
    private final List<MapObject> allMapObjects;
    private final List<Character> allCharacters;
    private final List<Skeleton> monsters;
    private final ArrayList<MapObject> renderQueue = new ArrayList<>();
    private final ArrayList<MapObject> renderedObjects = new ArrayList<>();

    private Hero hero;

    public GameScreen (OnceUponATime game) {
        this.game = game;
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        map = new TiledMapProcessor("world/start_location.tmx");
        renderer = new OrthogonalTiledMapRenderer(map.getMap(), 1f);

        //TODO: что-то сделать с этим мусором
        timer = 0;
        hero = new Hero(this);
        allCharacters = new ArrayList<>();
        allMapObjects = new ArrayList<>();
        monsters = new ArrayList<>();
        monsters.add(new Skeleton(this));
        allCharacters.addAll(monsters);
        allCharacters.add(hero);
        allMapObjects.addAll(allCharacters);
        maxMonstersCount = 5;
    }

    public void update (float dt) {
        for(int i = 0; i < allCharacters.size(); i++) {
            if(!allCharacters.get(i).isAlive()) {
                monsters.remove(allCharacters.get(i));
            }
            if(allCharacters.get(i).isAlive()) {
                allCharacters.get(i).update(dt);
            }
        }
        map.refillQueue();
        Collections.sort(allMapObjects, new Comparator<MapObject>() {
            @Override
            public int compare (MapObject o1, MapObject o2) {
                return (int) ((o2.getPosition().y + o2.getDepth()) - (o1.getPosition().y + o1.getDepth()));
            }
        });
        renderQueue.addAll(allMapObjects);
        updateCamera();
        renderer.setView(camera);
    }

    public void updateCamera () {
        camera.position.x = hero.getBody().getPosition().x;
        camera.position.y = hero.getBody().getPosition().y;
        camera.update();
    }

    @Override
    public void show () {
    }

    @Override
    public void render (float delta) {
        map.firstStepRender();
        update(delta);
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().setProjectionMatrix(camera.combined);
        renderer.render();
        map.nextStepRender();
        game.getBatch().begin();
        RenderHelper.render(map, renderQueue, game, font24, renderer);
        game.getBatch().end();
        renderQueue.clear();
        //TODO: Удалить после отладки
        game.getDebugRenderer().render(game.getWorld(), camera.combined);
        respawnMonsters(delta);
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

    public void dispose () {

    }

    public void respawnMonsters (float dt) {
        if(allCharacters.size() != maxMonstersCount + 1
                && maxMonstersCount != monsters.size()) {
            timer += dt;
            if(timer >= 10) {
                timer = 0;
                Skeleton skeleton = new Skeleton(this);
                monsters.add(skeleton);
                allCharacters.add(skeleton);
                allMapObjects.add(skeleton);
            }
        } else if(allCharacters.size() - 1 != monsters.size()) {
            for(int i = 0; i < allCharacters.size(); i++) {
                timer += dt;

                if((allCharacters.get(i) instanceof Skeleton) && !allCharacters.get(i).isAlive() && timer >= 10) {
                    Skeleton skeleton = (Skeleton) allCharacters.get(i);
                    skeleton.create();
                    monsters.add(skeleton);
                    timer = 0;
                }

            }
        }
    }

    public Hero getHero () {
        return hero;
    }

    public List<Skeleton> getMonsters () {
        return monsters;
    }

    public OnceUponATime getGame () {
        return game;
    }

    public TiledMapProcessor getMap () {
        return map;
    }
}
