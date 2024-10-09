import java.util.Arrays;

/**
 * Represents the board of a single game.
 * In charge of the board's size, squares' marks, and setting / getting its data.
 */
public class Board {
    /* Constants: */
    private static final int DEFAULT_SIZE = 4;  // the default size for the board is (4 x 4)

    /* Fields: */
    private final int size;  // the board's current size
    private final Mark[][] board;  // an array of the board's marks so far in the game: X, O or BLANK

    /**
     * Default constructor, initialize an empty board as a new 2D array of Marks in the default size,
     * filled by the default Mark.BLANK.
     */
    public Board() {
        this(DEFAULT_SIZE);  // delegation
    }

    /**
     * Parameterized constructor, initialize an empty board as a new 2D array of Marks in the given size,
     * filled by the default Mark.BLANK.
     *
     * @param size the size for the board.
     */
    public Board(int size) {
        this.size = size;  // assumes size is natural
        board = new Mark[size][size];
        for (Mark[] row : board) {  // row is a reference to an array of Marks (an element in board).
            Arrays.fill(row, Mark.BLANK);
        }
    }

    /**
     * Getter for the size of the board.
     *
     * @return the size of the board.
     */
    public int getSize() {
        return size;
    }

    /*
     * Check the validity of the given coordinates (row, col), and return the opposite boolean value.
     * The validity is defined by the board's size as limit.
     *
     * @param row first coordinate.
     * @param col second coordinate.
     * @return true if the coordinate is invalid, false otherwise.
     */
    private boolean invalidCoordinates(int row, int col) {
        return !(((-1 < row) && (row < size)) && ((-1 < col) && (col < size)));
    }

    /**
     * Try to mark the square represented by the given coordinates (row, col), with the value of given mark.
     * If the coordinates are invalid or if square is already occupied, return false;
     * otherwise update the mark and return true.
     *
     * @param mark the value to put in the given coordinate.
     * @param row  first coordinate.
     * @param col  second coordinate.
     * @return false if the coordinates are invalid or if the square is occupied, true otherwise.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (invalidCoordinates(row, col)) {
            return false;
        }
        if (board[row][col] != Mark.BLANK) {
            return false;
        }
        board[row][col] = mark;
        return true;
    }

    /**
     * Getter for the mark in the given coordinate.
     *
     * @param row first coordinate.
     * @param col second coordinate.
     * @return the given coordinates' mark, if exists. If not, return the default Mark.BLANK.
     */
    public Mark getMark(int row, int col) {
        if (invalidCoordinates(row, col)) {
            return Mark.BLANK;
        }
        return board[row][col];
    }
}
