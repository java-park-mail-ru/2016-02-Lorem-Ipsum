package game.gameprimitives.collisions;

import game.gameprimitives.Body;

public interface CollisionHandler {
    void handleCollision(Body first, Body second);
}
