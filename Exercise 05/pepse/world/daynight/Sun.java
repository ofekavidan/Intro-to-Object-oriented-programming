package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * Sun class represents the visual representation of the sun in the game world.
 * It creates a circular game object that acts as the sun in the sky, moving across the horizon
 * based on the day-night cycle. The sun's position transitions smoothly from one side of the
 * screen to the other, simulating the sun's movement from sunrise to sunset.
 */
public class Sun {
    private static final String TAG = "sun";
    private static final Color COLOR = Color.YELLOW;
    private static final float SIZE = 75f;
    private static final float HEIGHT_AT_X0_FACTOR = 2f / 3f;
    // we assume this is the height factor as we were told to do in the forum
    private static final float MIDDLE_FACTOR = 0.5f;
    private static final float INITIAL_ANGLE = 0f;
    private static final float FINAL_ANGLE = 360f;


    /**
     * Creates a GameObject that represents a sun. It sets it in the middle of the sky and add transition of
     * it, so it will rotate around the axis of the horizon's center.
     * @param windowDimensions the dimensions of the window in which the son will be added to.
     * @param cycleLength the length of a day duration.
     * @return the generated sun GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(Vector2.ZERO, Vector2.ONES.mult(SIZE), new OvalRenderable(COLOR));

        float groundHeightAtX0 = windowDimensions.y() * HEIGHT_AT_X0_FACTOR;
        Vector2 initialSunCenter = new Vector2(windowDimensions.x(), groundHeightAtX0).mult(MIDDLE_FACTOR);
        sun.setCenter(initialSunCenter);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(TAG);

        Vector2 cycleCenter = initialSunCenter.multY(1/MIDDLE_FACTOR);
        new Transition<>(
                sun,
                (Float angle) -> sun.setCenter(
                        initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                INITIAL_ANGLE,
                FINAL_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );
        return sun;
    }

}
