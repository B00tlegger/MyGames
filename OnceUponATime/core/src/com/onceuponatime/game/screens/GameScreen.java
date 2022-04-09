package com.onceuponatime.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.onceuponatime.game.OnceUponATime;
import com.onceuponatime.game.model.EllipseShape;
import com.onceuponatime.game.model.characters.Character;
import com.onceuponatime.game.model.characters.MapObject;
import com.onceuponatime.game.model.characters.playeble_characters.Hero;
import com.onceuponatime.game.model.characters.unplayeble_character.Skeleton;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class GameScreen implements Screen {
    private OnceUponATime game;
    private BitmapFont font24;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private int maxMonstersCount;
    private float timer;
    private TiledMap map;
    private List<MapObject> allMapObjects;
    private List<Character> allCharacters;
    private List<Skeleton> monsters;

    private Hero hero;

    public GameScreen (OnceUponATime game) {
        this.game = game;
        timer = 0;
        map = new TmxMapLoader().load("world/start_location.tmx");
        hero = new Hero(this);
        allCharacters = new ArrayList<>();
        allMapObjects = new ArrayList<>();
        monsters = new ArrayList<>();
        monsters.add(new Skeleton(this));
        allCharacters.addAll(monsters);
        allCharacters.add(hero);
        allMapObjects.addAll(allCharacters);
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        maxMonstersCount = 5;
        camera = new OrthographicCamera();
        renderer = new OrthogonalTiledMapRenderer(map, 1f);
        camera.setToOrtho(false);
        createBody();
        createEllipse();
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
        camera.position.x = hero.getBody().getPosition().x;
        camera.position.y = hero.getBody().getPosition().y;
        camera.update();
        renderer.setView(camera);
        firstRender();
        renderer.render();

    }

    @Override
    public void show () {
    }

    @Override
    public void render (float delta) {
        Collections.sort(allMapObjects, new Comparator<MapObject>() {
            @Override
            public int compare (MapObject o1, MapObject o2) {
                return (int) ((o2.getPosition().y + o2.getDepth()) - (o1.getPosition().y + o1.getDepth()));
            }
        });
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        for(int i = 0; i < allMapObjects.size(); i++) {
            if(allMapObjects.get(i) instanceof Character) {
                if(((Character) allMapObjects.get(i)).isAlive() && !test3((Character) allMapObjects.get(i))) {
                    allMapObjects.get(i).render(game.getBatch(), font24);
                }
            } else {
                allMapObjects.get(i).render(game.getBatch(), font24);
            }
        }
//        test2();
        secondRender();
        renderer.render();
        for(int i = 0; i < allMapObjects.size(); i++) {
            if(allMapObjects.get(i) instanceof Character) {
                if(((Character) allMapObjects.get(i)).isAlive() && test3((Character) allMapObjects.get(i))) {
                    allMapObjects.get(i).render(game.getBatch(), font24);
                }
            }
        }

        game.getBatch().end();
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

    public void test2 () {
        MapLayers layers = map.getLayers();
        Iterator layersIterator = layers.iterator();
        while(layersIterator.hasNext()) {
            MapLayer layer = (MapLayer) layersIterator.next();
            if(layer.getName().equals("back") ||
                    layer.getName().equals("deco") ||
                    layer.getName().equals("deco_2") ||
                    layer.getName().equals("forward") ||
                    layer.getName().equals("middle")) {
                layer.setVisible(true);
            } else {
                layer.setVisible(false);
            }
        }
    }

    public void secondRender () {
        MapLayers layers = map.getLayers();
        for(MapLayer layer : layers) {
            int priority;
            if(layer instanceof MapGroupLayer) {
                for(MapLayer gl : ((MapGroupLayer) layer).getLayers()) {
                    {
                        priority = gl.getProperties().get("priority", Integer.class) != null ?
                                gl.getProperties().get("priority", Integer.class)
                                : -1;
                        gl.setVisible(priority == 1);
                    }
                }
            } else {
                priority = layer.getProperties().get("priority", Integer.class) != null ?
                        layer.getProperties().get("priority", Integer.class)
                        : -1;
                layer.setVisible(priority == 1);
            }
        }
    }

    public void firstRender () {
        MapLayers layers = map.getLayers();
        for(MapLayer layer : layers) {
            int priority;
            if(layer instanceof MapGroupLayer) {
                for(MapLayer gl : ((MapGroupLayer) layer).getLayers()) {
                    {
                        priority = gl.getProperties().get("priority", Integer.class) != null ?
                                gl.getProperties().get("priority", Integer.class)
                                : -1;
                        gl.setVisible(priority == 0);
                    }
                }
            } else {
                priority = layer.getProperties().get("priority", Integer.class) != null ?
                        layer.getProperties().get("priority", Integer.class)
                        : -1;
                layer.setVisible(priority == 0);
            }


        }

    }

    public boolean test3 (Character character) {
        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) map.getLayers().get("objects");
        TiledMapTileLayer.Cell cell =
                tiledLayer.getCell((int) (character.getPosition().x / tiledLayer.getTileWidth()),
                                   (int) (character.getPosition().y / tiledLayer.getTileHeight() - 0.5));
        TiledMapTileLayer.Cell cell2 =
                tiledLayer.getCell((int) (character.getPosition().x / tiledLayer.getTileWidth() - 0.5),
                                   (int) (character.getPosition().y / tiledLayer.getTileHeight() - 0.5));
        return cell == null && cell2 == null;
    }

    public TiledMap getMap () {
        return map;
    }

    public OnceUponATime getGame () {
        return game;
    }

    private Body createBody () {
        Vector2 pos = getObjectPosition();
        BodyDef def = new BodyDef();
        def.fixedRotation = true;
        def.type = BodyDef.BodyType.StaticBody;
        Body body = game.getWorld().createBody(def);
        Vector2[] points = new Vector2[]{new Vector2(Math.abs(-1), Math.abs(-0.333333f)),
                new Vector2(Math.abs(52.6667f), Math.abs(-18f)),
                new Vector2(Math.abs(52.3333f), Math.abs(-44.3333f)),
                new Vector2(Math.abs(-1.33333f), Math.abs(-26.3333f))};
        PolygonShape polygon = new PolygonShape();
        polygon.set(points);
        body.createFixture(polygon, 1f);
        body.setTransform(pos.x, pos.y, 0);
        polygon.dispose();
        return body;
    }

    public void createEllipse (float x , float y, float weight, float height) {
        PolygonShape polygon = new PolygonShape();
        EllipseShape ellipseShape = new EllipseShape();
        Vector2[] upPoints = ellipseShape.upHemisphere(weight, height);
        Vector2[] downPoints = ellipseShape.downHemisphere(weight, height);
        BodyDef def = new BodyDef();
        polygon.set(upPoints);
        def.type = BodyDef.BodyType.StaticBody;
        Body upBody = game.getWorld().createBody(def);
        Body downBody = game.getWorld().createBody(def);
        upBody.createFixture(polygon, 1f);
        polygon.set(downPoints);
        downBody.createFixture(polygon, 1f);
        upBody.setTransform(x, y, 0);
        downBody.setTransform(x, y, 0);
    }

    private Vector2 getObjectPosition () {
        com.badlogic.gdx.maps.MapObject object = map.getLayers().get("Слой объектов 2").getObjects().get("телега");
        float x = object.getProperties().get("x", Float.class);
        float y = object.getProperties().get("y", Float.class);
        return new Vector2(x, y);
    }

}
