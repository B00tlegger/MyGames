package com.mygdx.game.model.characters.playeble_characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.characters.Character;
import com.mygdx.game.model.characters.unplayeble_character.Skeleton;
import com.mygdx.game.model.weapon.Spear;
import com.mygdx.game.screens.GameScreen;

import java.util.List;

public class Hero extends Character {
    private final float changeAnimation;
    private Skeleton closestEnemy;
    private boolean attackHasBeenMade;
    private final float changeAttackAnimation;


    public Hero (GameScreen screen) {
        super(screen);
        changeAnimation = 8f;
        changeAttackAnimation = 24f;
        create();
    }

    @Override
    public void create () {
        walkTextures = new TextureRegion(new Texture("png/walkcycle/BODY_male.png")).split(64, 64);
        attackTextures = new TextureRegion(new Texture("png/thrust/BODY_animation.png")).split(64, 64);
        position = new Vector2(200, 200);


        weapon = new Spear();
        weapon.setPosition(position);


        depth = 4;
        speed = 100.f;
        maxHp = 100;
        hP = maxHp;
        animationTimer = changeAnimation;
        attackHasBeenMade = false;
    }

    @Override
    public void render (SpriteBatch batch, BitmapFont font24) {
        super.render(batch, font24);
        batch.draw(doBodyAnimation(), position.x - 40, position.y - 20);
        if(canAttack()) {
            batch.draw(doWeaponAnimation(), weapon.getPosition().x - 40, weapon.getPosition().y - 20);
        }
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

    // перемещение в пространстве
    @Override
    public void toGo (float dt) {
        normalSpeed = getSpeed() * dt;
        direction.set(0, 0);

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(!canAttack()) {
                stayPosition = StayPosition.UP;
            }
            direction.y = 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(!canAttack()) {
                stayPosition = StayPosition.DOWN;
            }
            direction.y = -1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(!canAttack()) {
                stayPosition = StayPosition.RIGHT;
            }
            direction.x = 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(!canAttack()) {
                stayPosition = StayPosition.LEFT;
            }
            direction.x = -1;
        }
        temp.set(position).mulAdd(direction, normalSpeed);
        if(screen.getMap().isPassable(temp)) {
            position.mulAdd(direction, normalSpeed);
        }
    }

    private boolean stay () {
        return !Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                && !Gdx.input.isKeyPressed(Input.Keys.LEFT)
                && !Gdx.input.isKeyPressed(Input.Keys.UP)
                && !Gdx.input.isKeyPressed(Input.Keys.DOWN);

    }

    // Прощёт атаки

    public void attack (float dt) {
        findClosestEnemy();
        if(canAttack()) {
            if(attackShot == attackTextures[stayPosition.getValue()].length / 2 && !attackHasBeenMade) {
                closestEnemy.takeDamage(weapon.getDamage());
                attackHasBeenMade = true;
            }
        }
        if(attackShot == 0 && attackHasBeenMade) {
            attackHasBeenMade = false;
        }
    }

    private void findClosestEnemy () {
        float minDist = Float.MAX_VALUE;
        List<Skeleton> monsters = screen.getMonsters();
        for(int i = 0; i < monsters.size(); i++) {
            float dist = getDistance(monsters.get(i));
            if(dist < minDist) {
                closestEnemy = monsters.get(i);
                minDist = dist;
            }
        }
    }

    protected boolean canAttack () {
        if(closestEnemy != null) {
            if(getDistance(closestEnemy) < weapon.getAttackRadius()) {
                return true;
            }
        }
        return false;
    }

    // Анимация движений

    private TextureRegion doBodyAnimation () {
        if(canAttack()) {
            return attackAnimation();
        }
        attackShot = 0;
        direction.set(0, 0);
        return walkAnimation();
    }


    private TextureRegion walkAnimation () {
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

    private TextureRegion attackAnimation () {

        direction.set(closestEnemy.getPosition()).sub(position).nor();
        animationTimer -= normalSpeed;
        if(animationTimer <= 0) {
            attackShot += 1;
            if(attackShot >= attackTextures[stayPosition.getValue()].length) {
                attackShot = 0;
            }
            animationTimer = changeAttackAnimation;
        }
        findSide();
        return attackTextures[stayPosition.getValue()][attackShot];
    }

    //Анимация оружия

    private TextureRegion doWeaponAnimation () {
        return weapon.getAttackTextures()[stayPosition.getValue()][attackShot];
    }

    private void walkAnimationWeapon () {
        if(stayPosition.equals(StayPosition.RIGHT)) {
        } else if(stayPosition.equals(StayPosition.UP)) {

        } else if(stayPosition.equals(StayPosition.LEFT)) {

        } else {
        }
    }


}
