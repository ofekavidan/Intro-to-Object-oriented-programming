package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * Represents a brick object in the Bricker game.
 * Inherits from GameObject class and implements collision handling functionality.
 */
public class Brick extends GameObject {

    private final CollisionStrategy collisionStrategy; // Strategy to handle collisions with this brick

    /**
     * Constructs a new Brick object.
     *
     * @param topLeftCorner   Position of the top-left corner of the brick, in window coordinates (pixels).
     *                        Note that (0,0) is the top-left corner of the window.
     * @param dimensions      Width and height of the brick in window coordinates.
     * @param renderable      The renderable representing the brick.
     * @param collisionStrategy The collision strategy to be used when this brick collides with other objects.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Called when a collision occurs with this brick.
     * Delegates the collision handling to the assigned collision strategy.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Details of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        collisionStrategy.onCollision(this, other);
    }

    /**
     * Retrieves the collision strategy assigned to this brick.
     *
     * @return The collision strategy.
     */
    public CollisionStrategy getCollisionStrategy() {
        return collisionStrategy;
    }
}
