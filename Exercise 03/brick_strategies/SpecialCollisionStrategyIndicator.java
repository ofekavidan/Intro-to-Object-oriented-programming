package bricker.brick_strategies;

/**
 * Enum representing different types of special collision strategies that can occur in the game.
 * Each enum constant represents a specific behavior triggered when a collision occurs with a brick.
 */
public enum SpecialCollisionStrategyIndicator {
    /**
     * Indicates that additional balls (pucks) will be spawned upon collision with the brick.
     */
    ADDITIONAL_BALLS,

    /**
     * Indicates that an additional paddle will be spawned upon collision with the brick.
     */
    ADDITIONAL_PADDLE,

    /**
     * Indicates that the camera behavior will be changed upon collision with the brick.
     */
    CHANGE_CAMERA,

    /**
     * Indicates that a strike (or some form of power-up) will be added upon collision with the brick.
     */
    ADD_STRIKE,

    /**
     * Indicates that a double behavior will be triggered upon collision with the brick,
     * which may involve multiple effects or behaviors.
     */
    DOUBLE_BEHAVIOR
}
