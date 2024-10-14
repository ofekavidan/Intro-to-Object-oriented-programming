package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A game object that represents a block.
 * Blocks are static objects used in the game world for various purposes such as platforms,
 * obstacles, or terrain elements.
 * They typically have a fixed size and can participate in collisions with other game objects.
 */
public class Block extends GameObject {
    /**
     * The size of the block.
     * This constant represents the width and height of the block in pixels.
     */
    public static final int SIZE = 30;

    private static final String TAG = "block";

    private Runnable collisionStrategy = null;

    /**
     * Constructs a Block object with the specified parameters, assuming no collision strategy.
     *
     * @param topLeftCorner The position of the top-left corner of the block.
     * @param renderable    The renderable component for the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TAG);
    }

    /**
     * collision strategy setter.
     * @param collisionStrategy - collision strategy for the block
     */
    public void setCollisionStrategy(Runnable collisionStrategy) {
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Executes the collision strategy when colliding with another object.
     * Assuming the only object that collide with blocks is an avatar.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (collisionStrategy != null) {
            collisionStrategy.run();
        }
    }
}
