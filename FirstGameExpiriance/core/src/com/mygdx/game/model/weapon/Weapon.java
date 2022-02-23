package com.mygdx.game.model.weapon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Weapon {
    protected TextureRegion[][] attackTextures;
    protected String name;
    protected float attackRadius;
    protected float attackRate;
    protected int damage;
    protected Vector2 position;


    public Weapon (String name, float attackRadius, float attackRate, int damage) {
        this.name = name;
        this.attackRadius = attackRadius;
        this.attackRate = attackRate;
        this.damage = damage;
    }

    public Weapon () {
    }

    public TextureRegion[][] getAttackTextures () {
        return attackTextures;
    }

    public void setAttackTextures (TextureRegion[][] attackTextures) {
        this.attackTextures = attackTextures;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public float getAttackRadius () {
        return attackRadius;
    }

    public void setAttackRadius (float attackRadius) {
        this.attackRadius = attackRadius;
    }

    public float getAttackRate () {
        return attackRate;
    }

    public void setAttackRate (float attackRate) {
        this.attackRate = attackRate;
    }

    public int getDamage () {
        return damage;
    }

    public void setDamage (int damage) {
        this.damage = damage;
    }

    public Vector2 getPosition () {
        return position;
    }

    public void setPosition (Vector2 position) {
        this.position = position;
    }
}
