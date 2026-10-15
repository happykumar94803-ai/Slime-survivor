package io.github.quillraven.slimesurvivor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Enemy extends GameObject {
    private static final float SCALE = 1 / 32f;
    private static final float SPEED = 1.5f;

    private final Player player;

    public Enemy(float x, float y, Texture texture, Player player) {
        super(x, y, texture.getWidth() * SCALE, texture.getHeight() * SCALE, texture);
        this.player = player;
    }

    @Override
    public void update(float deltaTime) {
        Vector2 direction = player.getCenter(TMP_VEC2)
            .sub(rect.x + rect.width * 0.5f, rect.y + rect.height * 0.5f) // direction to player
            .nor() // normalize to avoid faster diagonal movement
            .scl(SPEED * deltaTime); // multiply by enemy speed

        // move towards the player center
        rect.setPosition(rect.getX() + direction.x, rect.getY() + direction.y);
    }

    public static Enemy spawn(Viewport gameViewport, Texture texture, Player player) {
        int edge = MathUtils.random(3); // 0: top, 1: right, 2: bottom, 3: left
        float x, y;

        switch (edge) {
            case 0 -> { // Top
                x = MathUtils.random(0, 1) * gameViewport.getWorldWidth();
                y = gameViewport.getWorldHeight();
            }
            case 1 -> { // Right
                x = gameViewport.getWorldWidth();
                y = MathUtils.random(0, 1) * gameViewport.getWorldHeight();
            }
            case 2 -> { // Bottom
                x = MathUtils.random(0, 1) * gameViewport.getWorldWidth();
                y = -texture.getHeight() * SCALE;
            }
            default -> { // Left
                x = -texture.getWidth() * SCALE;
                y = MathUtils.random(0, 1) * gameViewport.getWorldHeight();
            }
        }

        return new Enemy(x, y, texture, player);
    }
}
