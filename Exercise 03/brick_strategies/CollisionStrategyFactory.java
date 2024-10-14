package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A factory class responsible for creating collision strategies based on game parameters and conditions.
 */
public class CollisionStrategyFactory {

    private static final int NUMBER_OF_SPECIAL_STRATEGIES = 5;
    private static final int MAX_NUMBER_OF_STRATEGIES_IN_BRICK = 3;

    private final float ballSize, ballSpeed;
    private final Vector2 paddleDimensions;
    private final float paddleSpeed;
    private final float heartSize;

    private final Random rand = new Random();

    /**
     * Constructs a CollisionStrategyFactory with specified parameters.
     *
     * @param ballSize          The size of the ball.
     * @param ballSpeed         The speed of the ball.
     * @param paddleDimensions  The dimensions of the paddle.
     * @param paddleSpeed       The speed of the paddle.
     * @param heartSize         The size of the heart.
     */
    public CollisionStrategyFactory(float ballSize, float ballSpeed,
                                    Vector2 paddleDimensions, float paddleSpeed,
                                    float heartSize) {
        this.ballSize = ballSize;
        this.ballSpeed = ballSpeed;
        this.paddleDimensions = paddleDimensions;
        this.paddleSpeed = paddleSpeed;
        this.heartSize = heartSize;
    }

    /**
     * Selects and returns a random collision strategy. The strategy is chosen randomly from the exists
     * implementations, according to the probabilities supplied in the ex. This method is recursive in order
     * to create the double behavior (in the last case).
     *
     * @param brickerGameManager        The BrickerGameManager instance.
     * @param selectForDouble          A boolean indicating whether to select a strategy for double collision.
     * @param numberOfStrategiesInBrick The number of strategies currently in the brick.
     * @return                          The selected collision strategy.
     */
    public CollisionStrategy selectStrategy(BrickerGameManager brickerGameManager,
                                            boolean selectForDouble, int numberOfStrategiesInBrick) {
        CollisionStrategy collisionStrategy = null;
        numberOfStrategiesInBrick++;

        if (rand.nextBoolean() && !selectForDouble) {
            // Basic strategy - with probability 1/2,
            // cannot be returned for DoubleCollisionStrategy's array:
            collisionStrategy = new BasicCollisionStrategy(brickerGameManager);
        } else {
            // Special strategy - each with probability 1/(2*numberOfSpecialStrategies),
            // assuming the last strategy in the enum SpecialCollisionStrategyIndicator is DoubleCS:
            int numberOfSpecialStrategies = NUMBER_OF_SPECIAL_STRATEGIES - 1;
            if (numberOfStrategiesInBrick < MAX_NUMBER_OF_STRATEGIES_IN_BRICK) {
                // If it is legal (in this depth of the recursion) to draw DoubleCollisionStrategy:
                numberOfSpecialStrategies++;
            }
            switch (SpecialCollisionStrategyIndicator.values()[rand.nextInt(numberOfSpecialStrategies)]) {
            case ADDITIONAL_BALLS:
                collisionStrategy = new PucksCollisionStrategy(brickerGameManager, ballSize, ballSpeed);
                break;
            case ADDITIONAL_PADDLE:
                collisionStrategy = new PaddleCollisionStrategy(brickerGameManager, paddleDimensions,
                        paddleSpeed);
                break;
            case CHANGE_CAMERA:
                collisionStrategy = new CameraCollisionStrategy(brickerGameManager);
                break;
            case ADD_STRIKE:
                collisionStrategy = new StrikeCollisionStrategy(brickerGameManager, heartSize);
                break;
            case DOUBLE_BEHAVIOR:
                CollisionStrategy[] collisionStrategies = new CollisionStrategy[]{
                        selectStrategy(brickerGameManager, true, numberOfStrategiesInBrick++),
                        selectStrategy(brickerGameManager, true, numberOfStrategiesInBrick)
                };
                collisionStrategy = new DoubleCollisionStrategy(brickerGameManager, collisionStrategies);
                break;
            }
        }
        return collisionStrategy;
    }
}
