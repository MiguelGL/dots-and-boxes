package com.mgl.dotsandboxes.server.model.game;

/**
 * Represents the lifecycle phases of a game.
 */
public enum GameState {

    /**
     * The game has been created, but not yet started (no player made any turn yet).
     */
    CREATED,
    /**
     * The game has been started and is still ongoing.
     */
    ONGOING,
    /**
     * Game is done and no further playable.
     */
    TERMINATED;

    public boolean hasStarted() {
        return this != CREATED;
    }

    public boolean hasTerminated() {
        return this == TERMINATED;
    }

}
