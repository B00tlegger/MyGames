package com.onceuponatime.game.model.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.onceuponatime.game.model.BodyParts;
import com.onceuponatime.game.utils.AssetsType;

public class Spear extends Weapon {

    public Spear () {
        this.name = "Копьё";
        this.attackRadius = 80;
        this.attackRate = 150;
        this.damage = 10;
        attackType = AttackType.THRUST;
        weaponType = WeaponType.SPEAR;
        this.attackTextures = new TextureRegion(new Texture(attackType.getValue()
                                                                    + BodyParts.WEAPON.toString()
                                                                    + weaponType.getValue()
                                                                    + AssetsType.PNG.getFormat())).split(64, 64);
    }
}
