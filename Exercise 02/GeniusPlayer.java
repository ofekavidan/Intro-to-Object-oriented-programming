
/**
 * Represents a player in the game that is smarter than Clever, meaning that he wins "most of the time".
 * (For example - at least 55% of wins, in a tournament of 10,000 rounds with the default size and winStreak,
 * against both Clever and Whatever)
 */
public class GeniusPlayer implements Player {

    /**
     * Plays the turn as a genius player:
     * Chooses the first non-empty square in the board, from up to down, then from left to right, starting on
     * the second column. Then puts the given mark in it.
     * This way it blocks Clever's strategy and has a leverage to win (it wins 100% of the games).
     *
     * @param board the current board to play on.
     * @param mark the mark of this player in this turn.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        for (int col = 1; col < board.getSize(); col++)
            for (int row = 0; row < board.getSize(); row++)  // for every cell in board, in cols-order:
                if (board.putMark(mark, row, col)) return;  // if the cell is empty - fill it and return
        // finish with col=0:
        for (int row = 0; row < board.getSize(); row++)  // for every cell in col=0:
            if (board.putMark(mark, row, 0)) return;  // if the cell is empty - fill it and return
        // if there is no empty cell - the game was already over
    }
}
