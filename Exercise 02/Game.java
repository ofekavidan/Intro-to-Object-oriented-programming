
/**
 * Represents a single round in the whole tournament.
 * Each game knows when it ends and which player won (X, O or draw).
 */
public class Game {
    /* Constants: */
    private static final int DEFAULT_WIN_STREAK = 3;  // the default length of winning streak

    /* Fields: */
    private final Board board;  // the board of this game
    private final int winStreak;  // the length of winning streak of this game
    private final Player playerX;  // the player of Mark.X in this game
    private final Player playerO;  // the player of Mark.O in this game
    private final Renderer renderer;  // the way to present the board of this game

    /**
     * "Default" constructor, initialize a default board and a default winStreak, and setting the other
     * given parameters to their fields.
     *
     * @param playerX  the player of Mark.X in this game.
     * @param playerO  the player of Mark.O in this game.
     * @param renderer the way to present the board of this game.
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        this.board = new Board();
        this.winStreak = DEFAULT_WIN_STREAK;
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    /**
     * Parameterized constructor, initialize a board with the given size, and setting the other given
     * parameters to their fields. Also checks the validity of size and winStreak, according to the rules.
     *
     * @param playerX   the player of Mark.X in this game.
     * @param playerO   the player of Mark.O in this game.
     * @param size      the size of the board of this game.
     * @param winStreak the length of winning streak of this game.
     * @param renderer  the way to present the board of this game.
     */
    public Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        this.board = new Board(size);
        if (winStreak < 2 || size < winStreak) {
            this.winStreak = size;
        } else {
            this.winStreak = winStreak;
        }
        // it is assume-able that 2<=size<=9 and 2<=winStreak<=9,
        // therefore now: 2<=winStreak<=size<=9
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    /**
     * Getter of the winStreak field.
     *
     * @return the length of winning streak of this game.
     */
    public int getWinStreak() {
        return winStreak;
    }

    /**
     * Getter of the size of this game's board.
     *
     * @return the size of the board.
     */
    public int getBoardSize() {
        return board.getSize();
    }

    /*
     * Scans the board in all 4 "positive" direction (right, down, diagonally-up, diagonally-down), starting
     * from given coordinates, in order to find a streak in at least one of them.
     *
     * @param row  first coordinate.
     * @param col  second coordinate.
     * @param mark the Mark of the wanted streak.
     * @return true if there is a streak of mark that starts in (row, col), false otherwise.
     */
    private boolean streakStartsAt(int row, int col, Mark mark) {
        boolean horizontalStreak = true, verticalStreak = true,
                diagonalStreak = true, antiDiagonalStreak = true;
        for (int streakIndex = 1; streakIndex < winStreak; streakIndex++) {
            if (board.getMark(row, col + streakIndex) != mark)
                horizontalStreak = false;  // if the streak was broken horizontally
            if (board.getMark(row + streakIndex, col) != mark)
                verticalStreak = false;  // if the streak was broken vertically
            if (board.getMark(row + streakIndex, col + streakIndex) != mark)
                diagonalStreak = false;  // if the streak was broken diagonally
            if (board.getMark(row - streakIndex, col + streakIndex) != mark)
                antiDiagonalStreak = false;  // if the streak was broken anti-diagonally
        }
        // if there is a streak that wasn't broken, then there is a winner:
        return (horizontalStreak || verticalStreak || diagonalStreak || antiDiagonalStreak);
    }

    /*
     * Finds the winner, by to the current board and the given winStreak, according to the game's rules.
     *
     * @return the Mark of the winner if exists, BLANK if there is a draw, and null otherwise.
     */
    private Mark findWinner() {
        boolean fullBoard = true;
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {  // for every cell in board:
                Mark currentStreakMark = board.getMark(row, col);
                if (currentStreakMark == Mark.BLANK) {  // if the cell is empty
                    fullBoard = false;  // then the board is not full
                    continue;  // and there is no possible streak that starts in it
                }
                // since the loop checks for streak in all the board's cells,
                // it is enough to scan toward positive directions only (meaning 4 out of 8):
                if (streakStartsAt(row, col, currentStreakMark)) return currentStreakMark;
            }
        }
        if (fullBoard) return Mark.BLANK;  // draw
        return null;
    }

    /**
     * Runs the whole game process, and return the winning mark.
     * The game ends when one of the players has a win streak on the board, or when the board is full.
     *
     * @return if one of the players won - return its mark, otherwise - return Mark.BLANK.
     */
    public Mark run() {
        Mark winner = null;
        Mark turn = Mark.X;
        while (winner == null) {  // while neither X nor O won the game, nor was it a draw:
            switch (turn) {
                case X:
                    playerX.playTurn(board, Mark.X);
                    turn = Mark.O;
                    break;
                case O:
                    playerO.playTurn(board, Mark.O);
                    turn = Mark.X;
                    break;
            }
            renderer.renderBoard(board);  // render the board immediately after the turn was played
            winner = findWinner();
        }
        return winner;
    }
}
