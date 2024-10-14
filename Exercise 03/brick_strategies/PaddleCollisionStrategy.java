package bricker.brick_strategies;

import bricker.gameobjects.Paddle;
import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A collision strategy that creates an additional paddle upon collision of the brick with another object.
 */
public class PaddleCollisionStrategy extends BasicCollisionStrategy {

    private static final int MAX_COLLISION_NUMBER = 4; // Maximum number of collisions before removing paddle

    private final Vector2 paddleDimensions; // The dimensions of the paddle
    private final float paddleSpeed; // The speed of the paddle
    private static Paddle paddle; // The paddle object, the same for all bricks

    /**
     * Constructs a PaddleCollisionStrategy with the specified parameters.
     *
     * @param brickerGameManager    The BrickerGameManager instance.
     * @param paddleDimensions      The dimensions of the paddle.
     * @param paddleSpeed           The speed of the paddle.
     */
    public PaddleCollisionStrategy(BrickerGameManager brickerGameManager, Vector2 paddleDimensions,
                                   float paddleSpeed) {
        super(brickerGameManager);
        paddle = null; // Initialize the paddle object as null, so the strategy doesn't apply.
        this.paddleDimensions = paddleDimensions;
        this.paddleSpeed = paddleSpeed;
    }

    /**
     * Handles collision by creating a new paddle object if one does not exist already.
     *
     * @param thisObj   The first colliding object - brick.
     * @param otherObj  The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        if (paddle == null) {  // if the strategy doesn't apply:
            Renderable paddleImage = brickerGameManager.getController()
                    .imageReader.readImage(Constants.PADDLE_IMAGE_PATH, true);
            paddle = new Paddle(
                    Vector2.ZERO, paddleDimensions, paddleImage,
                    brickerGameManager.getController().inputListener,
                    brickerGameManager.getLeftRightBordersLimits(),
                    paddleSpeed
            );
            paddle.setCenter(brickerGameManager.getController().windowController.getWindowDimensions()
                    .mult(Constants.CENTER_FACTOR));
            brickerGameManager.addGameObject(paddle);
        }
    }

    /**
     * Ends the collision strategy by removing the paddle if the maximum number of collisions is reached.
     */
    @Override
    public void endStrategy() {
        super.endStrategy();
        if (paddle != null) {
            if (paddle.getCollisionCounter() >= MAX_COLLISION_NUMBER) {
                brickerGameManager.removeGameObject(paddle);
                paddle = null; // Set the paddle object to null after removal, so the strategy doesn't apply.
            }
        }
    }
}
