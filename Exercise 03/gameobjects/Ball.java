package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents a ball object in the game.
 */
public class Ball extends GameObject {

    private final Sound collisionSound; // Sound to be played upon collision
    private final Counter collisionCounter; // Counter to track collision occurrences

    /**
     * Constructs a new Ball instance.
     *
     * @param topLeftCorner Position of the ball's top-left corner, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height of the ball in window coordinates.
     * @param renderable    The visual representation of the ball.
     * @param collisionSound The sound to be played upon collision with other objects.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable); // Call the superclass constructor
        this.collisionSound = collisionSound;
        this.collisionCounter = new Counter(); // Initialize the collision counter
    }

    /**
     * Handles actions to be taken when the ball collides with another game object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision Information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // Flip the ball's velocity based on collision normal
        setVelocity(getVelocity().flipped(collision.getNormal()));
        collisionSound.play();
        collisionCounter.increment();
    }

    /**
     * Retrieves the current value of the collision counter.
     *
     * @return The current number of collisions the ball has encountered.
     */
    public int getCollisionCounter() {
        return collisionCounter.value();
    }

    /**
     * Resets the ball's position and velocity.
     *
     * @param velocity The new velocity of the ball.
     * @param center   The new position of the center of the ball.
     */
    public void resetPhaseSpace(Vector2 velocity, Vector2 center) {
        // Set the ball's velocity and center position
        setVelocity(velocity);
        setCenter(center);
    }
}
