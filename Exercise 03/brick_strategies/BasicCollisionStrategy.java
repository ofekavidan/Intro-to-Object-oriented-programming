package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Layer;

/**
 * A collision strategy that applies the basic behavior - removing the brick and decrease the bricks counter.
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    // Layer ID for given thisObj of onCollision() method:
    private static final int THIS_LAYER_ID = Layer.STATIC_OBJECTS;

    /**
     * A reference to the BrickerGameManager, that will be used in all the inherit realizations.
     */
    protected final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new BasicCollisionStrategy object.
     *
     * @param brickerGameManager The BrickerGameManager instance managing the game.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles collision between a brick and another GameObject.
     * Removes the brick from the game if collision occurs.
     *
     * @param thisObj   The current game object - brick.
     * @param otherObj  The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (brickerGameManager.removeGameObject(thisObj, THIS_LAYER_ID)) {
            thisObj.setTag(Constants.BLASTED_BRICK_TAG); // Set the tag of the object to indicate collision
            brickerGameManager.decreaseBricksCounter(); // Decrease the bricks counter in the game manager
        }
    }

    /**
     * Ends the collision strategy.
     */
    @Override
    public void endStrategy() {
        // No specific actions required to end this collision strategy
    }
}
