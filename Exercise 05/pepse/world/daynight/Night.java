package pepse.world.daynight;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * Night class represents the visual representation of night in the game world.
 * It creates a rectangular game object that acts as the night overlay on the screen,
 * with opacity transitioning from transparent to opaque and back, simulating the day-night cycle.
 */
public class Night {


    private static final String TAG = "night";
    private static final float MIDNIGHT_OPACITY = 0.5f;  // final transition value
    private static final float NOON_OPACITY = 0f;  // initial transition value
    private static final Color BASE_COLOR = Color.BLACK;
    private static final float HALF_DAY = 0.5f;


    /**
     * The role of the method is to produce the above rectangle according to windowDimensions,
     * and to cause its opacity to change circularly with a cycle time of cycleLength
     * (the number of seconds that a "day" takes). The method returns the very object it created.
     * @param windowDimensions - dimensions of the window
     * @param cycleLength - number of seconds in a day
     * @return - the night object
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, new
                RectangleRenderable(BASE_COLOR));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(TAG);
        // set the day-night cycle:
        new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                NOON_OPACITY,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength * HALF_DAY,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
        return night;
    }



}
