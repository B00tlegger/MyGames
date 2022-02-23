package com.mygdx.game.model.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spear extends Weapon {

    public Spear () {
        this.name = "Копьё";
        this.attackTextures = new TextureRegion(new Texture("png/thrust/WEAPON_spear.png")).split(64, 64);
        this.attackRadius = 80;
        this.attackRate = 2;
        this.damage = 10;
    }


    public Spear (String name, float attackRadius, float attackRate, int damage) {
        super(name, attackRadius, attackRate, damage);
    }
}
