package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.Color;

/**
 * Represents a fruit in the game world.
 */
public class Fruit {

    private static final String TAG = "fruit";
    private static final Color[] COLORS = {Color.RED, Color.ORANGE, Color.PINK, Color.YELLOW};

    private final Runnable externalEffect;
    private final float cycleLength;
    private int colorIndex = 0;

    /**
     * Constructs a Fruit object with the given external effect and cycle length.
     *
     * @param externalEffect The external effect caused by eating the fruit.
     * @param cycleLength    The length of the cycle for resetting the fruit's dimensions.
     */
    public Fruit(Runnable externalEffect, float cycleLength) {
        this.externalEffect = externalEffect;
        this.cycleLength = cycleLength;
    }

    /**
     * Creates a Block object representing the fruit at the specified position.
     *
     * @param treetopCenter The center position of the tree top.
     * @param i             The row index of the fruit.
     * @param j             The column index of the fruit.
     * @return The Block object representing the fruit.
     */
    public Block create(Vector2 treetopCenter, int i, int j) {
        Block fruit  = new Block(
                treetopCenter.add(Vector2.of(j, i).mult(Block.SIZE)),
                new OvalRenderable(COLORS[colorIndex])
        );
        fruit.setCollisionStrategy(() -> fruitWasEaten(fruit));
        fruit.setTag(TAG);
        return fruit;
    }

    /**
     * Retrieves the next colored image for the fruit.
     *
     * @return The Renderable object representing the next colored image.
     */
    public Renderable getNextColoredImage() {
        return new OvalRenderable(COLORS[colorIndex]);
    }

    /**
     * Increments the color index to get the next color in the array.
     * If the index reaches the end of the array, it wraps around to the beginning.
     */
    public void incrementColorIndex() {
        colorIndex = (colorIndex + 1) % COLORS.length;
    }

    /*
     * Handles the event when a fruit is eaten.
     *
     * @param fruit             The block representing the eaten fruit.
     * @param fruitExternalEffect The external effect caused by eating the fruit.
     * @param cycleLength       The length of the cycle for resetting the fruit's dimensions.
     */
    private void fruitWasEaten(Block fruit) {
        Vector2 dimensions = fruit.getDimensions().getImmutableCopy();
        fruit.setDimensions(Vector2.ZERO);
        externalEffect.run();
        new ScheduledTask(
                fruit,
                cycleLength,
                false,  // reset the fruit's dimensions only once
                () -> fruit.setDimensions(dimensions)
        );
    }

}
