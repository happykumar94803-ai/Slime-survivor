package io.github.quillraven.slimesurvivor;

/** Contract for game objects that update their state every frame. */
public interface Updatable {
    void update(float deltaTime);
}
