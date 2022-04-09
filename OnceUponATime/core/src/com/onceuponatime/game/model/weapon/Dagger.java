package com.onceuponatime.game.model.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.onceuponatime.game.model.BodyParts;
import com.onceuponatime.game.utils.AssetsType;

public class Dagger extends Weapon{
    public Dagger () {
        this.name = "Кинжал";
        this.attackRadius = 50;
        this.attackRate = 300f;
        this.damage = 5;
        attackType = AttackType.SLASH;
        weaponType = WeaponType.DAGGER;
        this.attackTextures = new TextureRegion(new Texture(attackType.getValue()
                                                                    + BodyParts.WEAPON
                                                                    + weaponType.getValue()
                                                                    + AssetsType.PNG.getFormat())).split(64, 64);
    }
}
