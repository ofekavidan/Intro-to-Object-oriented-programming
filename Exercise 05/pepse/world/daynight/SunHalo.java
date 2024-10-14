package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * SunHalo class represents the visual representation of the halo around the sun in the game world.
 * It creates a circular game object that acts as the halo around the sun, providing a visual effect
 * to enhance the appearance of the sun. The halo follows the movement of the sun.
 */
public class SunHalo {

    private static final String TAG = "halo";
    private static final Color COLOR = new Color(255, 255, 0, 20);
    private static final float RADIUS_FACTOR = 2f;

    /**
     * Creates a halo around the sun.
     *
     * @param sun The sun GameObject around which the halo will be created.
     * @return The GameObject representing the sun's halo.
     */
    public static GameObject create(GameObject sun) {
        GameObject halo = new GameObject(Vector2.ZERO, sun.getDimensions().mult(RADIUS_FACTOR),
                new OvalRenderable(COLOR));
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag(TAG);
        halo.addComponent((float deltaTime) -> halo.setCenter(sun.getCenter()));
        return halo;
    }

}
