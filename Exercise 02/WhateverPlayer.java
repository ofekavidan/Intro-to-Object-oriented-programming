import java.util.Random;

/**
 * Represents a random player in the game.
 * Using a random decision to choose coordinates in the board, if they are valid it puts there the given mark.
 */
public class WhateverPlayer implements Player {
    /**
     * Fields:
     */
    private final Random random = new Random();

    /**
     * Plays the turn as a random player:
     * Chooses randomly coordinates with the board's size as a limit, and when they are valid (empty), puts
     * the given mark in them.
     *
     * @param board the current board to play on.
     * @param mark  the mark of this player in this turn.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int row, col;
        do {  // choose random coordinates, as long as the last chosen were occupied
            row = random.nextInt(board.getSize());
            col = random.nextInt(board.getSize());
        } while (!board.putMark(mark, row, col));
    }
}
