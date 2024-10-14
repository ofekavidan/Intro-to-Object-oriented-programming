package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

/**
 * A collision strategy for managing the camera behavior in the game.
 * This strategy is responsible for setting up the camera when the main ball collides with certain objects,
 * and ending the camera's behavior after a certain number of collisions.
 */
public class CameraCollisionStrategy extends BasicCollisionStrategy {

     // The maximum number of collisions allowed before the camera behavior is ended.
    private static final int MAX_BALL_COLLISION_COUNTER = 4;

    // The factor by which to expand the camera frame width.
    private static final float FRAME_WIDTH_FACTOR = 1.2f;

    // Counter to keep track of the number of ball collisions since the camera was set.
    private static int ballCollisionsSinceCameraWasSet;

    /**
     * Constructs a new CameraCollisionStrategy.
     *
     * @param brickerGameManager The BrickerGameManager instance associated with this strategy.
     */
    public CameraCollisionStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    /**
     * Handles the collision event between game objects.
     * If the other object is the main ball and no camera is currently set, sets up a camera around the ball.
     *
     * @param thisObj   The game object on which this collision strategy is applied.
     * @param otherObj  The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        if (otherObj.getTag().equals(Constants.MAIN_BALL_TAG) && brickerGameManager.camera() == null) {
            brickerGameManager.setCamera(
                    new Camera(
                            brickerGameManager.getBall(),
                            Vector2.ZERO,
                            brickerGameManager.getController().windowController.getWindowDimensions()
                                    .mult(FRAME_WIDTH_FACTOR),
                            brickerGameManager.getController().windowController.getWindowDimensions()
                    )
            );
            ballCollisionsSinceCameraWasSet = brickerGameManager.getBall().getCollisionCounter();
        }
    }

    /**
     * Ends the camera behavior if the maximum number of ball collisions has been reached.
     */
    @Override
    public void endStrategy() {
        super.endStrategy();
        if (brickerGameManager.camera() != null) {
            if ((brickerGameManager.getBall().getCollisionCounter() - ballCollisionsSinceCameraWasSet)
                    > MAX_BALL_COLLISION_COUNTER) {
                brickerGameManager.setCamera(null);
            }
        }
    }
}
