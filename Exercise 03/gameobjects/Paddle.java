
package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents a paddle object in the Bricker game.
 * Inherits from GameObject class and handles user input for paddle movement.
 */
public class Paddle extends GameObject {

    private final float speed; // Speed of the paddle movement
    private final UserInputListener inputListener; // Listener for user input
    private final float leftBorder, rightBorder; // Left and right borders of the paddle movement area
    private final Vector2 dimensions; // Dimensions of the paddle
    private final Counter collisionCounter = new Counter(); // Counter for collision occurrences

    /**
     * Constructs a new Paddle object.
     *
     * @param topLeftCorner   Position of the top-left corner of the paddle, in window coordinates (pixels).
     *                        Note that (0,0) is the top-left corner of the window.
     * @param dimensions      Width and height of the paddle in window coordinates.
     * @param renderable      The renderable representing the paddle. Can be null, in which case
     *                        the paddle will not be rendered.
     * @param inputListener   Listener for user input events.
     * @param borders         An array containing the left and right borders of the paddle movement area.
     * @param speed           Speed of the paddle movement.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, float[] borders, float speed) {
        super(topLeftCorner, dimensions, renderable); // Call the constructor of the parent class (GameObject)
        this.speed = speed; // Assign the provided speed
        this.dimensions = dimensions; // Assign the provided dimensions
        this.inputListener = inputListener; // Assign the provided input listener
        this.leftBorder = borders[0]; // Assign the left border
        this.rightBorder = borders[1]; // Assign the right border
    }

    /**
     * Updates the paddle's position based on user input and ensures it stays within the specified borders.
     *
     * @param deltaTime Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Call the parent update method
        Vector2 movementDir = Vector2.ZERO; // Initialize the movement direction vector
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) { // Check if the left arrow key is pressed
            movementDir = movementDir.add(Vector2.LEFT); // Move the paddle left
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) { // Check if the right arrow key is pressed
            movementDir = movementDir.add(Vector2.RIGHT); // Move the paddle right
        }
        setVelocity(movementDir.mult(speed)); // Set the velocity based on the movement direction and speed

        Vector2 topLeftCorner = getTopLeftCorner(); // Get the current top-left corner of the paddle
        if (topLeftCorner.x() <= leftBorder) { // If the paddle reaches or exceeds the left border
            setTopLeftCorner(new Vector2(leftBorder, topLeftCorner.y()));
        }
        if (rightBorder <= (topLeftCorner.x() + dimensions.x())) { // If the paddle reaches right border
            setTopLeftCorner(new Vector2(rightBorder - dimensions.x(), topLeftCorner.y()));
        }
    }

    /**
     * Called when a collision occurs with this paddle.
     * Increments the collision counter.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Details of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision); // Call the parent method to handle collision
        collisionCounter.increment(); // Increment the collision counter
    }

    /**
     * Retrieves the current value of the collision counter.
     *
     * @return The number of collisions that have occurred with this paddle.
     */
    public int getCollisionCounter() {
        return collisionCounter.value(); // Return the current value of the collision counter
    }
}
