package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;

/**
 * Represents a leaf in the game world.
 */
public class Leaf {

    private static final String TAG = "leaf";
    private static final Color BASIC_COLOR = new Color(50, 200, 30);
    private static final float LEAF_ROTATION_AMPLITUDE = 10f;
    private static final float LEAF_SIZE_NOISE_FACTOR = 0.9f;
    private static final float LEAF_CYCLE_LENGTH = 2f;

    /**
     * Creates a leaf at the specified position with animation transitions.
     *
     * @param treetopCenter   The center position of the treetop.
     * @param i               The x-coordinate offset of the leaf.
     * @param j               The y-coordinate offset of the leaf.
     * @param waitTimeFactor  The factor to adjust the wait time for leaf animation.
     * @return                The created leaf.
     */
    public static Block create(Vector2 treetopCenter, int i, int j, float waitTimeFactor) {
        Block leaf = new Block(
                treetopCenter.add(Vector2.of(j, i).mult(Block.SIZE)),
                getApproximateColoredImage()
        );
        new ScheduledTask(
                leaf,
                waitTimeFactor * LEAF_CYCLE_LENGTH,
                false,  // init the Transition only once
                () -> initializeTransition(leaf)
        );
        leaf.setTag(TAG);
        return leaf;
    }

    /*
     * Retrieves an image to render a leaf with a random color.
     *
     * @return The renderer image.
     */
    private static Renderable getApproximateColoredImage() {
        return new RectangleRenderable(ColorSupplier.approximateColor(BASIC_COLOR));
    }

    /*
     * Initializes transitions for leaf GameObjects.
     * This method sets up animations for leaf rotation and dimensions.
     * The leaves swings are in the form of a pendulum, which is roughly a harmonic oscillator, so we use the
     * cubic interpolator
     *
     * @param leaf The leaf GameObject for which transitions are initialized.
     */
    private static void initializeTransition(Block leaf) {
        new Transition<>(
                leaf,
                leaf.renderer()::setRenderableAngle,
                -LEAF_ROTATION_AMPLITUDE,
                LEAF_ROTATION_AMPLITUDE,
                Transition.CUBIC_INTERPOLATOR_FLOAT,  // pendulum is roughly a harmonic oscillator
                LEAF_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
        new Transition<>(
                leaf,
                leaf::setDimensions,
                leaf.getDimensions().multY(LEAF_SIZE_NOISE_FACTOR),
                leaf.getDimensions().multX(LEAF_SIZE_NOISE_FACTOR),
                Transition.CUBIC_INTERPOLATOR_VECTOR,  // pendulum is roughly a harmonic oscillator
                LEAF_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

}
