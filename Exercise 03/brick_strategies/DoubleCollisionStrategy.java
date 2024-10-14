package bricker.brick_strategies;

import bricker.main.BrickerGameManager; // Importing BrickerGameManager class from bricker.main package
import danogl.GameObject; // Importing GameObject class from danogl package

/**
 * A collision strategy that applies two different collision strategies simultaneously to handle collisions.
 * This strategy is used when a brick has multiple special behaviors.
 */
public class DoubleCollisionStrategy extends BasicCollisionStrategy {

    private final CollisionStrategy[] collisionStrategies; // An array of collision strategies to apply

    /**
     * Constructs a DoubleCollisionStrategy with the specified collision strategies.
     *
     * @param brickerGameManager    The BrickerGameManager instance.
     * @param collisionStrategies   An array of collision strategies to apply.
     */
    public DoubleCollisionStrategy(BrickerGameManager brickerGameManager, CollisionStrategy[]
            collisionStrategies) {
        super(brickerGameManager);
        this.collisionStrategies = collisionStrategies;
    }

    /**
     * Handles collision by applying each collision strategy in the array to the colliding objects.
     *
     * @param thisObj   The first colliding object.
     * @param otherObj  The second colliding object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        for (var collisionStrategy : collisionStrategies) {
            collisionStrategy.onCollision(thisObj, otherObj);
        }
    }

    /**
     * Ends the collision strategy by calling the endStrategy method of each contained collision strategy.
     */
    @Override
    public void endStrategy() {
        super.endStrategy();
        for (var collisionStrategy : collisionStrategies) {
            collisionStrategy.endStrategy();
        }
    }

}
