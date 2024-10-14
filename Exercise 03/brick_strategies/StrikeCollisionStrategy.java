package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A collision strategy triggered when a brick collision occurs, causing a strike (or power-up) to be
 * added to the game.
 */
public class StrikeCollisionStrategy extends BasicCollisionStrategy {

    private static final Vector2 HEART_VELOCITY = new Vector2(0, 100);

    private final float heartSize;
    private Heart heart;

    /**
     * Constructs a StrikeCollisionStrategy.
     *
     * @param brickerGameManager The game manager controlling the game state.
     * @param heartSize          The size of the heart power-up to be added.
     */
    public StrikeCollisionStrategy(BrickerGameManager brickerGameManager, float heartSize) {
        super(brickerGameManager);
        this.heartSize = heartSize;
    }

    /**
     * Handles the collision event, adding a heart power-up to the game.
     *
     * @param thisObj   The brick object involved in the collision.
     * @param otherObj  The other object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        // Create a heart power-up to the game:
        Renderable heartImage = brickerGameManager.getController().imageReader
                .readImage(Constants.HEART_IMAGE_PATH, true);
        Vector2 heartDimensions = new Vector2(heartSize, heartSize);
        heart = new Heart(Vector2.ZERO, heartDimensions, heartImage);
        heart.setCenter(thisObj.getCenter());
        heart.setVelocity(HEART_VELOCITY);

        brickerGameManager.addGameObject(heart); // Add the heart power-up to the game
    }

    /**
     * Cleans up the collision strategy after the brick is blasted - that is removing the heart if it is
     * collided with the main paddle, or if it is out of window. If collided, add new strike to the game.
     */
    @Override
    public void endStrategy() {
        super.endStrategy();
        // Remove the heart power-up if it collided or moved out of bounds
        if (heart != null) {
            if (heart.isCollided() || heart.getCenter().y() >
                    brickerGameManager.getController().windowController.getWindowDimensions().y()) {
                if (heart.isCollided()) {
                    // Add a strike if the heart was collected
                    brickerGameManager.getStrikesManager().addStrike();
                }
                // Remove the heart power-up from the game
                brickerGameManager.removeGameObject(heart);
                heart = null;
            }
        }
    }

}
