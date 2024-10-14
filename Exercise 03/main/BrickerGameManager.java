package bricker.main;

import bricker.brick_strategies.CollisionStrategyFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The main manager of the Bricker game. An instance of this class deals with all the necessary components in
 * order to run and play the Bricker game. Contains also the main method.
 */
public class BrickerGameManager extends GameManager {

    /* EndGame Messages: */
    private static final String LOSE_MSG = "You lose! Play again?";
    private static final String WIN_MSG = "You win! Play again?";

    /* Window Constants: */
    private static final String WINDOW_TITLE = "Bricker";
    private static final int WINDOW_WIDTH = 700, WINDOW_HEIGHT = 500;

    /* Border Constants: */
    private static final int BORDER_WIDTH = 10;

    /* Ball Constants: */
    private static final float BALL_SIZE = 20f;
    private static final float BALL_SPEED = 200f;

    /* Paddle Constants: */
    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);
    private static final float PADDLE_SPEED = 2 * BALL_SPEED;

    /* Brick Constants: */
    private static final int BRICK_HEIGHT = 15;
    private static final int SPACE_BETWEEN_BRICKS = 1;
    private static final int NUMBER_OF_ROWS = 7;
    private static final int NUMBER_OF_BRICKS_IN_ROW = 8;

    /* Strikes Constants: */
    private static final int HEART_SIZE = 25;
    private static final int SPACE_BETWEEN_STRIKES_OBJECTS = 5;

    /* Control fields: */
    private Controller controller = null;
    private Vector2 windowDimensions = null;

    /* Bricks-related fields: */
    private final Vector2 bricksTableDimensions;
    private final Counter bricksCounter = new Counter();

    /* Objects fields: */
    private Ball ball = null;
    private Paddle paddle = null;
    private GameObject[] borders = null;
    private GameObject background = null;
    private Brick[] bricks = null;
    private StrikesManager strikes = null;

    /**
     * Constructor for BrickerGameManager.
     *
     * @param bricksTableDimensions Vector2 of the dimensions of the bricks table.
     */
    public BrickerGameManager(Vector2 bricksTableDimensions) {
        super(WINDOW_TITLE, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.bricksTableDimensions = bricksTableDimensions;
    }

    /**
     * Removes a game object from the game.
     *
     * @param gameObject The game object to be removed.
     * @param layerId    The layer ID from which the game object should be removed.
     * @return True if the game object was successfully removed, false otherwise.
     */
    public boolean removeGameObject(GameObject gameObject, int layerId) {
        return this.gameObjects().removeGameObject(gameObject, layerId);
    }

    /**
     * Removes a game object from the default layer of the game.
     *
     * @param gameObject The game object to be removed.
     * @return True if the game object was successfully removed, false otherwise.
     */
    public boolean removeGameObject(GameObject gameObject) {
        return this.gameObjects().removeGameObject(gameObject);
    }
    /**
     * Adds a game object to the game with the specified layer ID.
     *
     * @param gameObject The game object to add.
     * @param layerId    The layer ID to add the game object to.
     */
    public void addGameObject(GameObject gameObject, int layerId) {
        this.gameObjects().addGameObject(gameObject, layerId);
    }

    /**
     * Adds a game object to the game in the default layer of the game.
     *
     * @param gameObject The game object to add.
     */
    public void addGameObject(GameObject gameObject) {
        this.gameObjects().addGameObject(gameObject);
    }

    /**
     * Decreases the counter for the number of bricks.
     */
    public void decreaseBricksCounter() {
        bricksCounter.decrement();
    }

    /**
     * Retrieves the controller associated with the game.
     *
     * @return The controller of the game.
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Retrieves the strikes manager associated with the game.
     *
     * @return The strikes manager of the game.
     */
    public StrikesManager getStrikesManager() {
        return strikes;
    }

    /**
     * Retrieves the main ball object in the game.
     *
     * @return The main ball object.
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Retrieves the upper border width of the game's border.
     *
     * @return The upper border width of the border.
     */
    public float getUpperBorderLimit() {
        return BORDER_WIDTH;
    }

    /**
     * Retrieves an array containing the left and right border limits of the game.
     *
     * @return An array containing the left and right border limits.
     */
    public float[] getLeftRightBordersLimits() {
        return new float[]{BORDER_WIDTH, windowDimensions.x() - BORDER_WIDTH};
    }

    /*
     * Initializes the background of the game.
     * This method creates a game object representing the background image and adds it to the game.
     */
    private void initializeBackground() {
        Renderable backgroundImage = controller.imageReader.readImage(Constants.BACKGROUND_IMAGE_PATH,
                false);
        background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /*
     * Initializes the borders of the game.
     * This method creates game objects representing the left, right, and upper borders and adds them to the
     * game.
     */
    private void initializeBorders() {
        Renderable borderImage = (Constants.BORDER_IMAGE_PATH == Constants.EMPTY_STRING) ? null :
                controller.imageReader.readImage(Constants.BORDER_IMAGE_PATH, false);

        GameObject leftBorder = new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()), borderImage);
        GameObject rightBorder = new GameObject(new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), borderImage);
        GameObject upperBorder = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH), borderImage);

        borders = new GameObject[]{leftBorder, rightBorder, upperBorder};
        for (GameObject border : borders) {
            gameObjects().addGameObject(border, Layer.STATIC_OBJECTS);
        }
    }


    /*
     * Calculates a random velocity component for the ball.
     * This method generates a random boolean value to determine the direction of the velocity component
     * and returns either a positive or negative value based on the random boolean.
     *
     * @return A random velocity component for the ball.
     */
    private float calcRandomBallVelocityComponent() {
        Random rand = new Random();
        return rand.nextBoolean() ?
                BALL_SPEED / Constants.SQRT_TWO :
                -BALL_SPEED / Constants.SQRT_TWO;
    }

    /*
     * Resets the phase space of the ball.
     * This method recalculates the velocity components of the ball and sets its position to the center of
     * the window.
     */
    private void resetBallPhaseSpace() {
        ball.resetPhaseSpace(
                new Vector2(calcRandomBallVelocityComponent(), calcRandomBallVelocityComponent()),
                windowDimensions.mult(Constants.CENTER_FACTOR)
        );
    }

    /*
     * Initializes the ball object.
     * This method creates a new Ball object with the specified parameters,
     * sets its tag, resets its phase space, and adds it to the game objects.
     */
    private void initializeBall() {
        Renderable ballImage = controller.imageReader.readImage(Constants.BALL_IMAGE_PATH,
                true);
        Sound ballCollisionSound = controller.soundReader.readSound(Constants.BALL_COLLISION_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, ballCollisionSound);
        ball.setTag(Constants.MAIN_BALL_TAG);
        resetBallPhaseSpace();

        gameObjects().addGameObject(ball);
    }

    /*
     * Initializes the paddle object.
     * This method creates a new Paddle object with the specified parameters,
     * sets its center position, tag, and adds it to the game objects.
     */
    private void initializePaddle() {
        Renderable paddleImage = controller.imageReader.readImage(Constants.PADDLE_IMAGE_PATH,
                true);
        paddle = new Paddle(
                Vector2.ZERO, PADDLE_DIMENSIONS,
                paddleImage, controller.inputListener,
                getLeftRightBordersLimits(),
                PADDLE_SPEED
        );
        paddle.setCenter(new Vector2(windowDimensions.x() * Constants.CENTER_FACTOR,
                windowDimensions.y() - HEART_SIZE - SPACE_BETWEEN_STRIKES_OBJECTS - PADDLE_DIMENSIONS.y()));
        paddle.setTag(Constants.MAIN_PADDLE_TAG);

        gameObjects().addGameObject(paddle);
    }

    private Brick[] calcBricksArray(int numberOfRows, int numberOfBricksInRow, float[] leftRightBorders,
                                    Vector2 brickDimensions, Renderable brickImage,
                                    CollisionStrategyFactory collisionStrategyFactory) {
        Brick[] bricksArray = new Brick[numberOfRows * numberOfBricksInRow];
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfBricksInRow; col++) {
                Vector2 brickTopLeftCorner = new Vector2(
                        leftRightBorders[0] + (col + 1) * SPACE_BETWEEN_BRICKS + col * brickDimensions.x(),
                        getUpperBorderLimit() + (row + 1) * SPACE_BETWEEN_BRICKS + row * brickDimensions.y()
                );
                bricksArray[(row * numberOfBricksInRow) + col] = new Brick(
                        brickTopLeftCorner, brickDimensions, brickImage,
                        collisionStrategyFactory.selectStrategy(this, false, 0)
                );
            }
        }
        return bricksArray;
    }

    /*
     * Initializes the bricks' layout.
     * This method calculates the dimensions and positions of the bricks based on the provided table
     * dimensions,
     * creates Brick objects accordingly, adds collision strategies, and adds them to the game objects.
     */
    private void initializeBricks() {
        CollisionStrategyFactory collisionStrategyFactory = new CollisionStrategyFactory(BALL_SIZE,
                BALL_SPEED, PADDLE_DIMENSIONS, PADDLE_SPEED, HEART_SIZE);
        Renderable brickImage = controller.imageReader.readImage(Constants.BRICK_IMAGE_PATH,
                false);
        int numberOfBricksInRow = (int) bricksTableDimensions.x();
        int numberOfRows = (int) bricksTableDimensions.y();
        float[] leftRightBorders = getLeftRightBordersLimits();
        Vector2 brickDimensions = new Vector2(
                (leftRightBorders[1] - leftRightBorders[0] -
                        (numberOfBricksInRow + 1) * SPACE_BETWEEN_BRICKS) / numberOfBricksInRow,
                BRICK_HEIGHT
        );
        bricks = calcBricksArray(numberOfRows, numberOfBricksInRow, leftRightBorders, brickDimensions,
                brickImage, collisionStrategyFactory);

        bricksCounter.reset();
        bricksCounter.increaseBy(numberOfBricksInRow * numberOfRows);

        for (GameObject brick : bricks) {
            gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
        }
    }

    /*
     * Initializes the strikes' manager.
     * This method creates a new StrikesManager instance with the specified parameters and assigns it to
     * the strikes field.
     */
    private void initializeStrikes() {
        strikes = new StrikesManager(this, HEART_SIZE, SPACE_BETWEEN_STRIKES_OBJECTS);
    }


    /**
     * Initializes the game components and sets up the game environment.
     * This method sets up the controller, window dimensions, background, borders, ball, paddle,
     * bricks, and strikes.
     *
     * @param imageReader       The image reader used to load images.
     * @param soundReader       The sound reader used to load sounds.
     * @param inputListener     The input listener used to handle user input.
     * @param windowController  The window controller used to manage the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        controller = new Controller(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();

        initializeBackground();
        initializeBorders();
        initializeBall();
        initializePaddle();
        initializeBricks();
        initializeStrikes();
    }

    /*
     * Ends the strategies of blasted bricks.
     * This method iterates through the bricks and ends the collision strategies of bricks that was blasted.
     */
    private void endStrategiesOfBlastedBricks() {
        for (var brick : bricks) {
            if (brick.getTag().equals(Constants.BLASTED_BRICK_TAG)) {  // if the strategy of the brick started
                brick.getCollisionStrategy().endStrategy();
            }
        }
    }


    /*
     * Ends the game with condition based on the provided message.
     * If the player chooses to play again, the game is reset; otherwise, the game window is closed.
     *
     * @param msg The message displayed to the player.
     */
    private void endCondition(String msg) {
        if (controller.windowController.openYesNoDialog(msg)) {
            controller.windowController.resetGame();
        } else {
            controller.windowController.closeWindow();
        }
    }

    /*
     * Checks for strikes (loss conditions) and handles them accordingly.
     * If the ball goes beyond the window's bottom boundary, a strike is removed.
     * If the number of strikes becomes less than the minimum required strikes, the game ends with a loss.
     * If all bricks are destroyed, the game ends with a win.
     */
    private void checkForStrikes() {
        if (ball.getCenter().y() > windowDimensions.y()) {
            strikes.removeStrike();
            if (strikes.getNumberOfStrikes() < strikes.getMinNumberOfStrikes()) {
                endCondition(LOSE_MSG);
            } else {
                resetBallPhaseSpace();
            }
        }
        if (bricksCounter.value() <= 0) {
            endCondition(WIN_MSG);
        }
    }

    /**
     * Updates the game state for each frame.
     * It calls the superclass's update method, then ends strategies of blasted bricks, checks for strikes
     * updates, and handles the player's input to end the game if the 'W' key is pressed.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        endStrategiesOfBlastedBricks();
        checkForStrikes();
        if (controller.inputListener.isKeyPressed(KeyEvent.VK_W)) {
            endCondition(WIN_MSG);
        }
    }

    /**
     * Main method to start the game.
     * It initializes the game manager with the provided dimensions or defaults,
     * then starts the game loop by calling the run method.
     *
     * @param args Command-line arguments for specifying the dimensions of the bricks table.
     */
    public static void main(String[] args) {
        Vector2 bricksTableDimensions = new Vector2(NUMBER_OF_BRICKS_IN_ROW, NUMBER_OF_ROWS);
        if (args.length == Constants.TWO) {
            bricksTableDimensions = new Vector2(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }
        BrickerGameManager brickerGameManager = new BrickerGameManager(bricksTableDimensions);
        brickerGameManager.run();
    }

}
