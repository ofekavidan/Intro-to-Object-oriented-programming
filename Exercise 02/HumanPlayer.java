
/**
 * Represents a human player in the game.
 * Using an input from the user, representing coordinates in the board, this player put its mark on them, if
 * it is possible and legal.
 */
public class HumanPlayer implements Player {
    /* Constants */
    private final static int TWO_COORDINATES_BASE = 10;

    /*
     * Check the validity of the given coordinates (row, col), and return the opposite boolean value.
     * The validity is defined by the given size as limit.
     * * * I know there is a code duplication here (from Board), but there was no better way to implement it
     * * * (after hours of thinking and also according to the TA teacher).
     *
     * @param row  first coordinate.
     * @param col  second coordinate.
     * @param size the limit to define the validity.
     * @return true if the coordinate is invalid, false otherwise.
     */
    private boolean invalidCoordinates(int row, int col, int size) {
        return !(((-1 < row) && (row < size)) && ((-1 < col) && (col < size)));
    }


    /*
     * Translate the input int from given method <c>KeyboardInput.readInt()</c> into an array of ints,
     * represents the coordinates the user typed.
     * @return the typed coordinates as an ints array.
     */
    private int[] getCoordinates() {
        int chainedCoordinates = KeyboardInput.readInt();
        return new int[]{
                chainedCoordinates / TWO_COORDINATES_BASE,
                chainedCoordinates % TWO_COORDINATES_BASE
        };
    }

    /**
     * Plays the turn as a human player:
     * Asks the user for coordinates, and if they are valid - puts the given mark in them.
     * if not - keep asking for valid ones.
     *
     * @param board the current board to play on.
     * @param mark  the mark of this player in this turn.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        System.out.println(Constants.playerRequestInputString(mark.toString()));
        do {
            int[] coordinates = getCoordinates();
            if (invalidCoordinates(coordinates[0], coordinates[1], board.getSize())) {
                System.out.println(Constants.INVALID_COORDINATE);
            } else if (!board.putMark(mark, coordinates[0], coordinates[1])) {  // if the square is occupied
                System.out.println(Constants.OCCUPIED_COORDINATE);
            } else return;  // else the square wasn't occupied and its coords are valid,
            // therefore the last call to putMark has already played the turn on board.
        } while (true);
    }
}
