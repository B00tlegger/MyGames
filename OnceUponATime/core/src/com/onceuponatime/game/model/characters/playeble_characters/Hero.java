package com.onceuponatime.game.model.characters.playeble_characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.onceuponatime.game.model.BodyParts;
import com.onceuponatime.game.model.characters.Character;
import com.onceuponatime.game.model.characters.TypeOfRace;
import com.onceuponatime.game.model.characters.unplayeble_character.Skeleton;
import com.onceuponatime.game.model.weapon.Spear;
import com.onceuponatime.game.screens.GameScreen;
import com.onceuponatime.game.utils.Animator;
import com.onceuponatime.game.utils.AssetsType;

import java.awt.event.ActionEvent;
import java.util.List;

public class Hero extends Character {

    Body body;

    public Hero (GameScreen screen) {
        super(screen);
        create();
    }

    @Override
    public void create () {
        type = TypeOfRace.HUMAN;
        body = createBody();
        position = body.getPosition();
        weapon = new Spear();
        weapon.setPosition(body.getPosition());
        walkCycle = new TextureRegion(new Texture("png/walkcycle/BODY_male.png")).split(64, 64);

        attackCycle = new TextureRegion(new Texture(weapon.getAttackType().getValue()
                                                            + BodyParts.BODY
                                                            + type.getValue()
                                                            + AssetsType.PNG.getFormat())).split(64, 64);
        depth = 4;
        normSpeed = 100f;
        maxHp = 100;
        hP = maxHp;
        attackHasBeenMade = false;
        animator = new Animator(this, 8f, 24f);
    }

    @Override
    public void render (SpriteBatch batch, BitmapFont font24) {
        super.render(batch, font24);
        if(canAttack()) {
            findSide();
        }
        batch.draw(animator.doBodyAnimation(), body.getPosition().x - 32, body.getPosition().y - 8);
        if(canAttack() || animator.getAttackShot() != 0) {
            batch.draw(animator.doWeaponAnimation(), weapon.getPosition().x - 32, weapon.getPosition().y);
        }
        batch.setColor(0, 0, 0, 1);
        batch.draw(hpBar, body.getPosition().x - 42, body.getPosition().y + 62, 0, 0, hpBar.getWidth() + 4, 16, 1, 1, 0,
                   0, 0, 80, 20,
                   false, false);
        batch.setColor(1, 0, 0, 1);
        batch.draw(hpBar, body.getPosition().x - 40, body.getPosition().y + 64, 0, 0, hpBar.getWidth() * hP / maxHp, 12,
                   1, 1, 0, 0, 0,
                   80, 20,
                   false, false);
        batch.setColor(1, 1, 1, 1);
        font24.draw(batch, String.valueOf((int) hP), body.getPosition().x - 40, body.getPosition().y + 80, 80, 1,
                    false);

    }

    // перемещение в пространстве
    @Override
    public void toGo (float dt) {
        super.toGo(dt);
        direction.set(0, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(!canAttack()) {
                stayPosition = StayPosition.UP;
                body.setTransform(body.getPosition(), 0);
            }
            direction.y = 1;
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(!canAttack()) {
                stayPosition = StayPosition.DOWN;
                body.setTransform(body.getPosition(), 0);
            }
            direction.y = -1;
        } else {
            direction.y = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(!canAttack()) {
                stayPosition = StayPosition.RIGHT;
                body.setTransform(body.getPosition(), (float) Math.PI / 2);
            }
            direction.x = 1;
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(!canAttack()) {
                stayPosition = StayPosition.LEFT;
                body.setTransform(body.getPosition(), (float) Math.PI / 2);
            }
            direction.x = -1;
        } else {
            direction.x = 0;
        }
        body.setLinearVelocity(direction.scl(speed * 1000f));
//        temp.set(position).mulAdd(direction, speed);
//        if(canGo()) {
//            position.mulAdd(direction, speed);
//        }
    }

    public boolean stay () {
        return !Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                && !Gdx.input.isKeyPressed(Input.Keys.LEFT)
                && !Gdx.input.isKeyPressed(Input.Keys.UP)
                && !Gdx.input.isKeyPressed(Input.Keys.DOWN);

    }

    public void attack (float dt) {
        findClosestEnemy();
        super.attack(dt);
        if(canAttack()) {
            if(animator.getAttackShot() == attackCycle[stayPosition.getValue()].length / 2 + 1 && !attackHasBeenMade) {
                enemy.takeDamage(weapon.getDamage());
                attackHasBeenMade = true;
            }
            direction.set(enemy.getPosition()).sub(position);
        }
        if(animator.getAttackShot() <= 1 && attackHasBeenMade) {
            attackHasBeenMade = false;
        }
    }

    private void findClosestEnemy () {
        float minDist = Float.MAX_VALUE;
        List<Skeleton> monsters = screen.getMonsters();
        for(int i = 0; i < monsters.size(); i++) {
            float dist = getDistance(monsters.get(i));
            if(dist < minDist) {
                enemy = monsters.get(i);
                minDist = dist;
            }
        }
    }

    private boolean canGo () {
        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) screen.getMap().getLayers().get("collision");
        TiledMapTileLayer.Cell cell =
                tiledLayer.getCell((int) (temp.x / tiledLayer.getTileWidth()),
                                   (int) (temp.y / tiledLayer.getTileHeight() - 0.5));
        TiledMapTileLayer.Cell cell2 =
                tiledLayer.getCell((int) (temp.x / tiledLayer.getTileWidth() - 0.5),
                                   (int) (temp.y / tiledLayer.getTileHeight() - 0.5));
        return cell == null && cell2 == null;
    }

    private Vector2 getStartPosition () {
        MapObject object = screen.getMap().getLayers().get("spawn").getObjects().get("spawn");
        float x = object.getProperties().get("x", Float.class);
        float y = object.getProperties().get("y", Float.class);
        return new Vector2(x, y);
    }

    private Body createBody () {
        Vector2 startPosition = getStartPosition();
        BodyDef def = new BodyDef();
        def.fixedRotation = true;
        def.type = BodyDef.BodyType.DynamicBody;
        Body body = screen.getGame().getWorld().createBody(def);
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(18, 8);
        body.createFixture(polygon, 1f);
        body.setTransform(startPosition.x, startPosition.y + 64, 0);
        polygon.dispose();
        return body;
    }

    public Body getBody () {
        return body;
    }
}
