
package bricker.gameobjects;

import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a heart object in the Bricker game.
 * Inherits from GameObject class and defines collision handling functionality specific to hearts.
 */
public class Heart extends GameObject {

    private boolean collided = false; // Flag to track if the heart has collided with another object

    /**
     * Constructs a new Heart object.
     *
     * @param topLeftCorner Position of the top-left corner of the heart, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height of the heart in window coordinates.
     * @param renderable    The renderable representing the heart.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable); // Call the constructor of the parent class (GameObject)
    }

    /**
     * Called when a collision occurs with this heart.
     * Marks the heart as collided.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Details of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision); // Call the parent method to handle collision
        collided = true; // Mark the heart as collided
    }

    /**
     * Determines whether this heart should collide with another GameObject.
     * Hearts should only collide with the main paddle.
     *
     * @param other The other GameObject to check collision with.
     * @return True if the heart should collide with the other GameObject, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return (super.shouldCollideWith(other) && other.getTag().equals(Constants.MAIN_PADDLE_TAG));
        // Allow collision only with the main paddle
    }

    /**
     * Checks if the heart has collided with another object.
     *
     * @return True if the heart has collided, false otherwise.
     */
    public boolean isCollided() {
        return collided; // Return the collided flag
    }
}
