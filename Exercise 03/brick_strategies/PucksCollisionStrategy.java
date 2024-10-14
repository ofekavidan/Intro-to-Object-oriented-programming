package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;


/**
 * Represents a collision strategy for creating additional balls, called "pucks" upon collision with a brick.
 */
public class PucksCollisionStrategy extends BasicCollisionStrategy {

    private static final float DIMENSIONS_FACTOR = 0.75f; // Factor for puck dimensions relative to ball size
    private static final int NUMBER_OF_PUCKS = 2; // Number of pucks to generate upon collision

    private final float ballSize, ballSpeed; // Size and speed of the ball
    private Ball[] pucks; // Array to hold generated puck objects

    /**
     * Constructs a new PucksCollisionStrategy instance.
     *
     * @param brickerGameManager The BrickerGameManager instance managing the game.
     * @param ballSize           Size of the ball.
     * @param ballSpeed          Speed of the ball.
     */
    public PucksCollisionStrategy(BrickerGameManager brickerGameManager, float ballSize, float ballSpeed) {
        super(brickerGameManager); // Call the constructor of the parent class (BasicCollisionStrategy)
        this.ballSize = ballSize; // Assign the provided ball size
        this.ballSpeed = ballSpeed; // Assign the provided ball speed
    }

    /**
     * Handles the behavior upon collision with a brick.
     * Generates additional puck objects and adds them to the game.
     *
     * @param thisObj   The GameObject representing the brick.
     * @param otherObj  The GameObject representing the object collided with the brick.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj); // Call the parent method to handle collision

        Renderable puckImage = brickerGameManager.getController().imageReader
                .readImage(Constants.PUCK_IMAGE_PATH, true);
        Sound puckCollisionSound = brickerGameManager.getController().soundReader
                .readSound(Constants.BALL_COLLISION_SOUND_PATH); // Read the puck collision sound
        Vector2 puckDimensions = (new Vector2(ballSize, ballSize))
                .mult(DIMENSIONS_FACTOR); // Calculate the dimensions of the puck
        pucks = new Ball[NUMBER_OF_PUCKS]; // Initialize the array for puck objects
        for (int i = 0; i < NUMBER_OF_PUCKS; i++) {
            pucks[i] = new Ball(Vector2.ZERO, puckDimensions, puckImage, puckCollisionSound);
        }

        for (var puck : pucks) {
            // Reset the phase space of each puck using a continuous random velocity and the brick's center:
            puck.resetPhaseSpace(calcContinuousRandomVelocity(), thisObj.getCenter());
            brickerGameManager.addGameObject(puck); // Add the puck to the game
        }
    }

    /*
     * Calculates a continuously-random velocity vector for the puck.
     *
     * @return A Vector2 representing the random velocity.
     */
    private Vector2 calcContinuousRandomVelocity() {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI; // Generate a random angle
        float velocityX = (float) Math.cos(angle) * ballSpeed; // Calculate the x component of velocity
        float velocityY = (float) Math.sin(angle) * ballSpeed; // Calculate the y component of velocity
        return new Vector2(velocityX, velocityY);
    }

    /**
     * Ends the strategy by removing any pucks that have exited the window boundaries.
     */
    @Override
    public void endStrategy() {
        super.endStrategy(); // Call the parent method to end the strategy
        if (pucks != null) {
            for (int i = 0; i < NUMBER_OF_PUCKS; i++) {
                if (pucks[i] != null) {
                    if (pucks[i].getCenter().y() >
                            brickerGameManager.getController().windowController.getWindowDimensions().y()) {
                        // if puck is out of the window:
                        brickerGameManager.removeGameObject(pucks[i]);
                        pucks[i] = null;
                    }
                }
            }
        }
    }
}
