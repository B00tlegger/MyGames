package com.onceuponatime.game.model.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.onceuponatime.game.model.weapon.Weapon;
import com.onceuponatime.game.screens.GameScreen;
import com.onceuponatime.game.utils.Animator;


public abstract class Character extends MapObject{

    protected GameScreen screen;

    protected Vector2 direction;
    protected Vector2 temp;

    protected TypeOfRace type;
    protected TextureRegion[][] walkCycle;
    protected TextureRegion[][] attackCycle;
    protected StayPosition stayPosition;
    protected boolean attackHasBeenMade;
    protected float damageEffectCounter;
    protected float normSpeed;
    protected float speed;
    protected float attackSpeed;
    protected float hP;
    protected float maxHp;

    protected Texture hpBar;

    protected Character enemy;
    protected Weapon weapon;
    protected Animator animator;

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
        restore();
        attack(dt);

    }

    protected float getDistance (Character enemy) {
        return enemy.getPosition().dst(position);
    }

    protected void toGo (float dt){
        speed = normSpeed * dt;
    }

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

    protected void restore () {
        damageEffectCounter -= 0.5f;
        if(damageEffectCounter <= 0) {
            damageEffectCounter = 0;
        }

    }

    public void findSide () {
        if(direction.angleDeg() > 45.0f && direction.angleDeg() <= 135.0f) {
            stayPosition = StayPosition.UP;
        } else if(direction.angleDeg() > 135.0f && direction.angleDeg() <= 225.0f) {
            stayPosition = StayPosition.LEFT;
        } else if(direction.angleDeg() > 225.0f && direction.angleDeg() <= 315.0f) {
            stayPosition = StayPosition.DOWN;
        } else if(direction.angleDeg() > 315.0f && direction.angleDeg() <= 360.9f ||
                direction.angleDeg() >= 0.0f && direction.angleDeg() <= 45.0f || direction.angleDeg() == 0.0f) {
            stayPosition = StayPosition.RIGHT;
        }
    }

    public boolean canAttack () {
        if(enemy != null) {
            if(getDistance(enemy) < weapon.getAttackRadius() && enemy.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlive(){
        return hP > 0;
    }

    public void attack (float dt){
        attackSpeed = weapon.getAttackRate() * dt;
    };

    public abstract boolean stay();

    public float getAttackSpeed () {
        return attackSpeed;
    }

    public void setAttackSpeed (float attackSpeed) {
        this.attackSpeed = attackSpeed;
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

    public Vector2 getDirection () {
        return direction;
    }

    public TextureRegion[][] getWalkCycle () {
        return walkCycle;
    }

    public TextureRegion[][] getAttackCycle () {
        return attackCycle;
    }

    public float getSpeed () {
        return speed;
    }

    public StayPosition getStayPosition () {
        return stayPosition;
    }

    public Character getEnemy () {
        return enemy;
    }

    public Weapon getWeapon () {
        return weapon;
    }


}
