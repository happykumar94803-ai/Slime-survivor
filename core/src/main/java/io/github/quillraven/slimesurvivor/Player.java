package io.github.quillraven.slimesurvivor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends GameObject {
    private static final float SCALE = 1 / 32f;
    private static final float SPEED = 2f;
    private static final float LIFE = 5f;
    private static final float ATTACK_COOLDOWN = 1.6f;

    private final Vector2 moveDirection = new Vector2(0, 0);
    private final Vector2 lastDirection = new Vector2(1, 0); // used for attacks; default is right
    private float life = LIFE;
    private float attackTimer;
    private final Array<Attack> attacks = new Array<>();
    private final Animation<Texture> attackAnimation;
    private final Viewport gameViewport;
    private final Sound attackSfx;

    public Player(float x, float y,
                  Viewport gameViewport,
                  Texture texture,
                  Animation<Texture> attackAnimation,
                  Sound attackSfx) {
        super(x, y, texture.getWidth() * SCALE, texture.getHeight() * SCALE, texture);
        reset(x, y);
        this.gameViewport = gameViewport;
        this.attackAnimation = attackAnimation;
        this.attackSfx = attackSfx;
    }

    public void reset(float x, float y) {
        rect.setPosition(x, y);
        life = LIFE;
        attackTimer = ATTACK_COOLDOWN;
        attacks.clear();
    }

    @Override
    public void update(float deltaTime) {
        // Spawn new attack hitbox?
        if (canAttack(deltaTime)) {
            Vector2 playerCenter = getCenter(TMP_VEC2);
            attackSfx.play();
            attacks.add(new Attack(playerCenter, lastDirection, attackAnimation));
        }

        // Update attack hitbox life spans
        var iterator = attacks.iterator();
        while (iterator.hasNext()) {
            Attack attack = iterator.next();
            attack.update(deltaTime);
            if (attack.isDone()) {
                iterator.remove();
            }
        }

        // Move player
        move(deltaTime);
    }

    private void move(float deltaTime) {
        if (moveDirection.isZero()) return;

        float newX = rect.getX() + moveDirection.x * SPEED * deltaTime;
        float newY = rect.getY() + moveDirection.y * SPEED * deltaTime;

        // Clamp to screen bounds
        newX = Math.clamp(newX, 0, gameViewport.getWorldWidth() - rect.getWidth());
        newY = Math.clamp(newY, 0, gameViewport.getWorldHeight() - rect.getHeight());

        rect.setPosition(newX, newY);
    }

    public void changeDirection(Vector2 direction) {
        if (!direction.isZero()) {
            lastDirection.set(direction);
        }
        moveDirection.set(direction);
    }

    public float getLife() {
        return life;
    }

    public void subLife(float amount) {
        this.life -= amount;
    }

    public boolean isDead() {
        return life <= 0f;
    }

    public boolean canAttack(float delta) {
        attackTimer -= delta;
        if (attackTimer <= 0f) {
            attackTimer = ATTACK_COOLDOWN;
            return true;
        }

        return false;
    }

    public Array<Attack> getAttacks() {
        return attacks;
    }
}
