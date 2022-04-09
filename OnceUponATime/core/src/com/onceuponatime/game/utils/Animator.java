package com.onceuponatime.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.onceuponatime.game.model.characters.Character;

public class Animator {

    private Character character;

    private float animationTimer;
    private final float startWalkAnimationTimer;
    private float startAttackAnimationTimer;
    private int walkShot = 0;
    private int attackShot = 0;

    public Animator (Character character, float startWalkAnimationTimer,
                     float startAttackAnimationTimer) {
        this.character = character;
        this.startWalkAnimationTimer = startWalkAnimationTimer;
        this.animationTimer = startWalkAnimationTimer;
        this.startAttackAnimationTimer = startAttackAnimationTimer;
    }

    public TextureRegion doBodyAnimation () {
        if(character.canAttack()){
            return attackAnimation();
        }

        return attackShot == 0 ? walkAnimation() : attackAnimation();
    }

    private TextureRegion attackAnimation () {
        character.getDirection().set(character.getEnemy().getPosition()).sub(character.getPosition()).nor();
        animationTimer -= character.getAttackSpeed();
        if(animationTimer <= 0) {
            attackShot += 1;
            if(attackShot >= character.getAttackCycle()[character.getStayPosition().getValue()].length) {
                if(character.canAttack()) {
                    attackShot = 1;
                } else {
                    attackShot = 0;
                }

            }
            animationTimer = startAttackAnimationTimer;
        }
        return character.getAttackCycle()[character.getStayPosition().getValue()][attackShot];
    }

    private TextureRegion walkAnimation () {
        if(character.stay()) {
            walkShot = 0;
        } else {
            animationTimer -= character.getSpeed();
            if(animationTimer <= 0) {
                walkShot += 1;
                if(walkShot >= character.getWalkCycle()[character.getStayPosition().getValue()].length) {
                    walkShot = 1;
                }
                animationTimer = startWalkAnimationTimer;
            }
        }
        return character.getWalkCycle()[character.getStayPosition().getValue()][walkShot];
    }

    public TextureRegion doWeaponAnimation () {
        return character.getWeapon().getAttackTextures()[character.getStayPosition().getValue()][attackShot];
    }

    public int getAttackShot () {
        return attackShot;
    }
}
