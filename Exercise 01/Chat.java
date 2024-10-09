import java.util.Scanner;

/**
 * This class includes the main function, and is responsible for generating a chat between a constant number
 * of ChatterBots. It initializes the arrays of replies to legal and illegal request, names of the
 * ChatterBots, and the ChatterBots themselves.
 *
 * @author Roei Dahuki
 */
class Chat {
    /* Constants: */
    private static final int NUMBER_OF_BOTS = 2;
    private static final String INIT_STATEMENT = "say Hello World!";
    private static final String CHAT_FORMAT = "%s: %s";

    /**
     * main function.
     * Generates arrays to initialize the ChatterBots, then runs the chat in an infinite loop.
     *
     * @param args main's args.
     */
    public static void main(String[] args) {
        /* Initializations for the chat system */
        ChatterBot[] bots = new ChatterBot[NUMBER_OF_BOTS];
        String[] names = generateNamesArray();
        String[][] repliesToLegalRequestArray = generateRepliesToLegalRequestArray();
        String[][] repliesToIllegalRequestArray = generateRepliesToIllegalRequestArray();
        for (int i = 0; i < NUMBER_OF_BOTS; i++) {  // init. ChatterBots:
            bots[i] = new ChatterBot(
                    names[i],
                    repliesToLegalRequestArray[i],
                    repliesToIllegalRequestArray[i]
            );
        }
        String statement = INIT_STATEMENT;
        Scanner scanner = new Scanner(System.in);

        /* Chat's infinite loop */
        while (true) {
            for (var bot : bots) {
                statement = bot.replyTo(statement);
                System.out.printf(CHAT_FORMAT, bot.getName(), statement);
                scanner.nextLine();  // waiting for "Enter" before continuing
            }
        }
    }


    /*
     * Generates an array which its elements are the names for every ChatterBot, and returns it.
     */
    private static String[] generateNamesArray() {
        String[] namesArray = new String[NUMBER_OF_BOTS];
        for (int i = 0; i < NUMBER_OF_BOTS; i++) {
            namesArray[i] = "BOT#" + (i + 1);
        }
        return namesArray;
    }

    /*
     * Generates an array which its elements are the legalRequestsReplies field for every ChatterBot, and
     * returns it.
     */
    private static String[][] generateRepliesToLegalRequestArray() {
        String[][] repliesToLegalRequestArray = new String[NUMBER_OF_BOTS][];
        for (int i = 0; i < NUMBER_OF_BOTS; i++) {
            if (((i + 1) % 2) == 0) {
                repliesToLegalRequestArray[i] = new String[]{
                        ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
                        "you want me to say " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE +
                                ", do you? alright: " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
                        "okay, here goes: " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE};
            } else {
                repliesToLegalRequestArray[i] = new String[]{
                        ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
                        "say " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + "? okay: " +
                                ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
                        "if i will say " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE +
                                "it will make this statement to be very long... " +
                                "but since you've asked, and I can't say no: " +
                                ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE};
            }
        }
        return repliesToLegalRequestArray;
    }

    /*
     * Generates an array which its elements are the repliesToIllegalRequest field for every ChatterBot, and
     * returns it.
     */
    private static String[][] generateRepliesToIllegalRequestArray() {
        String[][] repliesToIllegalRequestArray = new String[NUMBER_OF_BOTS][];
        for (int i = 0; i < NUMBER_OF_BOTS; i++) {
            if (((i + 1) % 2) == 0) {
                repliesToIllegalRequestArray[i] = new String[]{"what ", "say I should say ",
                        "what " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST,
                        "say what? " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "? what's " +
                                ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "?"};
            } else {
                repliesToIllegalRequestArray[i] = new String[]{"whaaat ", "say say ",
                        "say say " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST,
                        "I don't know how to refer to " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST,
                        "ERROR: \"" + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST +
                                "\" is not a legal request!"};
            }
        }
        return repliesToIllegalRequestArray;
    }
}
