package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;

/**
 * Represents a trunk in the game world.
 */
public class Trunk {

    private static final String TAG = "trunk";
    private static final Color BASIC_COLOR = new Color(100, 50, 20);

    /**
     * Creates a trunk block at the specified position with the given height.
     *
     * @param pos    The position of the trunk.
     * @param height The height of the trunk.
     * @return The trunk block.
     */
    public static Block create(Vector2 pos, float height) {
        Block trunk = new Block(Vector2.ZERO, getApproximateColoredImage());
        trunk.setDimensions(Vector2.of(Block.SIZE, height));
        trunk.setTopLeftCorner(pos.subtract(trunk.getDimensions()));
        trunk.setTag(TAG);
        return trunk;
    }

    /**
     * Retrieves an image to render a trunk with a random color.
     *
     * @return The renderer image.
     */
    public static Renderable getApproximateColoredImage() {
        return new RectangleRenderable(ColorSupplier.approximateColor(BASIC_COLOR));
    }

}
