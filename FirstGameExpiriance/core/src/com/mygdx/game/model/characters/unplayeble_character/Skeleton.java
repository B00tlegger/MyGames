package com.mygdx.game.model.characters.unplayeble_character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.characters.Character;
import com.mygdx.game.model.weapon.Weapon;
import com.mygdx.game.screens.GameScreen;

public class Skeleton extends Character {
    private double agraRadius;
    private float moveTimer;
    private final float changeAnimation;


    public Skeleton (GameScreen game) {
        super(game);
        changeAnimation = 8f;
        create();
    }

    @Override
    public void create () {
        walkTextures = new TextureRegion(new Texture("png/walkcycle/BODY_skeleton.png")).split(64, 64);
        weapon = new Weapon("sword", 40, 1, 10);
        depth = 4;

        maxHp = 40;
        hP = maxHp;

        position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        while(!screen.getMap().isPassable(position)) {
            position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }

        speed = 40.f;
        agraRadius = 200.0;
    }

    @Override
    public void render (SpriteBatch batch, BitmapFont font24) {
        super.render(batch, font24);

        batch.draw(walkAnimation(normalSpeed), position.x - 40, position.y - 20);
        batch.setColor(0, 0, 0, 1);
        batch.draw(hpBar, position.x - 42, position.y + 80 - 20, 0, 0, hpBar.getWidth() + 4, 16, 1, 1, 0, 0, 0, 80, 20,
                   false, false);
        batch.setColor(1, 0, 0, 1);
        batch.draw(hpBar, position.x - 40, position.y + 80 - 18, 0, 0, hpBar.getWidth() * hP / maxHp, 12, 1, 1, 0, 0, 0,
                   80, 20,
                   false, false);
        batch.setColor(1, 1, 1, 1);
        font24.draw(batch, String.valueOf((int) hP), position.x - 40, position.y + 77, 80, 1, false);
    }

    @Override
    public void toGo (float dt) {
        normalSpeed = getSpeed() * dt;

        if(getDistance() < agraRadius) {
            harassment(normalSpeed);
        } else {
            moveTimer -= dt;
            changeDirection();
        }
        findSide();
        temp.set(position).mulAdd(direction, normalSpeed);
        if(screen.getMap().isPassable(temp)) {
            position.set(temp);
        }
        findSide();
    }

    @Override
    public void update (float dt) {
        super.update(dt);
    }

    private void harassment (float speed) {
        direction.set(screen.getHero().getPosition()).sub(position).nor();
    }

    public void attack (float dt) {
        if(getDistance() < weapon.getAttackRadius()) {
            attackTimer += dt;
            if(attackTimer >= weapon.getAttackRate()) {
                attackTimer = 0;
                screen.getHero().takeDamage(weapon.getDamage());
            }
        }
        attackTimer = 0;
    }

    private float getDistance () {
        return screen.getHero().getPosition().dst(position);
    }

    private void changeDirection () {
        if(moveTimer <= 0.0f) {
            moveTimer = MathUtils.random(1, 2);
            float x = MathUtils.random(-1, 1);
            float y = MathUtils.random(-1, 1);
            direction.set(x, y);
            direction.nor();
        }
    }

    private boolean stay () {
        float oldX = position.x;
        float oldY = position.y;
        float newX = position.x + temp.x;
        float newY = position.y + temp.y;
        return oldX == newX && oldY == newY;
    }

    private TextureRegion walkAnimation (float speed) {
        if(stay()) {
            shot = 0;
        } else {
            animationTimer -= normalSpeed;
            if(animationTimer <= 0) {
                shot += 1;
                if(shot >= walkTextures[stayPosition.getValue()].length) {
                    shot = 0;
                }
                animationTimer = changeAnimation;
            }
        }
        return walkTextures[stayPosition.getValue()][shot];
    }
}
