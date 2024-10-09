
/**
 * Represents a renderer which does not display the board (or anything else) on the screen at all.
 * Practically - does nothing.
 */
public class VoidRenderer implements Renderer {

    /**
     * Render the board as a void renderer:
     * Does nothing, meaning - do not display anything on the screen after every turn.
     *
     * @param board the current board to display.
     */
    @Override
    public void renderBoard(Board board) {
    }
}
