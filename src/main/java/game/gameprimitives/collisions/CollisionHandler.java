package game.gameprimitives.collisions;

import game.gameprimitives.Body;

public interface CollisionHandler {
    public void handleCollision(Body first, Body second);
}
