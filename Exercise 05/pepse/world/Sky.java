package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * Represents the sky in the game world.
 * The sky is a static background element that occupies the entire game window.
 * It is typically rendered behind all other game objects.
 */
public class Sky {
    private static final String TAG = "sky";

    private static final Color COLOR = Color.decode("#80C6E5");

    /**
     * Creates a sky game object with the specified dimensions.
     *
     * @param windowDimensions The dimensions of the game window.
     * @return The sky game object.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(TAG);
        return sky;
    }
}
