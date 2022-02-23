package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.model.characters.Character;
import com.mygdx.game.model.characters.MapObject;
import com.mygdx.game.model.characters.playeble_characters.Hero;
import com.mygdx.game.model.characters.unplayeble_character.Skeleton;

import java.util.*;

public class GameScreen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private int maxMonstersCount;
    float timer;
    private Map map;
    private List<MapObject> allMapObjects;
    private List<com.mygdx.game.model.characters.Character> allCharacters;
    private List<Skeleton> monsters;

    private Hero hero;

    public GameScreen (SpriteBatch batch) {
        this.batch = batch;
    }

    public void create () {
        timer = 0;
        map = new Map();
        hero = new Hero(this);
        allCharacters = new ArrayList<>();
        allMapObjects = new ArrayList<>();
        monsters = new ArrayList<>();
        monsters.add(new Skeleton(this));
        allCharacters.addAll(monsters);
        allCharacters.add(hero);
        allMapObjects.addAll(allCharacters);
        allMapObjects.addAll(map.getObjectList());
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        maxMonstersCount = 5;
    }

    public void render () {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        map.render(batch);
        Collections.sort(allMapObjects, new Comparator<MapObject>() {
            @Override
            public int compare (MapObject o1, MapObject o2) {
                return (int) ((o2.getPosition().y + o2.getDepth()) - (o1.getPosition().y + o1.getDepth()));
            }
        });
        for(int i = 0; i < allMapObjects.size(); i++) {
            if(allMapObjects.get(i) instanceof Character ){
                if(((Character)allMapObjects.get(i)).isAlive()){
                    allMapObjects.get(i).render(batch, font24);
                }
            } else {
                allMapObjects.get(i).render(batch, font24);
            }

        }
        respawnMonsters(dt);
        batch.end();
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

    public void setHero (Hero hero) {
        this.hero = hero;
    }

    public List<Character> getAllCharacters () {
        return allCharacters;
    }

    public void setAllCharacters (List<Character> allCharacters) {
        this.allCharacters = allCharacters;
    }

    public List<Skeleton> getMonsters () {
        return monsters;
    }

    public void setMonsters (List<Skeleton> monsters) {
        this.monsters = monsters;
    }

    public int getMaxMonstersCount () {
        return maxMonstersCount;
    }

    public Map getMap () {
        return map;
    }
}
