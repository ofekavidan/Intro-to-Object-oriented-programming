
/**
 * Represents a player in the game that is smarter than Whatever, meaning that he wins "most of the time".
 * (For example - at least 55% of wins, in a tournament of 10,000 rounds with the default size and winStreak)
 */
public class CleverPlayer implements Player {

    /**
     * Plays the turn as a clever player:
     * Chooses the first non-empty square in the board, from left to right, then from up to down.
     * Then puts the given mark in it.
     *
     * @param board the current board to play on.
     * @param mark  the mark of this player in this turn.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        for (int row = 0; row < board.getSize(); row++)
            for (int col = 0; col < board.getSize(); col++)  // for every cell in board, in rows-order:
                if (board.putMark(mark, row, col)) return;  // if the cell is empty - fill it and return
        // if there is no empty cell - the game was already over
    }
}
