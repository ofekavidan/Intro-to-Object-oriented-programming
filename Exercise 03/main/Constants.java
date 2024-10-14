package bricker.main;

/**
 * Constants class containing various constant values used in the game.
 * Includes: General (numbers, empty string), Assets (all paths to the assets), Tags (to objects) constants.
 */
public class Constants {

    /*
     * private constructor to pass the pre-submission.
     */
    private Constants() {}

    // General Constants:

    /**
     * Factor used for centering objects, set to 0.5.
     */
    public static final float CENTER_FACTOR = 0.5f;

    /**
     * Integer value representing 2.
     */
    public static final int TWO = 2;

    /**
     * Square root of 2, approximately 1.414.
     */
    public static final float SQRT_TWO = (float) Math.sqrt(2);

    /**
     * Empty string constant.
     */
    public static final String EMPTY_STRING = "";


    // Assets Constants:

    /**
     * Path to the background image asset.
     */
    public static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    /**
     * Path to the border image asset.
     */
    public static final String BORDER_IMAGE_PATH = EMPTY_STRING;

    /**
     * Path to the ball image asset.
     */
    public static final String BALL_IMAGE_PATH = "assets/ball.png";

    /**
     * Path to the ball collision sound asset.
     */
    public static final String BALL_COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";

    /**
     * Path to the paddle image asset.
     */
    public static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    /**
     * Path to the brick image asset.
     */
    public static final String BRICK_IMAGE_PATH = "assets/brick.png";

    /**
     * Path to the puck image asset.
     */
    public static final String PUCK_IMAGE_PATH = "assets/mockBall.png";

    /**
     * Path to the heart image asset.
     */
    public static final String HEART_IMAGE_PATH = "assets/heart.png";


    // Tags Constants:

    /**
     * Tag for the main ball object.
     */
    public static final String MAIN_BALL_TAG = "main_ball";

    /**
     * Tag for the main paddle object.
     */
    public static final String MAIN_PADDLE_TAG = "main_paddle";

    /**
     * Tag for some blasted bricks.
     */
    public static final String BLASTED_BRICK_TAG = "blasted_brick";

}
