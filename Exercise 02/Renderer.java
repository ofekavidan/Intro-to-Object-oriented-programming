
/**
 * Represents the way of each tournament to display the board status after every turn.
 */
public interface Renderer {
    /**
     * Displays the given board on the screen in a certain way.
     * @param board the current board to display.
     */
    void renderBoard(Board board);
}
