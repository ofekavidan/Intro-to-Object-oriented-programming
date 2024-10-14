package pepse.world;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.Observer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Avatar class represents the player's character in the game world.
 * It handles the avatar's movement, animation, and energy management.
 */
public class Avatar extends GameObject {
    private static final String TAG = "avatar";
    private static final Vector2 DIMENSIONS = Vector2.of(60, 85);
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 150;
    private static final float MAX_ENERGY = 100f;
    private static final float MIN_ENERGY = 0f;
    private static final float RUN_ENERGY = -0.5f;
    private static final float JUMP_ENERGY = -10f;
    private static final float STAND_ENERGY = 1f;
    private static final String PATH_FORMAT = "assets/%s_%d.png";
    private static final String IDLE = "idle";
    private static final String JUMP = "jump";
    private static final String RUN = "run";
    private static final int MAX_IDLE_PATH_INDEX = 3;
    private static final int MAX_JUMP_PATH_INDEX = 3;
    private static final int MAX_RUN_PATH_INDEX = 5;
    private static final float TIME_BETWEEN_CLIPS = 0.2f;

    private final UserInputListener inputListener;
    private final AnimationRenderable idleRenderer;
    private final AnimationRenderable runRenderer;
    private final AnimationRenderable jumpRenderer;
    private float energy;
    private final Collection<Observer> observers;

    /**
     * Constructs an Avatar object.
     *
     * @param pos          The initial position of the avatar.
     * @param inputListener The input listener to handle user input.
     * @param imageReader  The image reader to load avatar animations.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos.subtract(DIMENSIONS), DIMENSIONS, null);
        idleRenderer = new AnimationRenderable(getPaths(MAX_IDLE_PATH_INDEX, IDLE),
                imageReader, true, TIME_BETWEEN_CLIPS);
        runRenderer = new AnimationRenderable(getPaths(MAX_RUN_PATH_INDEX, RUN),
                imageReader, true, TIME_BETWEEN_CLIPS);
        jumpRenderer = new AnimationRenderable(getPaths(MAX_JUMP_PATH_INDEX, JUMP),
                imageReader, true, TIME_BETWEEN_CLIPS);
        renderer().setRenderable(idleRenderer);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        setTag(TAG);
        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
        this.observers = new HashSet<>();
    }

    /*
     * Retrieves the file paths for the avatar animations.
     *
     * @param numberOfPaths The number of animation paths.
     * @param state         The state of the avatar (idle, jump, or run).
     * @return An array of file paths for the avatar animations.
     */
    private String[] getPaths(int numberOfPaths, String state) {
        String[] paths = new String[numberOfPaths];
        for (int i = 0; i < numberOfPaths; i++) {
            paths[i] = String.format(PATH_FORMAT, state, i);
        }
        return paths;
    }

    /**
     * Updates the avatar's state and behavior based on input and energy levels.
     * This method handles jump, run, and idle states of the avatar.
     *
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        boolean isWithinJump = getVelocity().y() != 0;  // is the avatar within a jump
        boolean isJumping = handleJumpState();  // check jump state
        boolean isRunning = handleRunState();  // check run state
        // check idle state:
        if (!isWithinJump && !isJumping && !isRunning) {  // if is in idle state
            renderer().setRenderable(idleRenderer);
            energy = Math.min(MAX_ENERGY, energy + STAND_ENERGY);  // assuming (STAND_ENERGY >= 0)
        }
    }

    /*
     * Handles the logic for the running state of the character.
     *
     * @return True if the character is currently running, false otherwise.
     */
    private boolean handleRunState() {
        boolean isRunning = false;
        float xVelocity = calcInputXVelocity();
        if (xVelocity != 0 && isValidEnergy(energy + RUN_ENERGY)) {
            // if it should move horizontally, and is able to do so:
            transform().setVelocityX(xVelocity);
            isRunning = true;  // is in run state
            renderer().setRenderable(runRenderer);
            renderer().setIsFlippedHorizontally(xVelocity < 0);
            energy += RUN_ENERGY;
        } else {  // shouldn't move horizontally this update or isn't able to do so
            transform().setVelocityX(0);
        }
        return isRunning;
    }

    /*
     * Handles the logic for the jumping state of the character.
     *
     * @return True if the character is currently jumping, false otherwise.
     */
    private boolean handleJumpState() {
        boolean isJumping = false;
        float yVelocity = calcInputYVelocity();
        if (yVelocity != 0 && getVelocity().y() == 0 && isValidEnergy(energy + JUMP_ENERGY)) {
            // if it should move diagonally this update and is able to do so:
            transform().setVelocityY(yVelocity);
            isJumping = true;  // is in jump state
            renderer().setRenderable(jumpRenderer);
            energy += JUMP_ENERGY;
            notifyObservers();  // tells the observers to update themselves following the jump
        }
        return isJumping;
    }


    /*
     * Calculates the vertical velocity based on user input.
     *
     * @return The vertical velocity.
     */
    private float calcInputYVelocity() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            return VELOCITY_Y;
        }
        return 0;
    }

    /*
     * Calculates the horizontal velocity based on user input.
     *
     * @return The horizontal velocity.
     */
    private float calcInputXVelocity() {
        float xVelocity = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVelocity -= VELOCITY_X;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVelocity += VELOCITY_X;
        }
        return xVelocity;
    }

    /*
     * Checks if the given energy level is within valid bounds.
     *
     * @param energy The energy level to check.
     * @return True if the energy level is valid, false otherwise.
     */
    private static boolean isValidEnergy(float energy) {
        return (MIN_ENERGY <= energy) && (energy <= MAX_ENERGY);
    }


    /**
     * Adds energy to the avatar's energy.
     *
     * @param dEnergy The amount of energy to add.
     */
    public void addEnergy(float dEnergy) {
        this.energy = Math.min(MAX_ENERGY, energy + dEnergy);  // assuming (dEnergy >= 0)
    }

    /**
     * Retrieves the current energy level of the avatar.
     *
     * @return The current energy level.
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * Registers an observer to receive updates.
     *
     * @param observer The observer to register.
     * @return True if the observer was successfully registered, false otherwise.
     */
    public boolean registerObserver(Observer observer) {
        return observers.add(observer);
    }

    /**
     * Unregisters an observer.
     *
     * @param observer The observer to unregister.
     * @return True if the observer was successfully unregistered, false otherwise.
     */
    public boolean unregisterObserver(Observer observer) {
        return observers.remove(observer);
    }

    /*
     * Notifies all registered observers to update themselves.
     */
    private void notifyObservers() {
        for (var observer : observers) {
            observer.update();
        }
    }
}
