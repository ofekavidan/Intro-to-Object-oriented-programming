
/**
 * Represents the required elements of a logic / strategy for playing a single turn in the game.
 */
public interface Player {
    /**
     * Given the board's status, the player choose where to put the given mark, in order to play his turn.
     * @param board the current board to play on.
     * @param mark the mark of this player in this turn.
     */
    void playTurn(Board board, Mark mark);
}
