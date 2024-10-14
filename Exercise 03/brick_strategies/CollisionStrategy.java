package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Defines the interface for collision strategies between a brick and another GameObject.
 * Helps to ensure polymorphism, in the design-pattern strategy.
 */
public interface CollisionStrategy {

    /**
     * Handles collision between a brick and another GameObject.
     *
     * @param thisObj   The current game object - brick.
     * @param otherObj  The other game object involved in the collision.
     */
    void onCollision(GameObject thisObj, GameObject otherObj);

    /**
     * Ends the collision strategy.
     * This method can be used to perform cleanup or additional actions when the brick holds this strategy
     * was already blasted.
     */
    void endStrategy();
}
