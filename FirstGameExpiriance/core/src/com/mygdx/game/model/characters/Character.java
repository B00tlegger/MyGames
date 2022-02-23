package com.mygdx.game.model.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.weapon.Weapon;
import com.mygdx.game.screens.GameScreen;

public abstract class Character extends MapObject{

    protected GameScreen screen;
    protected Weapon weapon;
    protected float speed;
    protected Texture hpBar;
    protected float hP;
    protected float maxHp;
    protected float attackTimer;
    protected Vector2 direction;
    protected Vector2 temp;
    protected TextureRegion[][] walkTextures;
    protected TextureRegion[][] attackTextures;
    protected StayPosition stayPosition;
    protected float animationTimer;
    protected int shot = 0;
    protected int attackShot = 0;
    protected float normalSpeed;
    protected float damageEffectCounter;




    public Character (GameScreen screen) {
        this.screen = screen;
        hpBar = new Texture("Bar.png");
        direction = new Vector2(0, 0);
        temp = new Vector2(0, 0);
        stayPosition = StayPosition.DOWN;
    }

    public abstract void create ();

    @Override
    public void render (SpriteBatch batch, BitmapFont font24) {
        if(damageEffectCounter > 0) {
            batch.setColor(1, 1 - damageEffectCounter, 1 - damageEffectCounter, 1);
        }
    }

    public void update (float dt) {
        toGo(dt);
        restore(dt);
        attack(dt);

    }

    protected float getDistance (Character enemy) {
        return enemy.getPosition().dst(position);
    }

    protected abstract void toGo (float dt);

    public void takeDamage (int damage) {
        hP -= damage;
        damageEffectCounter += 1f;
        if(damageEffectCounter >= 1) {
            damageEffectCounter = 1;
        }
        if(hP <= 0) {
            hP = 0;
        }
    }

    protected void restore (float dt) {
        damageEffectCounter -= 0.5f;
        if(damageEffectCounter <= 0) {
            damageEffectCounter = 0;
        }

    }

    protected void findSide () {
        if(direction.angleDeg() > 45 && direction.angleDeg() <= 135) {
            stayPosition = StayPosition.UP;
        } else if(direction.angleDeg() > 135 && direction.angleDeg() <= 225) {
            stayPosition = StayPosition.LEFT;
        } else if(direction.angleDeg() > 225 && direction.angleDeg() <= 315) {
            stayPosition = StayPosition.DOWN;
        } else if(direction.angleDeg() > 315 && direction.angleDeg() <= 0 ||
                direction.angleDeg() >= 0 && direction.angleDeg() <= 45) {
            stayPosition = StayPosition.RIGHT;
        }
    }

    public Texture getNormalImg () {
        return texture;
    }

    public float getSpeed () {
        return speed;
    }

    public void setSpeed (float speed) {
        this.speed = speed;
    }

    public Vector2 getPosition () {
        return position;
    }

    public void setPosition (Vector2 position) {
        this.position = position;
    }

    public boolean isAlive(){
        return hP > 0;
    }

    public abstract void attack (float dt);

    public StayPosition getStayPosition(){
        return stayPosition;
    }

    public Vector2 getDirection(){
        return direction;
    }

    public enum StayPosition{
        UP(0),
        DOWN(2),
        LEFT(1),
        RIGHT(3);

        private final int value;

        private StayPosition(int value) {
            this.value = value;
        }

        public int getValue () {
            return value;
        }
    }

}
