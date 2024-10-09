import java.util.Arrays;

/**
 * Runs a series of games between 2 given players, with a given way of rendering the board.
 * The first player plays X on the odd rounds (with even index) and O on the even rounds (with odd index),
 * the second player does the opposite.
 * At the end of the tournament, it prints a message of the results.
 */
public class Tournament {
    /* Constants: */
    private static final String RESULTS_MSG =
            "######### Results #########\n" +
                    "Player 1, %s won: %d rounds\n" +
                    "Player 2, %s won: %d rounds\n" +
                    "Ties: %d\n";  // the format of the message to be printed at the end of the tournament

    /* Fields: */
    private final int rounds;  // number of rounds in this tournament
    private final Renderer renderer;  // the renderer of the board in every game in this tournament
    private final Player[] players;  // an array of the 2 players in this tournament
    private final int[] wins;  // an array of the number of wins of each player

    /**
     * Constructor, initialize the tournament by the given arguments,
     * and set the number of wins of every player to zero.
     *
     * @param rounds   number of games (rounds) to be played.
     * @param renderer the way to present the board in every game.
     * @param player1  the first strategy that will play a turn.
     * @param player2  the second strategy that will play a turn.
     */
    public Tournament(int rounds, Renderer renderer, Player player1, Player player2) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.players = new Player[] {player1, player2};
        this.wins = new int[players.length];
    }

    /**
     * Runs the loop of the tournament. In every iteration there is a new game, that is being run,
     * when the players are switching their Mark. At the end, the message of results is being printed.
     *
     * @param size        the size of the board for every game.
     * @param winStreak   the streak length for winning.
     * @param playerName1 a String of the first player's types (lower-case of given args[4] in cmd-line).
     * @param playerName2 a String of the second player's types (lower-case of given args[5] in cmd-line).
     */
    public void playTournament(int size, int winStreak, String playerName1, String playerName2) {
        for (int roundIndex = 0; roundIndex < rounds; roundIndex++) {
            int x = roundIndex % 2;  // even roundIndex ==> x = 0 ; odd roundIndex ==> x = 1
            int o = 1 - x;  // even roundIndex ==> o = 1 ; odd roundIndex ==> o = 0

            Game game = new Game(players[x], players[o], size, winStreak, renderer);
            Mark winner = game.run();
            switch (winner) {
                case X:
                    wins[x]++;
                    break;
                case O:
                    wins[o]++;
                    break;
                // otherwise - there was a draw, so there is no win to be counted.
            }
        }
        int ties = rounds - Arrays.stream(wins).sum();  // = #(played games) - #(wins)
        System.out.printf(RESULTS_MSG, playerName1, wins[0], playerName2, wins[1], ties);
    }

    /**
     * The main method, generates the fields of the tournament, by analyzing the given Strings arguments.
     * Checks the validity of the players' names (according to their factory's way of work), as this is the
     * only arguments that may be wrong (according to the ex. instructions).
     *
     * @param args command line arguments (an array of Strings).
     */
    public static void main(String[] args) {
        PlayerFactory playerFactory = new PlayerFactory();
        RendererFactory rendererFactory = new RendererFactory();

        int rounds = Integer.parseInt(args[0]);
        // it is assume-able that 2<=size<=9 and 2<=winStreak<=9, in addition -
        // the validity (winStreak<=size) is checked in the Game constructor, as its API demands:
        int size = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);
        // we use ".toLowerCase()" String's method, because capitals in renderer's/player's name are valid:
        Renderer renderer = rendererFactory.buildRenderer(args[3].toLowerCase(), size);
        if (renderer == null) {  // if there was a typo in the renderer name:
            System.out.println(Constants.UNKNOWN_RENDERER_NAME);
            return;
        }
        String[] playerNames = new String[]{args[4].toLowerCase(), args[5].toLowerCase()};
        Player[] players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = playerFactory.buildPlayer(playerNames[i]);
            if (players[i] == null) {  // if there was a typo in some player name:
                System.out.println(Constants.UNKNOWN_PLAYER_NAME);
                return;
            }
        }

        Tournament tournament = new Tournament(rounds, renderer, players[0], players[1]);
        tournament.playTournament(size, winStreak, playerNames[0], playerNames[1]);
    }
}
