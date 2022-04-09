package com.onceuponatime.game.model.characters.unplayeble_character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.onceuponatime.game.model.BodyParts;
import com.onceuponatime.game.model.characters.Character;
import com.onceuponatime.game.model.characters.TypeOfRace;
import com.onceuponatime.game.model.weapon.Dagger;
import com.onceuponatime.game.screens.GameScreen;
import com.onceuponatime.game.utils.Animator;
import com.onceuponatime.game.utils.AssetsType;

public class Skeleton extends Character {
    private double agraRadius;
    private float moveTimer;

    public Skeleton (GameScreen game) {
        super(game);
        create();
    }

    @Override
    public void create () {
        type = TypeOfRace.SKELETON;
        position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        walkCycle = new TextureRegion(new Texture("png/walkcycle/BODY_skeleton.png")).split(64, 64);
        weapon = new Dagger();
        weapon.setPosition(position);
        attackCycle = new TextureRegion(new Texture(weapon.getAttackType().getValue()
                                                            + BodyParts.BODY
                                                            + type.getValue()
                                                            + AssetsType.PNG.getFormat())).split(64, 64);
        depth = 4;
        maxHp = 40;
        hP = maxHp;
        normSpeed = 40.f;
        agraRadius = 200.0;
        enemy = screen.getHero();
        animator = new Animator(this, 8f, 20f);
    }

    @Override
    public void render (SpriteBatch batch, BitmapFont font24) {
        super.render(batch, font24);

        batch.draw(animator.doBodyAnimation(), position.x - 40, position.y - 20);
        if(canAttack() || animator.getAttackShot() != 0) {
            batch.draw(animator.doWeaponAnimation(), weapon.getPosition().x - 40, weapon.getPosition().y - 20);
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

    @Override
    public void toGo (float dt) {
        super.toGo(dt);

        if(getDistance() < agraRadius) {
            harassment(speed);
        } else {
            moveTimer -= dt;
            changeDirection();
        }
        temp.set(position).mulAdd(direction, speed);
//        if(screen.getMap().isPassable(temp)) {
            position.set(temp);
//        }
        findSide();
    }

    @Override
    public void update (float dt) {
        super.update(dt);
    }

    private void harassment (float speed) {
        direction.set(enemy.getPosition()).sub(position).nor();
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

    public boolean stay () {
        float oldX = position.x;
        float oldY = position.y;
        float newX = position.x + temp.x;
        float newY = position.y + temp.y;
        return oldX == newX && oldY == newY;
    }

    public void attack (float dt) {
        super.attack(dt);
        if(canAttack()) {
            if(animator.getAttackShot() == attackCycle[stayPosition.getValue()].length / 2 + 1 && !attackHasBeenMade) {
                enemy.takeDamage(weapon.getDamage());
                attackHasBeenMade = true;
            }
            direction.set(enemy.getPosition()).sub(position);
        }
        if(animator.getAttackShot() <= 1 && attackHasBeenMade) {
            attackHasBeenMade = false;
        }
    }
}
