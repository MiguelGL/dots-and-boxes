package com.mgl.dotsandboxes.server.model.player;

/**
 * Kind of the player a turn or anything may refer to.
 */
public enum PlayerKind {

    COMPUTER,
    HUMAN;

    public boolean computesScore() {
        return this != COMPUTER;
    }

}
