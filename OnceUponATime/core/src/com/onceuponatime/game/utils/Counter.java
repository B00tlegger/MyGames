package com.onceuponatime.game.utils;

public class Counter {
    private float damageEffectCounter;
    private float attackTimer;
    private float changeAnimation;

    private float animationTimer;
    private float moveTimer;
    private float changeAttackAnimationTimer;

    public Counter (){
        damageEffectCounter = 0;
    }

    public Counter (float damageEffectCounter, float attackTimer, float changeAnimation, float animationTimer,
                    float moveTimer,
                    float changeAttackAnimationTimer) {
        this.damageEffectCounter = damageEffectCounter;
        this.attackTimer = attackTimer;
        this.changeAnimation = changeAnimation;
        this.animationTimer = animationTimer;
        this.moveTimer = moveTimer;
        this.changeAttackAnimationTimer = changeAttackAnimationTimer;
    }
    public void plusDamageEffectCounter(float count){
        damageEffectCounter += count;
    }
    public void resetDamageEffectCounter(){
        damageEffectCounter = 0;
    }

    public void minusDamageEffectCounter(float count){
        damageEffectCounter -= count;
    }

    public float getDamageEffectCounter () {
        return damageEffectCounter;
    }

    public void setDamageEffectCounter (float damageEffectCounter) {
        this.damageEffectCounter = damageEffectCounter;
    }

    public float getAttackTimer () {
        return attackTimer;
    }

    public void setAttackTimer (float attackTimer) {
        this.attackTimer = attackTimer;
    }

    public float getAnimationTimer () {
        return animationTimer;
    }

    public void setAnimationTimer (float animationTimer) {
        this.animationTimer = animationTimer;
    }

    public float getMoveTimer () {
        return moveTimer;
    }

    public void setMoveTimer (float moveTimer) {
        this.moveTimer = moveTimer;
    }

    public float getChangeAttackAnimationTimer () {
        return changeAttackAnimationTimer;
    }

    public void setChangeAttackAnimationTimer (float changeAttackAnimationTimer) {
        this.changeAttackAnimationTimer = changeAttackAnimationTimer;
    }

    public float getChangeAnimation () {
        return changeAnimation;
    }

    public void setChangeAnimation (float changeAnimation) {
        this.changeAnimation = changeAnimation;
    }
}
